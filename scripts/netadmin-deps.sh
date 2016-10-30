#!/usr/bin/env bash

source scripts/versions.sh

if [ ! -d netadmin/target/lib ]; then
    rm -rf netadmin/target/lib netadmin/target/include
    mkdir -p netadmin/target/lib
    mkdir -p netadmin/target/include

    cp -rv target/ndep/${LIBNL_NAME}/lib/.libs/*.so* netadmin/target/lib
    cp -rv target/ndep/${LIBNL_NAME}/lib/.libs/*.a netadmin/target/lib
    cp -rv target/ndep/${LIBNL_NAME}/include/netlink netadmin/target/include/netlink
fi

