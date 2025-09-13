#!/bin/bash

# Create necessary directories
sudo mkdir -p /opt/packageintake
sudo mkdir -p /var/log/packageintake
sudo mkdir -p /etc/packageintake

# Set ownership
sudo chown -R packageintake:packageintake /opt/packageintake
sudo chown -R packageintake:packageintake /var/log/packageintake
sudo chown -R packageintake:packageintake /etc/packageintake

# Set permissions
sudo chmod 755 /opt/packageintake
sudo chmod 755 /var/log/packageintake
sudo chmod 755 /etc/packageintake

# Copy the systemd service file
sudo cp deploy/systemd/packageintake.service /etc/systemd/system/

# Reload systemd
sudo systemctl daemon-reload

# Enable and start the service
sudo systemctl enable packageintake
sudo systemctl start packageintake

# Check status
sudo systemctl status packageintake 