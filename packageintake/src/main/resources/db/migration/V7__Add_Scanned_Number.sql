-- Add Scanned_Number column to Inbound_Shipments table
ALTER TABLE Inbound_Shipments
ADD Scanned_Number NVARCHAR(255) NULL; 