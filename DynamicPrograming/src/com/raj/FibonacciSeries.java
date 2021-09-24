package com.raj;

public class FibonacciSeries {

	public static void main(String[] args) {
		//fibonacciSeriesSimple(10);
		//fibonacciSeriesArray(9);
		//int item = findFibonacciPosition(5);
		//System.out.println("Item:"+item);
		//int data = findFibonacciPositionRecursion(8);
		//System.out.println("Item:"+data);
		System.out.println(findFactorial(20));
	}

	private static long findFactorial(int num) {
		if(num==0) {
			return 1;
		}else {
			return num*findFactorial(num-1);
		}
	}

	//find item at given position in fibonacci series using recursion
	private static int findFibonacciPositionRecursion(int n) {
		if(n <= 1) {
			return n;
		}else {
			return findFibonacciPositionRecursion(n-2)+findFibonacciPositionRecursion(n-1);
		}
	}

	//find item at given position in fibonacci series
	private static int findFibonacciPosition(int position) {
		int[] fib = new int[position];
		if(position<=1) {
			return position;
		}else {
			fib[0]=0;fib[1]=1;
			for(int i=2;i<position;i++) {
				fib[i]=fib[i-1]+fib[i-2];
			}
			return fib[position-1];
		}
	}


	//By using variable
	private static void fibonacciSeriesSimple(int num) {
		int x=0,y=1,z;
		System.out.print(x+" "+y+" ");
		for(int i=2;i<num;i++) {
			z=x+y;
			System.out.print(z+" ");
			x=y;
			y=z;
		}
	}


	//by using aaray
	private static void fibonacciSeriesArray(int num) {
		int[] array = new int[num];
		if(num > 1) {
			array[0] =0;
			array[1] = 1;
			for(int i=2;i<num;i++) {
				array[i]=array[i-1]+array[i-2];
			}
			for(int i=0;i<array.length;i++) {
				System.out.print(array[i]+" ");
			}
		}else {
			System.out.println("Enter valid size!");
		}
	}
}
