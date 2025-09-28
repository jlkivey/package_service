CREATE TABLE Inbound_Shipments_Clients (
    Row_ID BIGINT IDENTITY(1,1) PRIMARY KEY,
    Client NVARCHAR(MAX) NULL,
    Last_Update_Datetime DATETIME NULL,
    Last_Update_User NVARCHAR(255) NULL
); 