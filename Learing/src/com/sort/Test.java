package com.sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {

	public static void main(String[] args) {
		List<Integer> list = List.of(1,2,3,4,5);
		
		Integer[] array= list.stream().toArray(Integer[] :: new);
		prime();
	} 
	
	public static  void prime() {
		List<Integer> list= IntStream.range(0, 100).filter(i->isPrime(i)).boxed().collect(Collectors.toList());
		System.out.println(list);
	}
	
	public static boolean isPrime(int number) {
		return IntStream.rangeClosed(2, number/2).noneMatch(i-> number%i ==0);
	}
}
