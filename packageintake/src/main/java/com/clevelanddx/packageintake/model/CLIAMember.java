package com.clevelanddx.packageintake.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "CLIA_Member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "CLIA Member information")
public class CLIAMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Row_ID")
    @Schema(description = "Unique identifier", example = "1")
    private Long rowId;

    @Column(name = "User_ID", nullable = false, unique = true)
    @Schema(description = "User ID", example = "john.doe")
    private String userId;

    @Column(name = "Last_Update_Datetime")
    @Schema(description = "Last update timestamp", example = "2024-04-11T14:30:00")
    private LocalDateTime lastUpdateDatetime;

    @Column(name = "Last_Update_User")
    @Schema(description = "Last update user", example = "admin")
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
    public static CLIAMemberBuilder builder() { return new CLIAMemberBuilder(); }
    
    public static class CLIAMemberBuilder {
        private Long rowId;
        private String userId;
        private LocalDateTime lastUpdateDatetime;
        private String lastUpdateUser;

        public CLIAMemberBuilder rowId(Long rowId) { this.rowId = rowId; return this; }
        public CLIAMemberBuilder userId(String userId) { this.userId = userId; return this; }
        public CLIAMemberBuilder lastUpdateDatetime(LocalDateTime lastUpdateDatetime) { this.lastUpdateDatetime = lastUpdateDatetime; return this; }
        public CLIAMemberBuilder lastUpdateUser(String lastUpdateUser) { this.lastUpdateUser = lastUpdateUser; return this; }

        public CLIAMember build() {
            CLIAMember member = new CLIAMember();
            member.rowId = this.rowId;
            member.userId = this.userId;
            member.lastUpdateDatetime = this.lastUpdateDatetime;
            member.lastUpdateUser = this.lastUpdateUser;
            return member;
        }
    }
}
