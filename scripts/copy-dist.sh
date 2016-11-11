#!/usr/bin/env bash

echo "Copying netadmin"
mkdir -p server/app/internal
rm -f server/app/internal/netadmin
cp netadmin/target/native/netadmin server/app/internal/netadmin
