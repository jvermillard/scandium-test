package org.eclipse.californium.scandium.test;

public class ScriptRunner {

    public static Thread runLuaScript(final String script) throws InterruptedException {
        Thread t = new Thread("script " + script) {
            @Override
            public void run() {
                System.out.println("run : lua5.1 " + script);
                try {
                    Process p = Runtime.getRuntime().exec("lua5.1 " + script);
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
