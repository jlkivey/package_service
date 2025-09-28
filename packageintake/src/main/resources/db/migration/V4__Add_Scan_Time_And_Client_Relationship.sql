-- Add Scan_Time column to Inbound_Shipments table
ALTER TABLE Inbound_Shipments
ADD Scan_Time DATETIME NULL;

-- Add foreign key column to Inbound_Shipments table
ALTER TABLE Inbound_Shipments
ADD Client_ID BIGINT NULL;

-- Add foreign key constraint
ALTER TABLE Inbound_Shipments
ADD CONSTRAINT FK_Inbound_Shipments_Client
FOREIGN KEY (Client_ID) REFERENCES Inbound_Shipments_Clients(Row_ID); 