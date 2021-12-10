package cn.tojintao.test;

import java.util.*;

/**
 * @author cjt
 * @date 2021/6/19 22:55
 */
public class MyTest {

    public static void main(String[] args) {
        List<Integer> arrayList = new ArrayList<>(0);
        LinkedList<Object> linkedList = new LinkedList<>();
        Set<Integer> set = new HashSet<>();set.add(1);
        Map<Integer, Integer> hashMap = new HashMap<>();
        Map<Integer, Integer> hashtable = new Hashtable<>();
        String str = Integer.valueOf(1).toString();
        char[] chars = str.toCharArray();
        Stack<Integer> stack = new Stack<>();
        print(arrayList);

    }

    private static void print(List<? extends Object> list) {
        System.out.println(list);
    }

}
