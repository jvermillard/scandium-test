package org.eclipse.californium.scandium.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.californium.elements.Connector;
import org.eclipse.californium.elements.RawData;
import org.eclipse.californium.elements.RawDataChannel;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;
import org.junit.Assert;
import org.junit.Test;

public class PskServerTest {

    private List<RawData> rcvd = new ArrayList<>();

    /**
     * This test run a DTLS server with a fixed PSK. A client is supposed to connect and send 6 messages. It's supposed
     * to be ran against an external DTLS client (like tinydtls)
     */
    @Test
    public void basic_tls_psk_test() throws IOException, InterruptedException {
        DTLSConnector dtlsServer = new DTLSConnector(new InetSocketAddress(5684), null);
        try {
            CountDownLatch latch = new CountDownLatch(6);

            InMemoryPskStore pskStore = new InMemoryPskStore();
            // put in the PSK store the default identity/psk for tinydtls tests
            pskStore.setKey("Client_identity", "secretPSK".getBytes());

            dtlsServer.getConfig().setServerPsk(pskStore);

            dtlsServer.setRawDataReceiver(new RawDataChannelImpl(dtlsServer, latch));

            dtlsServer.start();
            ScriptRunner.runLuaScript("lua5.1", "src/test/resources/simple-psk-client.lua", "5684");
            // wait for some data
            Assert.assertTrue(latch.await(10, TimeUnit.SECONDS));
            dtlsServer.stop();
        } finally {
            dtlsServer.stop();
        }
    }

    @Test
    public void tls_psk_error_and_restart_session() throws IOException, InterruptedException {
        DTLSConnector dtlsServer = new DTLSConnector(new InetSocketAddress(5685), null);
        try {
            CountDownLatch latch = new CountDownLatch(6);
            InMemoryPskStore pskStore = new InMemoryPskStore();
            // put in the PSK store the default identity/psk for tinydtls tests
            pskStore.setKey("Client_identity", "secretPSK".getBytes());

            dtlsServer.getConfig().setServerPsk(pskStore);

            dtlsServer.setRawDataReceiver(new RawDataChannelImpl(dtlsServer, latch));

            dtlsServer.start();

            ScriptRunner.runLuaScript("lua5.1", "src/test/resources/wrong-psk-client.lua", "5685");

            // wait for failure
            Thread.sleep(2000);

            // restart session with the correct PSK
            ScriptRunner.runLuaScript("lua5.1", "src/test/resources/simple-psk-client.lua", "5685");
            Assert.assertTrue(latch.await(10, TimeUnit.SECONDS));
        } finally {
            dtlsServer.stop();
        }
    }

    private class RawDataChannelImpl implements RawDataChannel {

        private Connector connector;

        private final CountDownLatch latch;

        public RawDataChannelImpl(Connector con, CountDownLatch latch) {
            this.connector = con;
            this.latch = latch;
        }

        public void receiveData(final RawData raw) {
            System.out.println("RCVD>>" + new String(raw.getBytes()));
            rcvd.add(raw);
            connector.send(new RawData("ACK".getBytes(), raw.getAddress(), raw.getPort()));
            latch.countDown();
        }
    }

}
