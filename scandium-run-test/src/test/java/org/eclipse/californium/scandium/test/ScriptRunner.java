package org.eclipse.californium.scandium.test;

import java.util.Arrays;

public class ScriptRunner {

    public static Thread runLuaScript(final String... script) throws InterruptedException {
        Thread t = new Thread("script " + script[0]) {
            @Override
            public void run() {
                System.err.println("run : " + Arrays.toString(script));
                try {
                    Process p = Runtime.getRuntime().exec(script);
                    int returnCode = p.waitFor();
                    System.out.println("script return code: " + returnCode);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        return t;
    }
}
