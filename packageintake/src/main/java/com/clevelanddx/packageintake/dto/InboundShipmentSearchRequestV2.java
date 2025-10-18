package com.clevelanddx.packageintake.dto;

import java.time.LocalDate;

public class InboundShipmentSearchRequestV2 {
    
    // String fields for partial matching
    private String trackingNumber;
    private String scannedNumber;
    private String status;
    private String orderNumber;
    private String lab;
    private String scanUser;
    private String clientName; // New field for client name search
    
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
    private Integer page = 0;
    private Integer size = 20;
    
    // Constructors
    public InboundShipmentSearchRequestV2() {}
    
    public InboundShipmentSearchRequestV2(String trackingNumber, String scannedNumber, String status, 
                                         String orderNumber, String lab, String scanUser, String clientName,
                                         LocalDate shipDateFrom, LocalDate shipDateTo, LocalDate scanDateFrom, 
                                         LocalDate scanDateTo, LocalDate emailReceiveDatetimeFrom, 
                                         LocalDate emailReceiveDatetimeTo, LocalDate lastUpdateDatetimeFrom, 
                                         LocalDate lastUpdateDatetimeTo, Integer page, Integer size) {
        this.trackingNumber = trackingNumber;
        this.scannedNumber = scannedNumber;
        this.status = status;
        this.orderNumber = orderNumber;
        this.lab = lab;
        this.scanUser = scanUser;
        this.clientName = clientName;
        this.shipDateFrom = shipDateFrom;
        this.shipDateTo = shipDateTo;
        this.scanDateFrom = scanDateFrom;
        this.scanDateTo = scanDateTo;
        this.emailReceiveDatetimeFrom = emailReceiveDatetimeFrom;
        this.emailReceiveDatetimeTo = emailReceiveDatetimeTo;
        this.lastUpdateDatetimeFrom = lastUpdateDatetimeFrom;
        this.lastUpdateDatetimeTo = lastUpdateDatetimeTo;
        this.page = page;
        this.size = size;
    }
    
    // Getters and Setters
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    
    public String getScannedNumber() { return scannedNumber; }
    public void setScannedNumber(String scannedNumber) { this.scannedNumber = scannedNumber; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    
    public String getLab() { return lab; }
    public void setLab(String lab) { this.lab = lab; }
    
    public String getScanUser() { return scanUser; }
    public void setScanUser(String scanUser) { this.scanUser = scanUser; }
    
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    
    public LocalDate getShipDateFrom() { return shipDateFrom; }
    public void setShipDateFrom(LocalDate shipDateFrom) { this.shipDateFrom = shipDateFrom; }
    
    public LocalDate getShipDateTo() { return shipDateTo; }
    public void setShipDateTo(LocalDate shipDateTo) { this.shipDateTo = shipDateTo; }
    
    public LocalDate getScanDateFrom() { return scanDateFrom; }
    public void setScanDateFrom(LocalDate scanDateFrom) { this.scanDateFrom = scanDateFrom; }
    
    public LocalDate getScanDateTo() { return scanDateTo; }
    public void setScanDateTo(LocalDate scanDateTo) { this.scanDateTo = scanDateTo; }
    
    public LocalDate getEmailReceiveDatetimeFrom() { return emailReceiveDatetimeFrom; }
    public void setEmailReceiveDatetimeFrom(LocalDate emailReceiveDatetimeFrom) { this.emailReceiveDatetimeFrom = emailReceiveDatetimeFrom; }
    
    public LocalDate getEmailReceiveDatetimeTo() { return emailReceiveDatetimeTo; }
    public void setEmailReceiveDatetimeTo(LocalDate emailReceiveDatetimeTo) { this.emailReceiveDatetimeTo = emailReceiveDatetimeTo; }
    
    public LocalDate getLastUpdateDatetimeFrom() { return lastUpdateDatetimeFrom; }
    public void setLastUpdateDatetimeFrom(LocalDate lastUpdateDatetimeFrom) { this.lastUpdateDatetimeFrom = lastUpdateDatetimeFrom; }
    
    public LocalDate getLastUpdateDatetimeTo() { return lastUpdateDatetimeTo; }
    public void setLastUpdateDatetimeTo(LocalDate lastUpdateDatetimeTo) { this.lastUpdateDatetimeTo = lastUpdateDatetimeTo; }
    
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
