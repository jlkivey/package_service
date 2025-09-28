package com.clevelanddx.packageintake.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Inbound_Shipments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundShipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Row_ID")
    private Long rowId;

    private String client;

    @Column(name = "Tracking_Number")
    private String trackingNumber;

    @Column(name = "Scanned_Number")
    private String scannedNumber;

    private String status;

    @Column(name = "Email_ID")
    private String emailId;

    @Column(name = "Order_Number")
    private String orderNumber;

    @Column(name = "Ship_Date")
    private LocalDate shipDate;

    private String lab;
    private String weight;

    @Column(name = "Number_Of_Samples")
    private String numberOfSamples;

    @Column(name = "Pickup_Time")
    private String pickupTime;

    @Column(name = "Pickup_Time_2")
    private String pickupTime2;

    @Column(name = "Email_Receive_Datetime")
    private LocalDateTime emailReceiveDatetime;

    @Column(name = "Last_Update_Datetime")
    private LocalDateTime lastUpdateDatetime;

    @Column(name = "Scan_Time")
    private LocalDateTime scanTime;

    @Column(name = "Scan_User")
    private String scanUser;

    @ManyToOne
    @JoinColumn(name = "Client_ID")
    private Client clientEntity;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "Shipment_Type")
    private InboundShipmentReference shipmentType;
} 