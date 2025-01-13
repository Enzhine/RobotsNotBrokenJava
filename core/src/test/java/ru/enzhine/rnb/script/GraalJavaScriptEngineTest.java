package ru.enzhine.rnb.script;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.io.IOAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class GraalJavaScriptEngineTest {
    private final ScriptEngineManager sem = new ScriptEngineManager();

    @Test
    public void interruptable__success() throws Exception {
        var jsBuilder = Context.newBuilder("js").allowIO(IOAccess.ALL).allowHostAccess(HostAccess.ALL);
        ScriptEngine jse = GraalJSScriptEngine.create(null, jsBuilder);

        AtomicInteger ai = new AtomicInteger(0);
        jse.put("out", ai);
        jse.eval("""
                function onShutdown() {
                    while(true) {
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
                var invocable = (Invocable) jse;
                invocable.invokeFunction("onShutdown");
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

    @Test
    public void interruptable__tested() throws Exception {
        var jsBuilder = Context.newBuilder("js").allowIO(IOAccess.ALL).allowHostAccess(HostAccess.ALL);
        ScriptEngine jse = GraalJSScriptEngine.create(null, jsBuilder);

        AtomicInteger ai = new AtomicInteger(0);
        jse.put("out", ai);
        jse.eval("""
                function onShutdown() {
                    while(true) {
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
                var invocable = (Invocable) jse;
                invocable.invokeFunction("onShutdown");
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
