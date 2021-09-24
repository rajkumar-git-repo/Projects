package com.sort;

public class SortProgram {

	public static void main(String[] args) {
		int[] array = {2,5,1,7,3,8,4,4,0};
		//bubbleSort(array);
		//selectionSort(array);
		//insertSort(array);
		//print(array);
		
		int[] array1 = {5,8,9,28,34};
		int[] array2 = {4,8,22,25,30,33,40,42};
		mergeTwoSortedArray(array1,array2);
	}

	private static void mergeTwoSortedArray(int[] array1, int[] array2) {
		int size1 = array1.length;
		int size2 = array2.length;
		int[] sortedArray = new int[size1+size2];
		int i=0;
		int j=0;
		int k=0;
		while(i<array1.length && j<array2.length) {
			if(array1[i] <= array2[j]) {
				sortedArray[k++] = array1[i];
				i++;
			}else if(array1[i] > array2[j]) {
				sortedArray[k++] = array2[j];
				j++;
			}
		}
		while(i<array1.length) {
			sortedArray[k++] = array1[i];
			i++;
		}
		
		while(j<array2.length) {
			sortedArray[k++] = array2[j];
			j++;
		}
		print(sortedArray);
	}

	private static void insertSort(int[] array) {
		int j=0;
		for(int i=1;i<array.length;i++) {
			int k = array[i];
			for(j=i-1; j>=0 && k < array[j] ;j--) {
				array[j+1] = array[j];
			}
			array[j+1] =k;
		}
		
	}

	private static void selectionSort(int[] array) {
		for(int i=0;i<array.length-1;i++) {
			int min = i;
			for(int j=i+1;j<array.length;j++) {
				if(array[j] < array[min]) {
					min = j;
				}
			}
			if(min != i) {
			   int temp = array[i];
			   array[i] = array[min];
			   array[min] = temp;
			}
		}
	}

	private static void bubbleSort(int[] array) {
		for(int i=0;i<array.length-1;i++) {
			int flag = 0;
			for(int j=0;j<array.length-i-1;j++) {
				if(array[j] > array[j+1]) {
					int temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
					flag =1;
				}
			}
			if(flag==0) {
				break;
			}
		}
	}

	private static void print(int[] array) {
		for(int i=0;i<array.length;i++) {
			System.out.print(array[i]+" ");
		}
	}
}
