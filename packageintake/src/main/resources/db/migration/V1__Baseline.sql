-- This is a baseline migration that handles existing tables
-- It creates the Flyway schema history table and marks existing tables as applied

-- Create the Flyway schema history table if it doesn't exist
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[flyway_schema_history]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[flyway_schema_history] (
        [installed_rank] INT NOT NULL,
        [version] NVARCHAR(50),
        [description] NVARCHAR(200),
        [type] NVARCHAR(20) NOT NULL,
        [script] NVARCHAR(1000) NOT NULL,
        [checksum] INT,
        [installed_by] NVARCHAR(100) NOT NULL,
        [installed_on] DATETIME NOT NULL DEFAULT GETDATE(),
        [execution_time] INT NOT NULL,
        [success] BIT NOT NULL,
        CONSTRAINT [flyway_schema_history_pk] PRIMARY KEY ([installed_rank])
    );
END

-- Check if Inbound_Shipments table exists and mark it as applied
IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Inbound_Shipments]') AND type in (N'U'))
AND NOT EXISTS (SELECT * FROM [dbo].[flyway_schema_history] WHERE [version] = '2')
BEGIN
    INSERT INTO [dbo].[flyway_schema_history] (
        [installed_rank],
        [version],
        [description],
        [type],
        [script],
        [checksum],
        [installed_by],
        [execution_time],
        [success]
    ) VALUES (
        2,
        '2',
        'Create Inbound Shipments',
        'SQL',
        'V2__Create_Inbound_Shipments.sql',
        0,
        SYSTEM_USER,
        0,
        1
    );
END 