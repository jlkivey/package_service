package com.clevelanddx.packageintake.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundShipmentReferenceDTO {
    private Long rowId;
    private String type;
    private String value;
    private String description;
}
