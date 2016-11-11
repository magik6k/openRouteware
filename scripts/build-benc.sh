#!/usr/bin/env bash

source scripts/versions.sh

echo "Building bencode-tools"
echo $PWD

mkdir -p target/ndep/
cd target/ndep/

if [ ! -f ${BENCODE_COMMIT}.zip ]; then
    wget ${BENCODE_ADDR} ${BENCODE_COMMIT}.zip
    unzip ${BENCODE_COMMIT}.zip
    cd ${BENCODE_NAME}
    ./configure
    make
    ar rcs libbencodetools.a bencode.o
fi

