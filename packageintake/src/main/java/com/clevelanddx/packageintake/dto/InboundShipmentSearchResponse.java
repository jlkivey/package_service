package com.clevelanddx.packageintake.dto;

import com.clevelanddx.packageintake.model.InboundShipment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundShipmentSearchResponse {
    private List<InboundShipment> shipments;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
}
