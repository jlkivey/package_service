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

    // Manual getters and setters for Lombok compatibility issues
    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getScannedNumber() {
        return scannedNumber;
    }

    public void setScannedNumber(String scannedNumber) {
        this.scannedNumber = scannedNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getShipDate() {
        return shipDate;
    }

    public void setShipDate(LocalDate shipDate) {
        this.shipDate = shipDate;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getNumberOfSamples() {
        return numberOfSamples;
    }

    public void setNumberOfSamples(String numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getPickupTime2() {
        return pickupTime2;
    }

    public void setPickupTime2(String pickupTime2) {
        this.pickupTime2 = pickupTime2;
    }

    public LocalDateTime getEmailReceiveDatetime() {
        return emailReceiveDatetime;
    }

    public void setEmailReceiveDatetime(LocalDateTime emailReceiveDatetime) {
        this.emailReceiveDatetime = emailReceiveDatetime;
    }

    public LocalDateTime getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(LocalDateTime lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public LocalDateTime getScanTime() {
        return scanTime;
    }

    public void setScanTime(LocalDateTime scanTime) {
        this.scanTime = scanTime;
    }

    public String getScanUser() {
        return scanUser;
    }

    public void setScanUser(String scanUser) {
        this.scanUser = scanUser;
    }

    public Client getClientEntity() {
        return clientEntity;
    }

    public void setClientEntity(Client clientEntity) {
        this.clientEntity = clientEntity;
    }

    public InboundShipmentReference getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(InboundShipmentReference shipmentType) {
        this.shipmentType = shipmentType;
    }

    // Builder pattern for compatibility
    public static InboundShipmentBuilder builder() {
        return new InboundShipmentBuilder();
    }

    public static class InboundShipmentBuilder {
        private Long rowId;
        private String client;
        private String trackingNumber;
        private String scannedNumber;
        private String status;
        private String emailId;
        private String orderNumber;
        private LocalDate shipDate;
        private String lab;
        private String weight;
        private String numberOfSamples;
        private String pickupTime;
        private String pickupTime2;
        private LocalDateTime emailReceiveDatetime;
        private LocalDateTime lastUpdateDatetime;
        private LocalDateTime scanTime;
        private String scanUser;
        private Client clientEntity;
        private InboundShipmentReference shipmentType;

        public InboundShipmentBuilder rowId(Long rowId) {
            this.rowId = rowId;
            return this;
        }

        public InboundShipmentBuilder client(String client) {
            this.client = client;
            return this;
        }

        public InboundShipmentBuilder trackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
            return this;
        }

        public InboundShipmentBuilder scannedNumber(String scannedNumber) {
            this.scannedNumber = scannedNumber;
            return this;
        }

        public InboundShipmentBuilder status(String status) {
            this.status = status;
            return this;
        }

        public InboundShipmentBuilder emailId(String emailId) {
            this.emailId = emailId;
            return this;
        }

        public InboundShipmentBuilder orderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
            return this;
        }

        public InboundShipmentBuilder shipDate(LocalDate shipDate) {
            this.shipDate = shipDate;
            return this;
        }

        public InboundShipmentBuilder lab(String lab) {
            this.lab = lab;
            return this;
        }

        public InboundShipmentBuilder weight(String weight) {
            this.weight = weight;
            return this;
        }

        public InboundShipmentBuilder numberOfSamples(String numberOfSamples) {
            this.numberOfSamples = numberOfSamples;
            return this;
        }

        public InboundShipmentBuilder pickupTime(String pickupTime) {
            this.pickupTime = pickupTime;
            return this;
        }

        public InboundShipmentBuilder pickupTime2(String pickupTime2) {
            this.pickupTime2 = pickupTime2;
            return this;
        }

        public InboundShipmentBuilder emailReceiveDatetime(LocalDateTime emailReceiveDatetime) {
            this.emailReceiveDatetime = emailReceiveDatetime;
            return this;
        }

        public InboundShipmentBuilder lastUpdateDatetime(LocalDateTime lastUpdateDatetime) {
            this.lastUpdateDatetime = lastUpdateDatetime;
            return this;
        }

        public InboundShipmentBuilder scanTime(LocalDateTime scanTime) {
            this.scanTime = scanTime;
            return this;
        }

        public InboundShipmentBuilder scanUser(String scanUser) {
            this.scanUser = scanUser;
            return this;
        }

        public InboundShipmentBuilder clientEntity(Client clientEntity) {
            this.clientEntity = clientEntity;
            return this;
        }

        public InboundShipmentBuilder shipmentType(InboundShipmentReference shipmentType) {
            this.shipmentType = shipmentType;
            return this;
        }

        public InboundShipment build() {
            InboundShipment shipment = new InboundShipment();
            shipment.rowId = this.rowId;
            shipment.client = this.client;
            shipment.trackingNumber = this.trackingNumber;
            shipment.scannedNumber = this.scannedNumber;
            shipment.status = this.status;
            shipment.emailId = this.emailId;
            shipment.orderNumber = this.orderNumber;
            shipment.shipDate = this.shipDate;
            shipment.lab = this.lab;
            shipment.weight = this.weight;
            shipment.numberOfSamples = this.numberOfSamples;
            shipment.pickupTime = this.pickupTime;
            shipment.pickupTime2 = this.pickupTime2;
            shipment.emailReceiveDatetime = this.emailReceiveDatetime;
            shipment.lastUpdateDatetime = this.lastUpdateDatetime;
            shipment.scanTime = this.scanTime;
            shipment.scanUser = this.scanUser;
            shipment.clientEntity = this.clientEntity;
            shipment.shipmentType = this.shipmentType;
            return shipment;
        }
    }
} 