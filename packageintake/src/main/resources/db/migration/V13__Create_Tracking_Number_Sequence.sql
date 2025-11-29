-- ============================================================================
-- Create Sequence for Tracking Number Generation
-- ============================================================================
-- This sequence is used to generate unique incrementing tracking numbers
-- for packages that don't have tracking numbers from carriers.
-- ============================================================================

-- Create sequence starting at 1000000 to ensure 7-digit numbers
-- Increment by 1, no maximum value, cycle disabled
IF NOT EXISTS (SELECT 1 FROM sys.sequences WHERE name = 'SEQ_Tracking_Number')
BEGIN
    CREATE SEQUENCE SEQ_Tracking_Number
    AS BIGINT
    START WITH 1000000
    INCREMENT BY 1
    NO MAXVALUE
    NO CYCLE
    CACHE 10;
END




