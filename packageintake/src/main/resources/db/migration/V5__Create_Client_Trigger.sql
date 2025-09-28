CREATE TRIGGER TR_Inbound_Shipments_Client_Insert
ON Inbound_Shipments
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    
    INSERT INTO Inbound_Shipments_Clients (Client, Last_Update_Datetime, Last_Update_User)
    SELECT DISTINCT i.Client, GETDATE(), SYSTEM_USER
    FROM inserted i
    WHERE i.Client IS NOT NULL
    AND NOT EXISTS (
        SELECT 1 
        FROM Inbound_Shipments_Clients c 
        WHERE c.Client = i.Client
    );
    
    -- Update Client_ID in Inbound_Shipments
    UPDATE i
    SET i.Client_ID = c.Row_ID
    FROM Inbound_Shipments i
    INNER JOIN Inbound_Shipments_Clients c ON i.Client = c.Client
    WHERE i.Client_ID IS NULL;
END; 