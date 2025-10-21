CREATE TABLE CLIA_Admin (
    Row_ID BIGINT IDENTITY(1,1) PRIMARY KEY,
    User_ID NVARCHAR(255) NOT NULL,
    Last_Update_Datetime DATETIME NULL,
    Last_Update_User NVARCHAR(255) NULL
);

-- Add unique constraint on User_ID
ALTER TABLE CLIA_Admin
ADD CONSTRAINT UK_CLIA_Admin_User_ID UNIQUE (User_ID);

-- Insert default admin users
INSERT INTO CLIA_Admin (User_ID, Last_Update_Datetime, Last_Update_User)
VALUES ('jeff.ivey', GETDATE(), 'SYSTEM'),
       ('sean.hennigan', GETDATE(), 'SYSTEM');
