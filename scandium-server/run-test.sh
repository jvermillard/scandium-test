#!/bin/sh
cd /build/californium.scandium
mvn clean install
(mvn test -Pintegration&)
sleep 30

cd /build/luadtls/build
echo RUN DA CLIENT
lua ./scandium.lua
wait

