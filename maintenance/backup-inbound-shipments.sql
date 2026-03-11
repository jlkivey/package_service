-- Backup Inbound_Shipments to a backup table (same schema as of migrations V2–V8).
-- Backup table has no foreign keys or IDENTITY so it can be populated from a SELECT.
-- Run as needed; drop the backup table first if you want to replace a previous backup.

SET NOCOUNT ON;

-- Drop existing backup table if it exists (SQL Server 2016+)
IF OBJECT_ID(N'dbo.Inbound_Shipments_Backup', N'U') IS NOT NULL
    DROP TABLE dbo.Inbound_Shipments_Backup;

-- Create backup table: same columns as Inbound_Shipments, no FKs, Row_ID not IDENTITY
CREATE TABLE dbo.Inbound_Shipments_Backup (
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
    CONSTRAINT PK_Inbound_Shipments_Backup PRIMARY KEY (Row_ID)
);

-- Copy all rows from Inbound_Shipments
INSERT INTO dbo.Inbound_Shipments_Backup (
    Row_ID,
    Client,
    Tracking_Number,
    Status,
    Email_ID,
    Order_Number,
    Ship_Date,
    Lab,
    Weight,
    Number_Of_Samples,
    Pickup_Time,
    Pickup_Time_2,
    Email_Receive_Datetime,
    Last_Update_Datetime,
    Scan_Time,
    Client_ID,
    Scan_User,
    Scanned_Number,
    Shipment_Type
)
SELECT
    Row_ID,
    Client,
    Tracking_Number,
    Status,
    Email_ID,
    Order_Number,
    Ship_Date,
    Lab,
    Weight,
    Number_Of_Samples,
    Pickup_Time,
    Pickup_Time_2,
    Email_Receive_Datetime,
    Last_Update_Datetime,
    Scan_Time,
    Client_ID,
    Scan_User,
    Scanned_Number,
    Shipment_Type
FROM dbo.Inbound_Shipments;

SELECT @@ROWCOUNT AS RowsBackedUp;
