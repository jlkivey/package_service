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
}
