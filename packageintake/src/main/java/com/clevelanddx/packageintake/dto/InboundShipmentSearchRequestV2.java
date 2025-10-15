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
    @Builder.Default
    private Integer page = 0;
    @Builder.Default
    private Integer size = 20;
}
