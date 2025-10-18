package com.clevelanddx.packageintake.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "Inbound_Shipments_Clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Client information")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Row_ID")
    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @Column(name = "Client")
    @Schema(description = "Client name", example = "Acme Corporation")
    private String client;

    @Column(name = "Last_Update_Datetime")
    @Schema(description = "Last update timestamp", example = "2024-04-11T14:30:00")
    private LocalDateTime lastUpdateTime;

    @Column(name = "Last_Update_User")
    @Schema(description = "Last update user", example = "john.doe")
    private String lastUpdateUser;

    // Manual getters and setters for Lombok compatibility issues
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
} 