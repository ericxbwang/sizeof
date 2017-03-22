package com.fenqile.sizeof;

/**
 *
 * 提供检测java对象占用内存大小功能。
 *
 * @author jason.shang
 */
public interface Sizeable {

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
    long sizeOf(Object object);

    /**
     *
     * 提供计算java对象运行时占用内存大小情况。
     * 如果指定参数onlyMetadata为true, 统计对象占用内存不包括运行时数据，
     * 如果指定参数onlyMetadata为false,统计对象占用和sizeOf(Object object)一致。
     *
     * @param object 统计占用内存对象
     * @param onlyMetadata 标识是否只统计对象元数据大小
     * @return 返回占用内存的字节大小
     *
     * @throws IllegalArgumentException 如果object等于null, 抛出运行时参数不合法异常。
     *
     */
    long sizeOf(Object object, boolean onlyMetadata);

}
