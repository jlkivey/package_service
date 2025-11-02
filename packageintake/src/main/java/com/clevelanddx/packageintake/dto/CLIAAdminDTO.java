package com.clevelanddx.packageintake.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "CLIA Admin data transfer object")
public class CLIAAdminDTO {
    
    @Schema(description = "Unique identifier", example = "1")
    private Long rowId;
    
    @Schema(description = "User ID", example = "admin.user")
    private String userId;
    
    @Schema(description = "Last update timestamp", example = "2024-04-11T14:30:00")
    private LocalDateTime lastUpdateDatetime;
    
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
    public static CLIAAdminDTOBuilder builder() { return new CLIAAdminDTOBuilder(); }
    
    public static class CLIAAdminDTOBuilder {
        private Long rowId;
        private String userId;
        private LocalDateTime lastUpdateDatetime;
        private String lastUpdateUser;

        public CLIAAdminDTOBuilder rowId(Long rowId) { this.rowId = rowId; return this; }
        public CLIAAdminDTOBuilder userId(String userId) { this.userId = userId; return this; }
        public CLIAAdminDTOBuilder lastUpdateDatetime(LocalDateTime lastUpdateDatetime) { this.lastUpdateDatetime = lastUpdateDatetime; return this; }
        public CLIAAdminDTOBuilder lastUpdateUser(String lastUpdateUser) { this.lastUpdateUser = lastUpdateUser; return this; }

        public CLIAAdminDTO build() {
            CLIAAdminDTO dto = new CLIAAdminDTO();
            dto.rowId = this.rowId;
            dto.userId = this.userId;
            dto.lastUpdateDatetime = this.lastUpdateDatetime;
            dto.lastUpdateUser = this.lastUpdateUser;
            return dto;
        }
    }
}
