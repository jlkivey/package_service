-- Set Last_Update_Datetime to default to current time when not specified.
-- Backfill existing NULLs. Safe to run again (constraint added only if missing).

DECLARE @ConstraintName sysname;

-- Inbound_Shipments
SET @ConstraintName = N'DF_Inbound_Shipments_Last_Update_Datetime';
IF NOT EXISTS (SELECT 1 FROM sys.default_constraints WHERE name = @ConstraintName)
BEGIN
    ALTER TABLE Inbound_Shipments
    ADD CONSTRAINT DF_Inbound_Shipments_Last_Update_Datetime DEFAULT (GETDATE()) FOR Last_Update_Datetime;
END

UPDATE Inbound_Shipments
SET Last_Update_Datetime = GETDATE()
WHERE Last_Update_Datetime IS NULL;

-- Inbound_Shipments_Clients
SET @ConstraintName = N'DF_Inbound_Shipments_Clients_Last_Update_Datetime';
IF NOT EXISTS (SELECT 1 FROM sys.default_constraints WHERE name = @ConstraintName)
BEGIN
    ALTER TABLE Inbound_Shipments_Clients
    ADD CONSTRAINT DF_Inbound_Shipments_Clients_Last_Update_Datetime DEFAULT (GETDATE()) FOR Last_Update_Datetime;
END

UPDATE Inbound_Shipments_Clients
SET Last_Update_Datetime = GETDATE()
WHERE Last_Update_Datetime IS NULL;
