-- Remove duplicate rows from Inbound_Shipments.
-- Uniqueness: (Tracking_Number, Client). Keeps the row with the highest Row_ID per group, deletes the rest.
-- Run a backup first (e.g. maintenance/backup-inbound-shipments.sql) if you may need to restore.

SET NOCOUNT ON;

-- Preview: how many duplicate rows will be removed (optional; comment out after review)
SELECT COUNT(*) AS RowsToDelete
FROM (
    SELECT Row_ID,
           ROW_NUMBER() OVER (PARTITION BY Tracking_Number, Client ORDER BY Row_ID DESC) AS rn
    FROM dbo.Inbound_Shipments
) x
WHERE rn > 1;

BEGIN TRANSACTION;

;WITH Ranked AS (
    SELECT Row_ID,
           ROW_NUMBER() OVER (PARTITION BY Tracking_Number, Client ORDER BY Row_ID DESC) AS rn
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
