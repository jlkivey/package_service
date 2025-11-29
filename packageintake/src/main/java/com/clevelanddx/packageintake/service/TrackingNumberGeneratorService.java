package com.clevelanddx.packageintake.service;

/**
 * Service for generating unique tracking numbers using a database sequence.
 * Used for packages that don't have tracking numbers from carriers.
 */
public interface TrackingNumberGeneratorService {
    /**
     * Generates the next unique tracking number from the sequence.
     * 
     * @return The next tracking number as a string
     */
    String generateNextTrackingNumber();
}




