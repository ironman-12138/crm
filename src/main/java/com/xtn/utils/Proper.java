package com.xtn.utils;

import java.util.*;

public class Proper extends Properties {
    //LinkedHashSet有序，可以保证读取出来顺序不变
    private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();
    /**
     * 读取key集合
     */
    @Override
    public Set<String> stringPropertyNames() {
        Set<String> set = new LinkedHashSet<String>();
        for (Object key : keys) {
            set.add((String) key);
        }
        return set;
    }

    @Override
    public Set<Object> keySet() {
        return keys;
    }

    /**
     * 枚举可以直接进行遍历，但是和iterator一样，遍历过程中不能进行修改删除等操作<br/>
     * 若要在遍历过程中进行修改擦除等操作，建议使用stringPropertyNames方法
     */
    @Override
    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(keys);
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        keys.add(key);
        return super.put(key, value);
    }
    /**
     * 若要移除元素，要重写remove方法
     */
    @Override
    public Object remove(Object o) {
        keys.remove(o);
        return super.remove(o);
    }
}
