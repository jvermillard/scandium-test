#!/bin/sh
cd /build/californium.scandium
mvn clean install

export LUA_PATH=';;/build/luadtls/build/?/init.lua;'
export LUA_CPATH=';;/build/luadtls/build/?.so;'

cd /build/scandium-run-test
mvn test

