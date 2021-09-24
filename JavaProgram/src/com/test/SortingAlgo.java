package com.test;

public class SortingAlgo {

	public static void main(String[] args) {
		int[] array = {3,2,4,5,2,1,7,4,0,11,1,22};
		//bubbleSort(array);
		//selectionSort(array);
		//insertionSort(array);
		print(array);
	}

	private static void insertionSort(int[] array) {
		int j=0;
		for(int i=1;i<array.length;i++) {
			int k = array[i];
			for(j=i-1; j>=0 && k < array[j];j--) {
				array[j+1] = array[j];
			}
			array[j+1]= k;
		}
	}

	private static void selectionSort(int[] array) {
		for(int i=0;i<array.length-1;i++) {
			int min = i;
			for(int j=i+1;j<array.length;j++) {
				if(array[min] > array[j]) {
					min = j;
				}
			}
			if(min != i) {
				int temp = array[min];
				array[min] = array[i];
				array[i] = temp;
			}
		}
		
	}

	private static void print(int[] array) {
		for(int i=0; i<array.length ;i++) {
			System.out.print(array[i]+" ");
		}
		
	}

	private static void bubbleSort(int[] array) {
		boolean flag = false;
		for(int i=0;i<array.length-1;i++) {
			for(int j=0;j<array.length-1-i;j++) {
				if(array[j] > array[j+1]) {
					int temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
					flag = true;
				}
			}
			if(!flag) {
				break;
			}
		}
	}
	
	
}
