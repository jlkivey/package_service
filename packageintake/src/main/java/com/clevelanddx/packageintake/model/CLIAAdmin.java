package com.clevelanddx.packageintake.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "CLIA_Admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "CLIA Admin information")
public class CLIAAdmin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Row_ID")
    @Schema(description = "Unique identifier", example = "1")
    private Long rowId;

    @Column(name = "User_ID", nullable = false, unique = true)
    @Schema(description = "User ID", example = "admin.user")
    private String userId;

    @Column(name = "Last_Update_Datetime")
    @Schema(description = "Last update timestamp", example = "2024-04-11T14:30:00")
    private LocalDateTime lastUpdateDatetime;

    @Column(name = "Last_Update_User")
    @Schema(description = "Last update user", example = "system")
    private String lastUpdateUser;

    // Manual getters and setters for Lombok compatibility
    public Long getRowId() { return rowId; }
    public void setRowId(Long rowId) { this.rowId = rowId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public LocalDateTime getLastUpdateDatetime() { return lastUpdateDatetime; }
    public void setLastUpdateDatetime(LocalDateTime lastUpdateDatetime) { this.lastUpdateDatetime = lastUpdateDatetime; }
    public String getLastUpdateUser() { return lastUpdateUser; }
    public void setLastUpdateUser(String lastUpdateUser) { this.lastUpdateUser = lastUpdateUser; }

    // Builder pattern
    public static CLIAAdminBuilder builder() { return new CLIAAdminBuilder(); }
    
    public static class CLIAAdminBuilder {
        private Long rowId;
        private String userId;
        private LocalDateTime lastUpdateDatetime;
        private String lastUpdateUser;

        public CLIAAdminBuilder rowId(Long rowId) { this.rowId = rowId; return this; }
        public CLIAAdminBuilder userId(String userId) { this.userId = userId; return this; }
        public CLIAAdminBuilder lastUpdateDatetime(LocalDateTime lastUpdateDatetime) { this.lastUpdateDatetime = lastUpdateDatetime; return this; }
        public CLIAAdminBuilder lastUpdateUser(String lastUpdateUser) { this.lastUpdateUser = lastUpdateUser; return this; }

        public CLIAAdmin build() {
            CLIAAdmin admin = new CLIAAdmin();
            admin.rowId = this.rowId;
            admin.userId = this.userId;
            admin.lastUpdateDatetime = this.lastUpdateDatetime;
            admin.lastUpdateUser = this.lastUpdateUser;
            return admin;
        }
    }
}
