package com.github.cszxyang.compiler;

import java.util.*;

public class Example {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 4);
        System.out.println(list.stream().distinct().count());
    }
}