package com.fenqile.sizeof.support;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 使用当前API方式:
 * <p>1. 通过命令行执行自己的Main方法：java -javaagent:sizeof.jar <your main class>
 * <p>2. 在IDE中启动，JVM参数添加： -javaagent:target/sizeof.jar -XX:-UseCompressedOops,
 *    其中UseCompressedOops参数禁用指针压缩，可以不加。
 *
 * <p>使用当前API：
 * <pre>
 *     class TestClass {
 *         public static void main(String args){
 *             SizeOf sizeOf  = new SizeOf();
 *             sizeOf.sizeOf(new Integer(1));
 *         }
 *     }
 * </pre>
 *
 * 或者采用更简洁的方式：
 *<pre>
 *     import static com.fenqile.sizeof.support.SizeOfTool.*;
 *
 *     class TestClass {
 *         public static void main(String args){
 *             System.out.println(sizeOf(new Integer(1)));
 *         }
 *     }
 *</pre>
 *
 * @see com.fenqile.sizeof.support.SizeOfTool
 *
 * @author jason.shang
 */
public class SizeOf extends AbstractSizable {

    @Override
    protected long doSizeOf(Object object){

        IdentityHashMap<Object, Object> visited = new IdentityHashMap<>();
        Deque<Object> analysing = new LinkedList<>();

        long size = sizeOf(object, visited, analysing);
        while (!analysing.isEmpty()){
            size += sizeOf(analysing.pollFirst(), visited, analysing);
        }

        return size;
    }

    private long sizeOf(Object object, Map<Object, Object> visited, Deque analysing){

        if(object == null || shouldSkip(object, visited)){
            return 0;
        }

        visited.put(object, null);

        // calculate primitive variables 、 member pointers and so on
        long size = instrumentation.getObjectSize(object);

        Class<?> parent = object.getClass();
        while (parent != null) {

            // recursive analysis array elements
            if(parent.isArray()){
                if(parent.getName().length() != 2){
                    int length = Array.getLength(object);
                    for(int i = 0; i < length; i++){
                        analysing.push(Array.get(object, i));
                    }
                }
                return size;
            }

            // recursive analysis map elements
            if(Map.class.isAssignableFrom(parent)){
                Map<Object, Object> pairs = (Map<Object, Object>)object;
                for(Map.Entry<Object, Object> pair : pairs.entrySet()){
                    analysing.push(pair);
                }
                return size;
            }

            Field[] fields = parent.getDeclaredFields();
            for(int i = 0; i < fields.length; i++){
               // ignore repeat calculate static fields
                Field field = fields[i];
                if(!Modifier.isStatic(field.getModifiers())){
                    // ignore repeat calculate primitive fields
                    if(field.getType().isPrimitive()) continue;
                    boolean savedAccessible = field.isAccessible();
                    try{
                        if(!savedAccessible){
                            field.setAccessible(true);
                        }
                        Object added = field.get(object);
                        if(added != null) {
                            analysing.push(added);
                        }
                    } catch (IllegalAccessException e) {
                        // ignore
                    } finally {
                        if(!savedAccessible) field.setAccessible(false);
                    }
                }
            }

            parent = parent.getSuperclass();

        }

        return size;
    }

    private boolean shouldSkip(Object object, Map<Object, Object> visited) {
        if(object instanceof String){
            String value = (String)object;
            if(object == value.intern()){
                return true;
            }
        }
        return visited.containsKey(object);
    }
}