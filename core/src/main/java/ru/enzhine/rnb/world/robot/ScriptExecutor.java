package ru.enzhine.rnb.world.robot;

import javax.script.CompiledScript;
import javax.script.ScriptException;

public interface ScriptExecutor {
    void inject(String key, Object value);

    Object invoke(String func, Object... args) throws ScriptException, NoSuchMethodException;

    CompiledScript compileScript(StringBuilder script) throws ScriptException;

    void execute(CompiledScript cs) throws ScriptException;

    boolean isOnceExecuted();

    void reset();
}