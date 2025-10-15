# Production Deployment Guide

## Prerequisites

1. **Java 17** installed on the target server
2. **Systemd** service manager (standard on most Linux distributions)
3. **User account** named `packageintake` created on the server
4. **Database access** configured and tested
5. **Azure AD credentials** configured

## Pre-Deployment Setup

### 1. Create User Account
```bash
sudo useradd -r -s /bin/false packageintake
```

### 2. Configure Environment Variables
Copy the example environment file and update with actual values:
```bash
cp env.example /etc/packageintake/.env
sudo nano /etc/packageintake/.env
```

**IMPORTANT**: The `.env` file should only contain sensitive data:
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password  
- `AZURE_CLIENT_SECRET`: Azure AD client secret

**Non-sensitive configuration** (ports, URLs, logging levels) should be in `application-prod.properties`.

### 3. Test Database Connection
Ensure the database is accessible from the server with the provided credentials.

## Deployment Steps

### 1. Copy Application Files
```bash
# Copy the JAR file
sudo cp packageintake-0.0.1-SNAPSHOT.jar /opt/packageintake/

# Copy configuration (external override file)
sudo cp application-prod.properties /etc/packageintake/
```

### 2. Run Deployment Script
```bash
sudo ./deploy/deploy.sh
```

This script will:
- Create necessary directories
- Set proper ownership and permissions
- Install the systemd service
- Enable and start the service

### 3. Verify Deployment
```bash
# Check service status
sudo systemctl status packageintake

# Check logs
sudo journalctl -u packageintake -f

# Test application
curl http://localhost:8080/health
```

## Service Management

### Start/Stop/Restart Service
```bash
sudo systemctl start packageintake
sudo systemctl stop packageintake
sudo systemctl restart packageintake
```

### View Logs
```bash
# Real-time logs
sudo journalctl -u packageintake -f

# Recent logs
sudo journalctl -u packageintake --since "1 hour ago"
```

### Update Application
1. Stop the service: `sudo systemctl stop packageintake`
2. Replace the JAR file: `sudo cp new-packageintake-0.0.1-SNAPSHOT.jar /opt/packageintake/`
3. Start the service: `sudo systemctl start packageintake`

## Configuration Files

- **JAR Location**: `/opt/packageintake/packageintake-0.0.1-SNAPSHOT.jar`
- **Configuration Override**: `/etc/packageintake/application-prod.properties` (external config file)
- **Environment**: `/etc/packageintake/.env`
- **Service File**: `/etc/systemd/system/packageintake.service`
- **Logs**: `/var/log/packageintake/` (if configured)

## Configuration Priority

Spring Boot will load configuration in this order (later files override earlier ones):
1. Built-in `application-prod.properties` (inside the JAR)
2. External `/etc/packageintake/application-prod.properties` (override file)
3. Environment variables from `/etc/packageintake/.env`

## Configuration Separation

### `/etc/packageintake/.env` (Sensitive Data Only):
- Database credentials (`DB_USERNAME`, `DB_PASSWORD`)
- API secrets (`AZURE_CLIENT_SECRET`)
- Encryption keys
- Authentication tokens

### `/etc/packageintake/application-prod.properties` (Configuration Only):
- Server ports and URLs
- Database connection strings (non-sensitive parts)
- Logging levels and patterns
- Feature flags
- Business logic settings

## Troubleshooting

### Common Issues

1. **Service won't start**: Check logs with `sudo journalctl -u packageintake`
2. **Database connection issues**: Verify credentials in `/etc/packageintake/.env`
3. **Permission issues**: Ensure `packageintake` user owns `/opt/packageintake`
4. **Port conflicts**: Check if port 8080 is available

### Health Check
The application should be accessible at:
- Health endpoint: `http://localhost:8080/actuator/health`
- Main application: `http://localhost:8080/`

## Security Notes

- Environment file `/etc/packageintake/.env` contains sensitive data - ensure proper permissions
- Service runs as non-root user `packageintake`
- Database credentials should be rotated regularly
- Azure AD client secret should be kept secure
