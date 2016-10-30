#!/usr/bin/env bash

source scripts/versions.sh

echo "Building libnl"
echo $PWD

mkdir -p target/ndep/
cd target/ndep/

if [ ! -f ${LIBNL_NAME}.tar.gz ]; then
    wget ${LIBNL_ADDR} ${LIBNL_NAME}.tar.gz
    tar xzvf ${LIBNL_NAME}.tar.gz
    cd ${LIBNL_NAME}
    ./configure
    make
fi
