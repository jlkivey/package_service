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

    // Manual getters and setters for Lombok compatibility issues
    public List<InboundShipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<InboundShipment> shipments) {
        this.shipments = shipments;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    // Builder pattern for compatibility
    public static InboundShipmentSearchResponseBuilder builder() {
        return new InboundShipmentSearchResponseBuilder();
    }

    public static class InboundShipmentSearchResponseBuilder {
        private List<InboundShipment> shipments;
        private long totalElements;
        private int totalPages;
        private int currentPage;
        private int pageSize;
        private boolean hasNext;
        private boolean hasPrevious;

        public InboundShipmentSearchResponseBuilder shipments(List<InboundShipment> shipments) {
            this.shipments = shipments;
            return this;
        }

        public InboundShipmentSearchResponseBuilder totalElements(long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public InboundShipmentSearchResponseBuilder totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public InboundShipmentSearchResponseBuilder currentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public InboundShipmentSearchResponseBuilder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public InboundShipmentSearchResponseBuilder hasNext(boolean hasNext) {
            this.hasNext = hasNext;
            return this;
        }

        public InboundShipmentSearchResponseBuilder hasPrevious(boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
            return this;
        }

        public InboundShipmentSearchResponse build() {
            InboundShipmentSearchResponse response = new InboundShipmentSearchResponse();
            response.shipments = this.shipments;
            response.totalElements = this.totalElements;
            response.totalPages = this.totalPages;
            response.currentPage = this.currentPage;
            response.pageSize = this.pageSize;
            response.hasNext = this.hasNext;
            response.hasPrevious = this.hasPrevious;
            return response;
        }
    }
}
