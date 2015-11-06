package com.simple.mongodb.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Dong Wang.
 * Created on 2014/9/13 上午 11:12.
 * <p>
 * charge if null or empty util tools
 */
public interface EmptyUtil {

    /**
     * charge String symbol is null or empty value ""
     */
    static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * charge number object is null or zero
     */
    static boolean isNullOrZero(Integer num) {
        return num == null || num == 0;
    }

    /**
     * charge number object is null or zero
     */
    static boolean isNullOrZero(Long num) {
        return num == null || num == 0;
    }

    /**
     * charge number object is null or zero
     */
    static boolean isNullOrZero(Double num) {
        return num == null || num == 0;
    }

    /**
     * charge number object is null or zero
     */
    static boolean isNullOrZero(Float num) {
        return num == null || num == 0;
    }

    /**
     * charge number object is null or zero
     */
    static boolean isNullOrZero(Short num) {
        return num == null || num == 0;
    }

    /**
     * charge number object is null or zero
     */
    static boolean isNullOrZero(Byte num) {
        return num == null || num == 0;
    }

    /**
     * charge object array is null or length is zero
     */
    static boolean isNullOrEmpty(Object[] byteArray) {
        return byteArray == null || byteArray.length == 0;
    }

    /**
     * charge int array is null or length is zero
     */
    static boolean isNullOrEmpty(int[] byteArray) {
        return byteArray == null || byteArray.length == 0;
    }

    /**
     * charge long array is null or length is zero
     */
    static boolean isNullOrEmpty(long[] byteArray) {
        return byteArray == null || byteArray.length == 0;
    }

    /**
     * charge double array is null or length is zero
     */
    static boolean isNullOrEmpty(double[] byteArray) {
        return byteArray == null || byteArray.length == 0;
    }

    /**
     * charge float array is null or length is zero
     */
    static boolean isNullOrEmpty(float[] byteArray) {
        return byteArray == null || byteArray.length == 0;
    }

    /**
     * charge short array is null or length is zero
     */
    static boolean isNullOrEmpty(short[] byteArray) {
        return byteArray == null || byteArray.length == 0;
    }

    /**
     * charge byte array is null or length is zero
     */
    static boolean isNullOrEmpty(byte[] byteArray) {
        return byteArray == null || byteArray.length == 0;
    }

    /**
     * charge collection object is null or size is zero
     */
    static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * charge map is null or size is zero
     */
    static boolean isNullOrEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }
}
