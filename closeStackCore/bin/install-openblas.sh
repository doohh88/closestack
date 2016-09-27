#!/bin/bash
# Gets the necessary dependencies, compiles OpenBLAS from the latest source
# and perfirms necessary configuration on Ubuntu 15.10
# Tested on Ubuntu Mate 15.10
# author: --wiku--
 
# script should fail if any of below commands fails
set -e
 
# Update package index, install git, gfortran and necessary build tols
sudo apt-get -y update
sudo apt-get -y install git build-essential gfortran
 
# Get the latest OpenBLAS source
git clone https://github.com/xianyi/OpenBLAS.git
 
# Make and install OpenBLAS in /opt/OpenBLAS
cd OpenBLAS
sudo make FC=gfortran
sudo make PREFIX=/opt/OpenBLAS install
 
# Use update-alternatives to let the OS know the new BLAS library
sudo update-alternatives --install "/usr/lib/libblas.so" libblas.so /opt/OpenBLAS/lib/libopenblas.so 500
sudo update-alternatives --install "/usr/lib/libblas.so.3" libblas.so.3 /opt/OpenBLAS/lib/libopenblas.so 500
 
# Same trick for liblapack (link to openblas lib)
sudo update-alternatives --install "/usr/lib/liblapack.so" liblapack.so /opt/OpenBLAS/lib/libopenblas.so 500
sudo update-alternatives --install "/usr/lib/liblapack.so.3" liblapack.so.3 /opt/OpenBLAS/lib/libopenblas.so 500
 
# Use update-alternatives to set OpenBLAS as default system BLAS
sudo update-alternatives --set libblas.so /opt/OpenBLAS/lib/libopenblas.so
sudo update-alternatives --set libblas.so.3 /opt/OpenBLAS/lib/libopenblas.so
sudo update-alternatives --set liblapack.so /opt/OpenBLAS/lib/libopenblas.so
sudo update-alternatives --set liblapack.so.3 /opt/OpenBLAS/lib/libopenblas.so
 
echo "OpenBLAS installed successfully"