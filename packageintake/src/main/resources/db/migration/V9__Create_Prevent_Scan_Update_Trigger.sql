CREATE TRIGGER prevent_scan_update_trigger
ON Inbound_Shipments
INSTEAD OF UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    
    -- Update the record, preserving existing scan_time and scan_user values
    UPDATE Inbound_Shipments
    SET 
        Client = i.Client,
        Tracking_Number = i.Tracking_Number,
        Status = i.Status,
        Email_ID = i.Email_ID,
        Order_Number = i.Order_Number,
        Ship_Date = i.Ship_Date,
        Lab = i.Lab,
        Weight = i.Weight,
        Number_Of_Samples = i.Number_Of_Samples,
        Pickup_Time = i.Pickup_Time,
        Pickup_Time_2 = i.Pickup_Time_2,
        Email_Receive_Datetime = i.Email_Receive_Datetime,
        Last_Update_Datetime = i.Last_Update_Datetime,
        -- Preserve existing scan values if they exist, otherwise use new values
        Scan_Time = CASE 
            WHEN d.Scan_Time IS NOT NULL THEN d.Scan_Time 
            ELSE i.Scan_Time 
        END,
        Client_ID = i.Client_ID,
        Scan_User = CASE 
            WHEN d.Scan_User IS NOT NULL THEN d.Scan_User 
            ELSE i.Scan_User 
        END,
        Scanned_Number = i.Scanned_Number,
        Shipment_Type = i.Shipment_Type
    FROM Inbound_Shipments s
    INNER JOIN inserted i ON s.Row_ID = i.Row_ID
    INNER JOIN deleted d ON s.Row_ID = d.Row_ID;
END;

