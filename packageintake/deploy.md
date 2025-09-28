# Create and set ownership of application directories
sudo mkdir -p /opt/packageintake
sudo mkdir -p /var/log/packageintake
sudo mkdir -p /etc/packageintake

# Set ownership to the new user
sudo chown -R packageintake:packageintake /opt/packageintake
sudo chown -R packageintake:packageintake /var/log/packageintake
sudo chown -R packageintake:packageintake /etc/packageintake

# Set appropriate permissions
sudo chmod 755 /opt/packageintake
sudo chmod 755 /var/log/packageintake
sudo chmod 755 /etc/packageintake

# Update system packages
sudo dnf update -y

# Install Java 17 (if not already installed)
sudo dnf install java-17-openjdk-devel

# Install other required tools
sudo dnf install wget curl unzip
