package com.fenqile.sizeof.support;

import java.util.HashMap;
import java.util.Map;

import static com.fenqile.sizeof.support.SizeOfTool.*;

/**
 *
 *
 *
 * @author jason.shang
 */
public class SizeOfTest {

    public static void main(String[] args){

        testInteger();
        testMap();
        testClass();
    }

    static void testInteger(){
        long size = sizeOf(new Integer(1));
        // 64bit vm , expected: 24
        // 32bit vm , expected: 16
        System.out.println("sizeOf(new Integer(1)):" + size);
    }

    static void testMap(){
        Map<Integer, Integer> map = new HashMap<>();
//        map.put(Integer.valueOf(1), Integer.valueOf(200));
//        map.put(Integer.valueOf(2), Integer.valueOf(201));
        long size = sizeOf(map);


        System.out.println("sizeOf(map->1,200;2,201):" + size);
    }

    static void testClass(){
        class A {
            int a;
            Integer b;
        }

        A a = new A();
        long size = sizeOf(a);

        // 64bit vm , expected: 24
        // 32bit vm , expected: 16
        System.out.println("sizeOf(new A()):" + size);
    }
}
