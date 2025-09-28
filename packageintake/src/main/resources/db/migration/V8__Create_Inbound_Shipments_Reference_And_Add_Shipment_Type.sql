-- Create Inbound_Shipments_Reference table
CREATE TABLE Inbound_Shipments_Reference (
    Row_ID BIGINT IDENTITY(1,1) PRIMARY KEY,
    Type NVARCHAR(255) NOT NULL,
    Value NVARCHAR(255) NOT NULL,
    Description NVARCHAR(255) NULL
);

-- Insert initial data
INSERT INTO Inbound_Shipments_Reference (Type, Value, Description) VALUES 
('SHIPPING_TYPE', 'Dry Ice', NULL),
('SHIPPING_TYPE', 'LL Box', NULL),
('SHIPPING_TYPE', 'Courier', NULL);

-- Add Shipment_Type column to Inbound_Shipments table
-- Note: This assumes the Inbound_Shipments table already exists from previous migrations
ALTER TABLE Inbound_Shipments 
ADD Shipment_Type BIGINT NULL;

-- Add foreign key constraint
ALTER TABLE Inbound_Shipments 
ADD CONSTRAINT FK_Inbound_Shipments_Shipment_Type 
FOREIGN KEY (Shipment_Type) REFERENCES Inbound_Shipments_Reference(Row_ID);
