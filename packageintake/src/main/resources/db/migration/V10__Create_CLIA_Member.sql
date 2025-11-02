CREATE TABLE CLIA_Member (
    Row_ID BIGINT IDENTITY(1,1) PRIMARY KEY,
    User_ID NVARCHAR(255) NOT NULL,
    Last_Update_Datetime DATETIME NULL,
    Last_Update_User NVARCHAR(255) NULL
);

-- Add unique constraint on User_ID
ALTER TABLE CLIA_Member
ADD CONSTRAINT UK_CLIA_Member_User_ID UNIQUE (User_ID);
