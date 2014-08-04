-- update path to make the sample runnable easily
package.path = './?/init.lua;' .. package.path

local dtls = require 'dtls'
local socket = require 'socket'

-- create UDP socket
local udp = socket.udp();

local port = tonumber(arg[1])
udp:setsockname('*', port+1000)
-- print(port)

-- change UDP socket in DTLS socket
dtls.wrap(udp, {security = "PSK", identity = "Client_identity", key = "WRONGsecretPSK"})

-- DTLS handshake in automaticaly do at first sendto
udp:sendto("my clear data 1\n","127.0.0.1", port)
udp:sendto("my clear data 2\n","127.0.0.1", port)
udp:sendto("my clear data 3\n","127.0.0.1", port)
udp:sendto("my clear data 4\n","127.0.0.1", port)
udp:sendto("my clear data 5\n","127.0.0.1", port)
udp:sendto("my clear data 6\n","127.0.0.1", port)
--udp:sendto("my clear data 7\n","127.0.0.1", port)
print 'done'

