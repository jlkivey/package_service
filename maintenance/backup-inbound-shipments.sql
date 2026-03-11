-- Backup Inbound_Shipments to a backup table (same schema as of migrations V2–V8).
-- Backup table name includes current date: Inbound_Shipments_Backup_yyyyMMdd (e.g. Inbound_Shipments_Backup_20260310).
-- Backup table has no foreign keys or IDENTITY so it can be populated from a SELECT.
-- Run as needed; each run creates a new dated backup table.

SET NOCOUNT ON;

DECLARE @BackupTableName NVARCHAR(128) = N'Inbound_Shipments_Backup_' + CONVERT(NVARCHAR(8), GETDATE(), 112);
DECLARE @Sql NVARCHAR(MAX);

-- Drop existing backup table for today if it exists (so script is re-runnable same day)
SET @Sql = N'IF OBJECT_ID(N''dbo.' + REPLACE(@BackupTableName, N'''', N'''''') + N''', N''U'') IS NOT NULL DROP TABLE dbo.[' + @BackupTableName + N'];';
EXEC sp_executesql @Sql;

-- Create backup table: same columns as Inbound_Shipments, no FKs, Row_ID not IDENTITY
SET @Sql = N'
CREATE TABLE dbo.[' + @BackupTableName + N'] (
    Row_ID                BIGINT          NOT NULL,
    Client                NVARCHAR(MAX)   NULL,
    Tracking_Number       NVARCHAR(MAX)   NULL,
    Status                NVARCHAR(MAX)   NULL,
    Email_ID              NVARCHAR(MAX)   NULL,
    Order_Number          NVARCHAR(MAX)   NULL,
    Ship_Date             DATE            NULL,
    Lab                   NVARCHAR(MAX)   NULL,
    Weight                NVARCHAR(MAX)   NULL,
    Number_Of_Samples     NVARCHAR(MAX)   NULL,
    Pickup_Time           NVARCHAR(MAX)   NULL,
    Pickup_Time_2         NVARCHAR(MAX)   NULL,
    Email_Receive_Datetime DATETIME       NULL,
    Last_Update_Datetime  DATETIME        NULL,
    Scan_Time             DATETIME        NULL,
    Client_ID             BIGINT          NULL,
    Scan_User             NVARCHAR(MAX)   NULL,
    Scanned_Number        NVARCHAR(255)   NULL,
    Shipment_Type         BIGINT          NULL,
    CONSTRAINT [PK_' + @BackupTableName + N'] PRIMARY KEY (Row_ID)
);';
EXEC sp_executesql @Sql;

-- Copy all rows from Inbound_Shipments
SET @Sql = N'
INSERT INTO dbo.[' + @BackupTableName + N'] (
    Row_ID, Client, Tracking_Number, Status, Email_ID, Order_Number, Ship_Date,
    Lab, Weight, Number_Of_Samples, Pickup_Time, Pickup_Time_2,
    Email_Receive_Datetime, Last_Update_Datetime, Scan_Time, Client_ID,
    Scan_User, Scanned_Number, Shipment_Type
)
SELECT
    Row_ID, Client, Tracking_Number, Status, Email_ID, Order_Number, Ship_Date,
    Lab, Weight, Number_Of_Samples, Pickup_Time, Pickup_Time_2,
    Email_Receive_Datetime, Last_Update_Datetime, Scan_Time, Client_ID,
    Scan_User, Scanned_Number, Shipment_Type
FROM dbo.Inbound_Shipments;';
EXEC sp_executesql @Sql;

SELECT @BackupTableName AS BackupTable, @@ROWCOUNT AS RowsBackedUp;
