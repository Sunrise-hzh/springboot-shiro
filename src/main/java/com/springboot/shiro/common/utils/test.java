package com.springboot.shiro.common.utils;

import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class test {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("a","b","c","d","f");
        List<String> collect = list.stream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(collect);
        System.out.println(list);

        List<String> collect2 = list.stream().filter(realm -> realm.equals("a") || realm.equals("b")).collect(Collectors.toList());
        System.out.println(collect2);

        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        integers.add(19);
        integers.add(21);

        List<Integer> resultIntList = integers.stream().filter(x -> x<20&&x>1).collect(Collectors.toList());
        System.out.println(resultIntList);
    }

    public static boolean test(Integer t){
        return t>10&&t<20;
    }
}
