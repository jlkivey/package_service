package com.clevelanddx.packageintake.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {
    private Long id;
    private String client;
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

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    // Builder pattern for compatibility
    public static ClientDTOBuilder builder() {
        return new ClientDTOBuilder();
    }

    public static class ClientDTOBuilder {
        private Long id;
        private String client;
        private String lastUpdateUser;

        public ClientDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ClientDTOBuilder client(String client) {
            this.client = client;
            return this;
        }

        public ClientDTOBuilder lastUpdateUser(String lastUpdateUser) {
            this.lastUpdateUser = lastUpdateUser;
            return this;
        }

        public ClientDTO build() {
            ClientDTO dto = new ClientDTO();
            dto.id = this.id;
            dto.client = this.client;
            dto.lastUpdateUser = this.lastUpdateUser;
            return dto;
        }
    }
} 