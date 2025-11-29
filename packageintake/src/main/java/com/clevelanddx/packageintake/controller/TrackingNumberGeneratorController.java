package com.clevelanddx.packageintake.controller;

import com.clevelanddx.packageintake.service.TrackingNumberGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for generating unique tracking numbers.
 * Used for packages that don't have tracking numbers from carriers.
 */
@RestController
@RequestMapping("/api/tracking-number-generator")
@Tag(name = "Tracking Number Generator", description = "API for generating unique tracking numbers")
public class TrackingNumberGeneratorController {

    private final TrackingNumberGeneratorService service;

    @Autowired
    public TrackingNumberGeneratorController(TrackingNumberGeneratorService service) {
        this.service = service;
    }

    @GetMapping("/next")
    @Operation(
        summary = "Generate next tracking number",
        description = "Generates the next unique tracking number from the database sequence. " +
                     "This is used for packages that don't have tracking numbers from carriers."
    )
    @ApiResponse(responseCode = "200", description = "Successfully generated tracking number")
    @ApiResponse(responseCode = "500", description = "Error generating tracking number")
    public ResponseEntity<Map<String, String>> generateNextTrackingNumber() {
        try {
            String trackingNumber = service.generateNextTrackingNumber();
            
            Map<String, String> response = new HashMap<>();
            response.put("trackingNumber", trackingNumber);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to generate tracking number: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}

