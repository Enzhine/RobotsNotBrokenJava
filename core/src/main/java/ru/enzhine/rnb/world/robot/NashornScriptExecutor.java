package ru.enzhine.rnb.world.robot;

import org.openjdk.nashorn.api.scripting.NashornScriptEngine;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.CompiledScript;
import javax.script.ScriptException;

public class NashornScriptExecutor implements ScriptExecutor {

    private static final NashornScriptEngineFactory nsef = new NashornScriptEngineFactory();
    private NashornScriptEngine nse;
    private boolean onceExecuted;

    public NashornScriptExecutor() {
        reset();
    }

    @Override
    public void reset() {
        this.nse = (NashornScriptEngine) nsef.getScriptEngine();
        this.onceExecuted = false;
    }

    @Override
    public CompiledScript compileScript(StringBuilder script) throws ScriptException {
        return this.nse.compile(script.toString());
    }

    @Override
    public Object invoke(String func, Object... args) throws ScriptException, NoSuchMethodException {
        return this.nse.invokeFunction(func, args);
    }

    @Override
    public void inject(String key, Object value) {
        this.nse.put(key, value);
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
