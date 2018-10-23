package com.example.ov_mm.notes.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static <T, R> List<R> map(List<T> list, Function<T, R> function) {
        List<R> result = new ArrayList<>(list.size());
        for (T item : list) {
            result.add(function.apply(item));
        }
        return result;
    }

    public static <T, R> R[] map(T[] list, Class<R> arrayClass, Function<T, R> function) {
        R[] result = (R[]) Array.newInstance(arrayClass, list.length);
        for (int i = 0; i < list.length; i++) {
            result[i] = function.apply(list[i]);
        }
        return result;
    }

}
