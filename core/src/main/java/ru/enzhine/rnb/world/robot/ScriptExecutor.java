package ru.enzhine.rnb.world.robot;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

public interface ScriptExecutor {
    void inject(String key, Object value);

    Object invoke(String func, Object... args) throws ScriptException, NoSuchMethodException;

    CompiledScript compileScript(String script) throws ScriptException;

    void execute(CompiledScript cs) throws ScriptException;

    boolean isOnceExecuted();

    void interrupt(Duration timeout) throws TimeoutException;

    void reset();
}
