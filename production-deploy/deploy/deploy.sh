#!/bin/bash

# Production Deployment Script for Package Intake Service
# This script sets up the application for production deployment

set -e  # Exit on any error

echo "Starting Package Intake production deployment..."

# Check if running as root or with sudo
if [ "$EUID" -ne 0 ]; then
    echo "Please run this script with sudo or as root"
    exit 1
fi

# Check if packageintake user exists
if ! id "packageintake" &>/dev/null; then
    echo "Creating packageintake user..."
    useradd -r -s /bin/false packageintake
fi

# Create necessary directories
echo "Creating directories..."
mkdir -p /opt/packageintake
mkdir -p /var/log/packageintake
mkdir -p /etc/packageintake

# Copy application files
echo "Copying application files..."
cp ../packageintake-0.0.1-SNAPSHOT.jar /opt/packageintake/
cp ../application-prod.properties /etc/packageintake/

# Copy environment file if it exists
if [ -f "../env.example" ] && [ ! -f "/etc/packageintake/.env" ]; then
    echo "Copying environment template..."
    cp ../env.example /etc/packageintake/.env
    echo "IMPORTANT: Please update /etc/packageintake/.env with actual values before starting the service"
fi

# Set ownership
echo "Setting ownership..."
chown -R packageintake:packageintake /opt/packageintake
chown -R packageintake:packageintake /var/log/packageintake
chown -R packageintake:packageintake /etc/packageintake

# Set permissions
echo "Setting permissions..."
chmod 755 /opt/packageintake
chmod 755 /var/log/packageintake
chmod 755 /etc/packageintake
chmod 600 /etc/packageintake/.env 2>/dev/null || true

# Copy the systemd service file
echo "Installing systemd service..."
cp systemd/packageintake.service /etc/systemd/system/

# Reload systemd
echo "Reloading systemd..."
systemctl daemon-reload

# Enable the service
echo "Enabling service..."
systemctl enable packageintake

# Check if environment file has been configured
if grep -q "your_actual_" /etc/packageintake/.env 2>/dev/null; then
    echo "WARNING: Environment file contains placeholder values!"
    echo "Please update /etc/packageintake/.env with actual sensitive values:"
    echo "  - DB_USERNAME: Database username"
    echo "  - DB_PASSWORD: Database password"
    echo "  - AZURE_CLIENT_SECRET: Azure AD client secret"
    echo ""
    echo "Service is enabled but not started. Run 'sudo systemctl start packageintake' after configuring environment."
else
    # Start the service
    echo "Starting service..."
    systemctl start packageintake
fi

# Check status
echo "Checking service status..."
systemctl status packageintake --no-pager

echo "Deployment completed!"
echo "Application should be available at: http://localhost:8080"
echo "Logs can be viewed with: sudo journalctl -u packageintake -f" 