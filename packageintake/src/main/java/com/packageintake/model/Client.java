package com.packageintake.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "Clients")
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Client_ID")
    private Long clientId;

    @Column(name = "Client_Name", nullable = false)
    private String clientName;

    @Column(name = "Active", nullable = false)
    private Boolean active = true;

    @Column(name = "Created_Datetime", nullable = false)
    private LocalDateTime createdDatetime;

    @Column(name = "Last_Update_Datetime", nullable = false)
    private LocalDateTime lastUpdateDatetime;

    @PrePersist
    protected void onCreate() {
        createdDatetime = LocalDateTime.now();
        lastUpdateDatetime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdateDatetime = LocalDateTime.now();
    }
} 