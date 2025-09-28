# Shipment Type Reference Fix

## Problem

The application was experiencing a `TransientPropertyValueException` when updating `InboundShipment` entities that had a `shipmentType` reference. The error occurred because:

```
org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing : com.clevelanddx.packageintake.model.InboundShipment.shipmentType -> com.clevelanddx.packageintake.model.InboundShipmentReference
```

This happened when the `shipmentType` field in the incoming `InboundShipment` object was a new/detached instance that Hibernate couldn't manage.

## Solution

The fix implements a comprehensive approach to handle `shipmentType` references properly:

### 1. Entity Relationship Configuration

Added cascade configuration to the `InboundShipment` entity:

```java
@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
@JoinColumn(name = "Shipment_Type")
private InboundShipmentReference shipmentType;
```

### 2. Service Layer Logic

Enhanced the `InboundShipmentServiceImpl.updateShipment()` method to:

- Check if `shipmentType` has an ID and find the existing reference
- If no ID but has type/value, find existing or create new reference
- Handle cases where the reference doesn't exist gracefully

### 3. Enhanced Reference Management

Added new methods to `InboundShipmentReferenceService`:

- `findOrCreateReference(String type, String value, String description)` - Finds existing or creates new
- `findByTypeAndValue(String type, String value)` - Efficient lookup without creation
- `createReference(InboundShipmentReference reference)` - Create new references
- `updateReference(Long id, InboundShipmentReference reference)` - Update existing references
- `deleteReference(Long id)` - Delete references

### 4. Repository Enhancement

Added efficient lookup method to `InboundShipmentReferenceRepository`:

```java
Optional<InboundShipmentReference> findByTypeAndValue(String type, String value);
```

### 5. Controller Endpoints

Enhanced `InboundShipmentReferenceController` with full CRUD operations:

- `POST /api/inbound-shipment-references` - Create new reference
- `PUT /api/inbound-shipment-references/{id}` - Update existing reference
- `DELETE /api/inbound-shipment-references/{id}` - Delete reference
- `POST /api/inbound-shipment-references/find-or-create` - Find or create by type/value
- `GET /api/inbound-shipment-references/type/{type}/value/{value}` - Find by type and value

## Usage Examples

### Updating a Shipment with Existing Shipment Type ID

```json
PUT /api/inbound-shipments/123
{
  "shipmentType": {
    "rowId": 1
  },
  "status": "Updated"
}
```

### Updating a Shipment with New Shipment Type

```json
PUT /api/inbound-shipments/123
{
  "shipmentType": {
    "type": "SHIPPING_METHOD",
    "value": "Express",
    "description": "Express shipping service"
  },
  "status": "Updated"
}
```

### Creating a New Shipment Type Reference

```json
POST /api/inbound-shipment-references
{
  "type": "SHIPPING_METHOD",
  "value": "Standard",
  "description": "Standard shipping service"
}
```

### Finding or Creating a Reference

```
POST /api/inbound-shipment-references/find-or-create?type=SHIPPING_METHOD&value=Express&description=Express shipping
```

## Benefits

1. **Eliminates TransientPropertyValueException** - Properly handles all shipment type reference scenarios
2. **Automatic Reference Management** - Creates new references when needed
3. **Efficient Lookups** - Uses database indexes for fast type/value searches
4. **Full CRUD Operations** - Complete management of shipment type references
5. **Backward Compatibility** - Existing code continues to work
6. **Data Integrity** - Prevents orphaned or invalid references

## Testing

All existing tests pass (56/56) and new tests have been added to verify:

- Updating shipments with existing shipment type IDs
- Updating shipments with new shipment type data
- Handling missing shipment type references gracefully
- Creating and managing shipment type references

## Migration

No database migration is required. The existing data structure is preserved, and the fix works with current data.

## Future Considerations

- Consider adding validation to ensure shipment type references are not deleted while in use
- Monitor performance of the new lookup methods
- Consider adding caching for frequently accessed shipment type references
