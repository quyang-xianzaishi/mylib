package com.example.qlibrary.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/27.
 */
public class CollectionUtils {

  public static boolean isEmpty(Collection<?> collection) {
    return null == collection || collection.isEmpty();
  }

  public static boolean isNotEmpty(Collection<?> collection) {
    return !isEmpty(collection);
  }

  public static boolean isEmpty(Map<?, ?> map) {
    return (map == null || map.isEmpty());
  }

  public static boolean isEmpty(Object[] array) {
    return (array == null || array.length == 0);
  }
}
