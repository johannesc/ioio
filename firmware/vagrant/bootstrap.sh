#!/usr/bin/env bash

apt-get update

# java is needed for MPLAB X
#apt-get install default-jre -y

apt-get install openjdk-6-jre-headless -y
# We need curl to download SW
apt-get install curl -y

# We need unzip below
apt-get install unzip -y

file /usr/bin/file | grep -q 64-bit
# 32 bit library support is needed for compiler and its installer
if [ $? -eq 0 ]; then
  apt-get install ia32-libs -y
else
  echo "No need to install 32-bit libraries since we are running under 32 bit os"
fi

if [ ! -d "/opt/microchip/mplabc30/v3.30c" ]; then
  # Install c30 compiler unattended mode.
  # mplab c30 compiler for Linux does not seem to be available oneline anymore so
  # you need to supply a local copy in the /vagrant folder
  if [ ! -f "/vagrant/mplabc30-v3.30c-linux-installer.run" ]; then
    echo "You must have a local copy of mplabc30-v3.30c-linux-installer.run in your vagrant folder"
  else
    /vagrant/mplabc30-v3.30c-linux-installer.run -- --mode unattended
    cd /opt/microchip/mplabc30/v3.30c/bin/bin
    ln -s ../c30_device.info
    ln -s ../device_files
    cd -
  fi

else
  echo "mplabc30 already installed"
fi

# Download and install MPLAB X if not there alreay
if [ ! -d "/opt/microchip/mplabx/mplab_ide/bin" ]; then
  # Use local copy if available
  if [ -f "/vagrant/MPLABX-v2.30-linux-installer.zip" ]; then
    echo "Using local copy of MPLABX-v2.30-linux-installer.zip"
    cp /vagrant/MPLABX-v2.30-linux-installer.zip .
  else
    echo "Downloading MPLABX-v2.30-linux-installer.zip"
    curl -O http://ww1.microchip.com/downloads/en/DeviceDoc/MPLABX-v2.30-linux-installer.zip
  fi
  unzip -q MPLABX-v2.30-linux-installer.zip
  chmod u+x MPLABX-v2.30-linux-installer.sh
  ./MPLABX-v2.30-linux-installer.sh -- --mode unattended
  rm MPLABX-v2.30-linux-installer.zip
  rm MPLABX-v2.30-linux-installer.sh
  # Set up path correctly
  echo 'PATH=/opt/microchip/mplabx/mplab_ide/bin/:$PATH'  > /etc/profile.d/microchip_mplabx.sh
else
  echo "MPLAB X already installed"
fi

