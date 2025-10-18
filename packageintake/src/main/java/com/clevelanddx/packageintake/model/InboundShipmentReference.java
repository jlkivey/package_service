package com.clevelanddx.packageintake.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "Inbound_Shipments_Reference")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundShipmentReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Row_ID")
    private Long rowId;

    @Column(name = "Type", length = 255)
    private String type;

    @Column(name = "Value", length = 255)
    private String value;

    @Column(name = "Description", length = 255)
    private String description;

    // Manual getters and setters for Lombok compatibility issues
    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Builder pattern for compatibility
    public static InboundShipmentReferenceBuilder builder() {
        return new InboundShipmentReferenceBuilder();
    }

    public static class InboundShipmentReferenceBuilder {
        private Long rowId;
        private String type;
        private String value;
        private String description;

        public InboundShipmentReferenceBuilder rowId(Long rowId) {
            this.rowId = rowId;
            return this;
        }

        public InboundShipmentReferenceBuilder type(String type) {
            this.type = type;
            return this;
        }

        public InboundShipmentReferenceBuilder value(String value) {
            this.value = value;
            return this;
        }

        public InboundShipmentReferenceBuilder description(String description) {
            this.description = description;
            return this;
        }

        public InboundShipmentReference build() {
            InboundShipmentReference reference = new InboundShipmentReference();
            reference.rowId = this.rowId;
            reference.type = this.type;
            reference.value = this.value;
            reference.description = this.description;
            return reference;
        }
    }
}
