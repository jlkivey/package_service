-- Duplication detection: finds Inbound_Shipments rows with the same Tracking_Number
-- for a single Client (excludes 'N/A'). Returns up to 1000 rows, ordered by newest duplicate set first.
WITH DuplicateTrackingNumbers AS (
    SELECT [Tracking_Number], MAX([Row_ID]) AS MaxRowId
    FROM [dbo].[Inbound_Shipments]
    WHERE [Tracking_Number] <> 'N/A'
    GROUP BY [Tracking_Number]
    HAVING COUNT(*) > 1
       AND COUNT(DISTINCT [Client]) = 1
)
SELECT TOP (1000)
    s.[Row_ID],
    s.[Client],
    s.[Tracking_Number],
    s.[Status],
    s.[Email_ID],
    s.[Order_Number],
    s.[Ship_Date],
    s.[Lab],
    s.[Weight],
    s.[Number_Of_Samples],
    s.[Pickup_Time],
    s.[Pickup_Time_2],
    s.[Email_Receive_Datetime],
    s.[Last_Update_Datetime],
    s.[Scan_Time],
    s.[Client_ID],
    s.[Scan_User],
    s.[Scanned_Number],
    s.[Shipment_Type]
FROM [dbo].[Inbound_Shipments] s
INNER JOIN DuplicateTrackingNumbers d ON s.[Tracking_Number] = d.[Tracking_Number]
WHERE s.[Tracking_Number] <> 'N/A'
ORDER BY d.MaxRowId DESC, s.[Row_ID] DESC;
