package ru.enzhine.rnb.script;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openjdk.nashorn.api.scripting.NashornScriptEngine;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NashornEngineTest {
    private final NashornScriptEngineFactory nsef = new NashornScriptEngineFactory();

    @Test
    public void __tested() throws Exception {
        NashornScriptEngine nse = (NashornScriptEngine) nsef.getScriptEngine();
        AtomicInteger ai = new AtomicInteger(0);
        nse.put("out", ai);
        nse.eval("""
                function onShutdown() {
                    while(true) {
                        if (java.lang.Thread.interrupted()){
                            break;
                        }
                        out.incrementAndGet();
                    }
                }
                function onBoot() {
                    print("onBoot");
                }
                """);
        ThreadPoolExecutor es = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        var future = es.submit(() -> {
            try {
                nse.invokeFunction("onShutdown");
            } catch (ScriptException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException te) {
            future.cancel(true);
        }
        var was = ai.get();
        System.out.println(es.getActiveCount());
        Thread.sleep(1000 * 2);
        System.out.println(es.getActiveCount());
        Assertions.assertEquals(was, ai.get());
    }
}
