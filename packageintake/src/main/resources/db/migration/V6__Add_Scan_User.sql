-- Add Scan_User column to Inbound_Shipments table
ALTER TABLE Inbound_Shipments
ADD Scan_User NVARCHAR(MAX) NULL; 