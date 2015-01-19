#!/usr/bin/env bash

apt-get update

# java is needed for MPLAB X
#apt-get install default-jre -y

apt-get install openjdk-6-jre-headless -y
# We need curl to download SW
apt-get install curl -y

# We need unzip below
apt-get install unzip -y

# 32 bit library support is needed for compiler and its installer
apt-get install ia32-libs -y

# Install c30 compiler unattended mode. 
# mplab c30 compiler for Linux does not seem to be available oneline anymore so
# you need to supply a local copy in the /vagrant folder
/vagrant/mplabc30-v3.30c-linux-installer.run -- --mode unattended
cd /opt/microchip/mplabc30/v3.30c/bin/bin
ln -s ../c30_device.info
ln -s ../device_files
cd -

# Download and install MPLAB X
curl -O http://ww1.microchip.com/downloads/en/DeviceDoc/MPLABX-v2.30-linux-installer.zip
unzip -q MPLABX-v2.30-linux-installer.zip 
chmod a+x MPLABX-v2.30-linux-installer.sh
./MPLABX-v2.30-linux-installer.sh -- --mode unattended
# Set up path correctly
echo 'PATH=/opt/microchip/mplabx/mplab_ide/bin/:$PATH'  > /etc/profile.d/microchip_mplabx.sh

# Install git
apt-get install git -y

