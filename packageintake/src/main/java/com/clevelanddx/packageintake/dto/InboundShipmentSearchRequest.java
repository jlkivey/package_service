package com.clevelanddx.packageintake.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundShipmentSearchRequest {
    
    // String fields for partial matching
    private String trackingNumber;
    private String scannedNumber;
    private String status;
    private String orderNumber;
    private String lab;
    private String scanUser;
    
    // Date fields for range searching
    private LocalDate shipDateFrom;
    private LocalDate shipDateTo;
    private LocalDate scanDateFrom;
    private LocalDate scanDateTo;
    private LocalDate emailReceiveDatetimeFrom;
    private LocalDate emailReceiveDatetimeTo;
    private LocalDate lastUpdateDatetimeFrom;
    private LocalDate lastUpdateDatetimeTo;
    
    // Pagination
    @Builder.Default
    private Integer page = 0;
    @Builder.Default
    private Integer size = 20;

    // Manual getters and setters for Lombok compatibility issues
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getScanUser() {
        return scanUser;
    }

    public void setScanUser(String scanUser) {
        this.scanUser = scanUser;
    }

    public LocalDate getShipDateFrom() {
        return shipDateFrom;
    }

    public void setShipDateFrom(LocalDate shipDateFrom) {
        this.shipDateFrom = shipDateFrom;
    }

    public LocalDate getShipDateTo() {
        return shipDateTo;
    }

    public void setShipDateTo(LocalDate shipDateTo) {
        this.shipDateTo = shipDateTo;
    }

    public LocalDate getScanDateFrom() {
        return scanDateFrom;
    }

    public void setScanDateFrom(LocalDate scanDateFrom) {
        this.scanDateFrom = scanDateFrom;
    }

    public LocalDate getScanDateTo() {
        return scanDateTo;
    }

    public void setScanDateTo(LocalDate scanDateTo) {
        this.scanDateTo = scanDateTo;
    }

    public LocalDate getEmailReceiveDatetimeFrom() {
        return emailReceiveDatetimeFrom;
    }

    public void setEmailReceiveDatetimeFrom(LocalDate emailReceiveDatetimeFrom) {
        this.emailReceiveDatetimeFrom = emailReceiveDatetimeFrom;
    }

    public LocalDate getEmailReceiveDatetimeTo() {
        return emailReceiveDatetimeTo;
    }

    public void setEmailReceiveDatetimeTo(LocalDate emailReceiveDatetimeTo) {
        this.emailReceiveDatetimeTo = emailReceiveDatetimeTo;
    }

    public LocalDate getLastUpdateDatetimeFrom() {
        return lastUpdateDatetimeFrom;
    }

    public void setLastUpdateDatetimeFrom(LocalDate lastUpdateDatetimeFrom) {
        this.lastUpdateDatetimeFrom = lastUpdateDatetimeFrom;
    }

    public LocalDate getLastUpdateDatetimeTo() {
        return lastUpdateDatetimeTo;
    }

    public void setLastUpdateDatetimeTo(LocalDate lastUpdateDatetimeTo) {
        this.lastUpdateDatetimeTo = lastUpdateDatetimeTo;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    // Builder pattern for compatibility
    public static InboundShipmentSearchRequestBuilder builder() {
        return new InboundShipmentSearchRequestBuilder();
    }

    public static class InboundShipmentSearchRequestBuilder {
        private String trackingNumber;
        private String scannedNumber;
        private String status;
        private String orderNumber;
        private String lab;
        private String scanUser;
        private LocalDate shipDateFrom;
        private LocalDate shipDateTo;
        private LocalDate scanDateFrom;
        private LocalDate scanDateTo;
        private LocalDate emailReceiveDatetimeFrom;
        private LocalDate emailReceiveDatetimeTo;
        private LocalDate lastUpdateDatetimeFrom;
        private LocalDate lastUpdateDatetimeTo;
        private Integer page = 0;
        private Integer size = 20;

        public InboundShipmentSearchRequestBuilder trackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
            return this;
        }

        public InboundShipmentSearchRequestBuilder scannedNumber(String scannedNumber) {
            this.scannedNumber = scannedNumber;
            return this;
        }

        public InboundShipmentSearchRequestBuilder status(String status) {
            this.status = status;
            return this;
        }

        public InboundShipmentSearchRequestBuilder orderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
            return this;
        }

        public InboundShipmentSearchRequestBuilder lab(String lab) {
            this.lab = lab;
            return this;
        }

        public InboundShipmentSearchRequestBuilder scanUser(String scanUser) {
            this.scanUser = scanUser;
            return this;
        }

        public InboundShipmentSearchRequestBuilder shipDateFrom(LocalDate shipDateFrom) {
            this.shipDateFrom = shipDateFrom;
            return this;
        }

        public InboundShipmentSearchRequestBuilder shipDateTo(LocalDate shipDateTo) {
            this.shipDateTo = shipDateTo;
            return this;
        }

        public InboundShipmentSearchRequestBuilder scanDateFrom(LocalDate scanDateFrom) {
            this.scanDateFrom = scanDateFrom;
            return this;
        }

        public InboundShipmentSearchRequestBuilder scanDateTo(LocalDate scanDateTo) {
            this.scanDateTo = scanDateTo;
            return this;
        }

        public InboundShipmentSearchRequestBuilder emailReceiveDatetimeFrom(LocalDate emailReceiveDatetimeFrom) {
            this.emailReceiveDatetimeFrom = emailReceiveDatetimeFrom;
            return this;
        }

        public InboundShipmentSearchRequestBuilder emailReceiveDatetimeTo(LocalDate emailReceiveDatetimeTo) {
            this.emailReceiveDatetimeTo = emailReceiveDatetimeTo;
            return this;
        }

        public InboundShipmentSearchRequestBuilder lastUpdateDatetimeFrom(LocalDate lastUpdateDatetimeFrom) {
            this.lastUpdateDatetimeFrom = lastUpdateDatetimeFrom;
            return this;
        }

        public InboundShipmentSearchRequestBuilder lastUpdateDatetimeTo(LocalDate lastUpdateDatetimeTo) {
            this.lastUpdateDatetimeTo = lastUpdateDatetimeTo;
            return this;
        }

        public InboundShipmentSearchRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }

        public InboundShipmentSearchRequestBuilder size(Integer size) {
            this.size = size;
            return this;
        }

        public InboundShipmentSearchRequest build() {
            InboundShipmentSearchRequest request = new InboundShipmentSearchRequest();
            request.trackingNumber = this.trackingNumber;
            request.scannedNumber = this.scannedNumber;
            request.status = this.status;
            request.orderNumber = this.orderNumber;
            request.lab = this.lab;
            request.scanUser = this.scanUser;
            request.shipDateFrom = this.shipDateFrom;
            request.shipDateTo = this.shipDateTo;
            request.scanDateFrom = this.scanDateFrom;
            request.scanDateTo = this.scanDateTo;
            request.emailReceiveDatetimeFrom = this.emailReceiveDatetimeFrom;
            request.emailReceiveDatetimeTo = this.emailReceiveDatetimeTo;
            request.lastUpdateDatetimeFrom = this.lastUpdateDatetimeFrom;
            request.lastUpdateDatetimeTo = this.lastUpdateDatetimeTo;
            request.page = this.page;
            request.size = this.size;
            return request;
        }
    }
}
