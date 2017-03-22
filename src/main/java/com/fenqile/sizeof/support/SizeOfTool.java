package com.fenqile.sizeof.support;

/**
 *
 * 便捷使用SizeOf辅助类
 *
 * @author jason.shang
 */
public class SizeOfTool {

    private static final SizeOf sizeOf = new SizeOf();

    public static long sizeOf(Object object){
        return sizeOf.sizeOf(object);
    }

    public static long sizeOf(Object object, boolean onlyMetadata){
        return sizeOf.sizeOf(object, onlyMetadata);
    }
}
