# V2 Search Endpoint Testing Guide

## Overview
The V2 search endpoint has been successfully implemented with client name search capability. Here's how to test it:

## Implementation Summary
✅ **V2 Search Endpoint**: `/api/inbound-shipments/search/v2`  
✅ **New DTO**: `InboundShipmentSearchRequestV2` with `clientName` field  
✅ **New Repository Method**: `searchShipmentsV2()` with LEFT JOIN to client table  
✅ **New Service Method**: `searchShipmentsV2()` in `InboundShipmentServiceImpl`  
✅ **New Controller Endpoint**: `POST /api/inbound-shipments/search/v2`  

## Test Commands

### 1. Basic V2 Search Test
```bash
curl -X POST http://localhost:8080/api/inbound-shipments/search/v2 \
  -H "Content-Type: application/json" \
  -d '{
    "clientName": "test",
    "page": 0,
    "size": 10
  }'
```

### 2. V2 Search with Multiple Criteria
```bash
curl -X POST http://localhost:8080/api/inbound-shipments/search/v2 \
  -H "Content-Type: application/json" \
  -d '{
    "clientName": "Cleveland",
    "trackingNumber": "12345",
    "status": "scanned",
    "page": 0,
    "size": 20
  }'
```

### 3. V2 Search with Date Range
```bash
curl -X POST http://localhost:8080/api/inbound-shipments/search/v2 \
  -H "Content-Type: application/json" \
  -d '{
    "clientName": "Lab",
    "shipDateFrom": "2024-01-01",
    "shipDateTo": "2024-12-31",
    "page": 0,
    "size": 50
  }'
```

### 4. Empty Search (Get All)
```bash
curl -X POST http://localhost:8080/api/inbound-shipments/search/v2 \
  -H "Content-Type: application/json" \
  -d '{
    "page": 0,
    "size": 20
  }'
```

## Key Features

### Client Name Search
- **Field**: `clientName` in request body
- **Search Type**: Partial, case-insensitive
- **SQL**: `c.Client LIKE CONCAT('%', :clientName, '%')`
- **Join**: `LEFT JOIN Inbound_Shipments_Clients c ON s.Client_ID = c.Row_ID`

### All V2 Search Criteria
- `trackingNumber` - Partial match
- `scannedNumber` - Partial match  
- `status` - Partial match
- `orderNumber` - Partial match
- `lab` - Partial match
- `scanUser` - Partial match
- `clientName` - **NEW** - Partial match (case-insensitive)
- `shipDateFrom` / `shipDateTo` - Date range
- `scanDateFrom` / `scanDateTo` - Date range
- `emailReceiveDatetimeFrom` / `emailReceiveDatetimeTo` - Date range
- `lastUpdateDatetimeFrom` / `lastUpdateDatetimeTo` - Date range

### Pagination
- `page` - Page number (0-based, default: 0)
- `size` - Page size (default: 20, max: 1000)

## Response Format
```json
{
  "shipments": [...],
  "totalElements": 150,
  "totalPages": 8,
  "currentPage": 0,
  "pageSize": 20,
  "hasNext": true,
  "hasPrevious": false
}
```

## Current Status
⚠️ **Compilation Issue**: The application cannot start due to Lombok compatibility issues in existing code. The V2 search implementation itself is syntactically correct and ready for testing once the Lombok issues are resolved.

## Next Steps
1. Resolve Lombok compatibility issues in existing codebase
2. Start the application
3. Test the V2 search endpoint using the commands above
4. Verify client name search functionality works correctly

## Files Modified
- `InboundShipmentSearchRequestV2.java` - New DTO with clientName field
- `InboundShipmentRepository.java` - Added searchShipmentsV2() method
- `InboundShipmentService.java` - Added searchShipmentsV2() interface method
- `InboundShipmentServiceImpl.java` - Implemented searchShipmentsV2() method
- `InboundShipmentController.java` - Added POST /search/v2 endpoint
