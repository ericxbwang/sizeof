package com.fenqile.sizeof.support;

import com.fenqile.sizeof.Sizeable;

import java.lang.instrument.Instrumentation;

/**
 *
 * 提供检测Java运行时对象占用大小工具类。
 *
 * @author jason.shang
 */
public abstract class AbstractSizable implements Sizeable {

    protected static Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        AbstractSizable.instrumentation = instrumentation;
    }

    public long sizeOf(Object object){
        return sizeOf(object, false);
    }

    public long sizeOf(Object object, boolean onlyMetadata){

        if(instrumentation == null){
            throw new IllegalStateException("Can not access instrumentation environment.\n" +
                    "Please check if jar file containing SizeOf class is \n" +
                    "specified in the java's \"-javaagent\" command line argument. \n" +
                    "usage: java -javaagent:sizeof-version.jar <your main class>");
        }

        if(object == null){
            throw new IllegalArgumentException("Invalid argument error, calculate object cant't be null");
        }
        return onlyMetadata ? instrumentation.getObjectSize(object) : doSizeOf(object);
    }

    /**
     *
     * 提供计算java对象运行时占用内存大小情况，
     * 统计占用的内存包括：java对象头 + 运行时数据 + 数据对齐。
     *
     * @param object 统计占用内存对象
     * @return 返回占用内存的字节大小
     *
     * @throws IllegalArgumentException 如果object等于null, 抛出运行时参数不合法异常。
     *
     */
    protected abstract long doSizeOf(Object object);

}
