SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;

CREATE OR ALTER PROCEDURE dbo.MergeInboundShipmentByTrackingNumber
    @Tracking_Number nvarchar(255),
    @Rehearsal       bit = 0   -- 1 = report what would happen only; no delete/update
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    DECLARE 
        @TrackingNumberTrimmed nvarchar(255),
        @SourceRowID bigint,   -- exact tracking match (batch row)
        @TargetRowID bigint;   -- scanned-number partial match (manual row)

    SET @TrackingNumberTrimmed = LTRIM(RTRIM(@Tracking_Number));

    IF @TrackingNumberTrimmed IS NULL OR @TrackingNumberTrimmed = ''
    BEGIN
        THROW 50001, 'Tracking number is required and cannot be null or empty.', 1;
    END;

    BEGIN TRANSACTION;

    -------------------------------------------------------------------------
    -- When 3+ rows match (same Tracking_Number or Scanned_Number contains it):
    -- We pick exactly ONE source (TOP 1 by recency) and ONE target (TOP 1 by
    -- preference). Only that pair is merged; other matching rows are unchanged.
    -- Call the procedure again to merge another pair if needed.
    -------------------------------------------------------------------------

    -------------------------------------------------------------------------
    -- 1) Find source row:
    --    exact match on Tracking_Number = @Tracking_Number
    --    filter out null/empty tracking numbers
    -------------------------------------------------------------------------
    SELECT TOP (1)
        @SourceRowID = s.Row_ID
    FROM dbo.Inbound_Shipments s
    WHERE s.Tracking_Number IS NOT NULL
      AND LTRIM(RTRIM(s.Tracking_Number)) <> ''
      AND s.Tracking_Number = @TrackingNumberTrimmed
    ORDER BY 
        ISNULL(s.Email_Receive_Datetime, '19000101') DESC,
        ISNULL(s.Last_Update_Datetime, '19000101') DESC,
        s.Row_ID DESC;

    IF @SourceRowID IS NULL
    BEGIN
        ROLLBACK TRANSACTION;
        THROW 50002, 'No source row found with an exact Tracking_Number match.', 1;
    END;

    -------------------------------------------------------------------------
    -- 2) Find target row:
    --    scanned_number contains the tracking number as substring
    --    filter out null/empty scanned numbers
    --    exclude the source row itself
    --
    --    Preference:
    --      a) rows with null/empty Tracking_Number first
    --      b) then most recently scanned / latest row
    -------------------------------------------------------------------------
    SELECT TOP (1)
        @TargetRowID = t.Row_ID
    FROM dbo.Inbound_Shipments t
    WHERE t.Row_ID <> @SourceRowID
      AND t.Scanned_Number IS NOT NULL
      AND LTRIM(RTRIM(t.Scanned_Number)) <> ''
      AND CHARINDEX(@TrackingNumberTrimmed, t.Scanned_Number) > 0
    ORDER BY
        CASE 
            WHEN t.Tracking_Number IS NULL OR LTRIM(RTRIM(t.Tracking_Number)) = '' THEN 0 
            ELSE 1 
        END,
        ISNULL(t.Scan_Time, '19000101') DESC,
        t.Row_ID DESC;

    IF @TargetRowID IS NULL
    BEGIN
        ROLLBACK TRANSACTION;
        THROW 50003, 'No target row found where Scanned_Number contains the tracking number.', 1;
    END;

    IF @TargetRowID = @SourceRowID
    BEGIN
        ROLLBACK TRANSACTION;
        THROW 50004, 'Source row and target row resolved to the same row. Merge aborted.', 1;
    END;

    -------------------------------------------------------------------------
    -- 3) Snapshot source row before deleting it
    -------------------------------------------------------------------------
    DECLARE @Source TABLE
    (
        Row_ID bigint,
        Client nvarchar(255),
        Tracking_Number nvarchar(255),
        Status nvarchar(255),
        Email_ID nvarchar(max),
        Order_Number nvarchar(255),
        Ship_Date date,
        Lab nvarchar(max),
        Weight nvarchar(max),
        Number_Of_Samples nvarchar(max),
        Pickup_Time nvarchar(max),
        Pickup_Time_2 nvarchar(max),
        Email_Receive_Datetime datetime,
        Last_Update_Datetime datetime,
        Client_ID bigint
    );

    INSERT INTO @Source
    (
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
        Client_ID
    )
    SELECT
        s.Row_ID,
        s.Client,
        s.Tracking_Number,
        s.Status,
        s.Email_ID,
        s.Order_Number,
        s.Ship_Date,
        s.Lab,
        s.Weight,
        s.Number_Of_Samples,
        s.Pickup_Time,
        s.Pickup_Time_2,
        s.Email_Receive_Datetime,
        GETDATE(),           -- new Last_Update_Datetime for merged target
        s.Client_ID
    FROM dbo.Inbound_Shipments s
    WHERE s.Row_ID = @SourceRowID;

    -------------------------------------------------------------------------
    -- Rehearsal: report what would happen and exit without changing data
    -------------------------------------------------------------------------
    IF @Rehearsal = 1
    BEGIN
        SELECT
            1 AS Rehearsal,
            @TrackingNumberTrimmed AS Would_Merge_Tracking_Number,
            @SourceRowID           AS Would_Delete_Source_Row_ID,
            @TargetRowID           AS Would_Update_Target_Row_ID;

        SELECT
            'Source (would be deleted)' AS Row_Type,
            s.Row_ID,
            s.Client,
            s.Tracking_Number,
            s.Status,
            s.Email_ID,
            s.Order_Number,
            s.Ship_Date,
            s.Scan_Time,
            s.Scan_User,
            s.Scanned_Number
        FROM dbo.Inbound_Shipments s
        WHERE s.Row_ID = @SourceRowID;

        SELECT
            'Target (would be updated with source data)' AS Row_Type,
            t.Row_ID,
            t.Client,
            t.Tracking_Number,
            t.Status,
            t.Email_ID,
            t.Order_Number,
            t.Ship_Date,
            t.Scan_Time,
            t.Scan_User,
            t.Scanned_Number
        FROM dbo.Inbound_Shipments t
        WHERE t.Row_ID = @TargetRowID;

        ROLLBACK TRANSACTION;
        RETURN;
    END;

    -------------------------------------------------------------------------
    -- 4) Delete the duplicate batch row first
    --    This avoids unique-key conflicts when target gets same tracking/client.
    -------------------------------------------------------------------------
    DELETE FROM dbo.Inbound_Shipments
    WHERE Row_ID = @SourceRowID;

    IF @@ROWCOUNT <> 1
    BEGIN
        ROLLBACK TRANSACTION;
        THROW 50005, 'Failed to delete the source duplicate row.', 1;
    END;

    -------------------------------------------------------------------------
    -- 5) Update the manual row with source/email values
    --    Never change:
    --      Row_ID
    --      Scan_Time
    --      Client_ID
    --      Scan_User
    --      Scanned_Number
    --      Shipment_Type
    -------------------------------------------------------------------------
    UPDATE t
    SET
        t.Client                 = s.Client,
        t.Tracking_Number        = s.Tracking_Number,
        t.Status                 = s.Status,
        t.Email_ID               = s.Email_ID,
        t.Order_Number           = s.Order_Number,
        t.Ship_Date              = s.Ship_Date,
        t.Lab                    = s.Lab,
        t.Weight                 = s.Weight,
        t.Number_Of_Samples      = s.Number_Of_Samples,
        t.Pickup_Time            = s.Pickup_Time,
        t.Pickup_Time_2          = s.Pickup_Time_2,
        t.Email_Receive_Datetime = s.Email_Receive_Datetime,
        t.Last_Update_Datetime   = GETDATE()
    FROM dbo.Inbound_Shipments t
    CROSS JOIN @Source s
    WHERE t.Row_ID = @TargetRowID;

    IF @@ROWCOUNT <> 1
    BEGIN
        ROLLBACK TRANSACTION;
        THROW 50006, 'Failed to update the target row.', 1;
    END;

    COMMIT TRANSACTION;

    -------------------------------------------------------------------------
    -- 6) Return what happened
    -------------------------------------------------------------------------
    SELECT
        0 AS Rehearsal,
        @TrackingNumberTrimmed AS Merged_Tracking_Number,
        @SourceRowID           AS Deleted_Source_Row_ID,
        @TargetRowID           AS Updated_Target_Row_ID;
END;
