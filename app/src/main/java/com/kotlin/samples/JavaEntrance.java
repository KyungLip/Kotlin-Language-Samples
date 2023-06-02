package com.kotlin.samples;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author liupeng
 * @version v1.0
 * @e-mail kyliupeng@didiglobal.com
 * @date 2023/5/5 10:14 上午
 * @Desc
 */
public class JavaEntrance {
    class People {
    }

    class Man extends People {
    }

    public void entrance() {
        TestA testA = new TestA();
        testA.getNum();
    }

    static class TestA {
        private int num;

        public int getNum() {
            Thread thread = Thread.currentThread();
            StackTraceElement[] stackTraceElements = thread.getStackTrace();
            if (stackTraceElements != null) {
                StackTraceElement traceElement;
                for (int i = 0; i < stackTraceElements.length; i++) {
                    traceElement = stackTraceElements[i];
                    Log.i("kylp", "ClassName:" + traceElement.getClassName());
                    Log.i("kylp", "MethodName:" + traceElement.getMethodName());
                    Log.i("kylp", "LineNumber:" + traceElement.getLineNumber());
                }
            }

            return num;
        }

        public synchronized void updateNum() {
            num++;
        }
    }

}
