-- Remove duplicate rows from Inbound_Shipments.
-- Uniqueness: (Tracking_Number, Client).
-- Keeps one row per group: prefer rows that have Scan_Time or Scan_User; then highest Row_ID wins.
-- Run a backup first (e.g. maintenance/backup-inbound-shipments.sql) if you may need to restore.

SET NOCOUNT ON;

-- Order: 1) has scan info (Scan_Time or Scan_User), 2) highest Row_ID
-- (1 = has Scan_Time or non-null/non-empty Scan_User; 0 = no scan info)
SELECT COUNT(*) AS RowsToDelete
FROM (
    SELECT Row_ID,
           ROW_NUMBER() OVER (
               PARTITION BY Tracking_Number, Client
               ORDER BY
                   CASE WHEN Scan_Time IS NOT NULL OR (Scan_User IS NOT NULL AND LTRIM(RTRIM(Scan_User)) <> '') THEN 1 ELSE 0 END DESC,
                   Row_ID DESC
           ) AS rn
    FROM dbo.Inbound_Shipments
) x
WHERE rn > 1;

BEGIN TRANSACTION;

;WITH Ranked AS (
    SELECT Row_ID,
           ROW_NUMBER() OVER (
               PARTITION BY Tracking_Number, Client
               ORDER BY
                   CASE WHEN Scan_Time IS NOT NULL OR (Scan_User IS NOT NULL AND LTRIM(RTRIM(Scan_User)) <> '') THEN 1 ELSE 0 END DESC,
                   Row_ID DESC
           ) AS rn
    FROM dbo.Inbound_Shipments
)
DELETE s
FROM dbo.Inbound_Shipments s
INNER JOIN Ranked r ON s.Row_ID = r.Row_ID
WHERE r.rn > 1;

SELECT @@ROWCOUNT AS RowsDeleted;

-- Review result; then either COMMIT TRANSACTION or ROLLBACK TRANSACTION
-- COMMIT TRANSACTION;
ROLLBACK TRANSACTION;
