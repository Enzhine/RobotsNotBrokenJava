package ru.enzhine.rnb.world.robot;


import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.io.IOAccess;

import javax.script.*;

public class GraalJavaScriptExecutor implements ScriptExecutor {

    private static final Context.Builder jsEngineConfig =
            Context.newBuilder("js").allowIO(IOAccess.ALL).allowHostAccess(HostAccess.SCOPED);

    private ScriptEngine jse;
    private boolean onceExecuted;

    public GraalJavaScriptExecutor() {
        reset();
    }

    @Override
    public void reset() {
        this.jse = GraalJSScriptEngine.create(null, jsEngineConfig);
        this.onceExecuted = false;
    }

    @Override
    public CompiledScript compileScript(String script) throws ScriptException {
        var compilable = (Compilable) jse;
        return compilable.compile(script);
    }

    @Override
    public Object invoke(String func, Object... args) throws ScriptException, NoSuchMethodException {
        var invokable = (Invocable) jse;
        return invokable.invokeFunction(func, args);
    }

    @Override
    public void inject(String key, Object value) {
        jse.put(key, value);
    }

    @Override
    public void execute(CompiledScript cs) throws ScriptException {
        onceExecuted = true;
        cs.eval();
    }

    @Override
    public boolean isOnceExecuted() {
        return onceExecuted;
    }
}
