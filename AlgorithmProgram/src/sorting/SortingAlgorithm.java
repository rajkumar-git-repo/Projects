package sorting;

public class SortingAlgorithm {

	public static void main(String[] args) {
		int[] array = {6, 4, 5, 2, 1, 7, 8, 3 };
		int[] array1 = {5,8,9,28,34};
		int[] array2 = {4,8,22,25,30,33,40,42};
		//bubbleSort(array);
		//selectionSort(array);
		//insertionSort(array);
		mergeTwoSortedArray(array1,array2);
		//print(array);
	}

	private static void mergeTwoSortedArray(int[] array1, int[] array2) {
		int size = array1.length+array2.length;
		int[] sortedArray = new int[size];
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
		while(i < array1.length) {
			sortedArray[k++] = array1[i];
			i++;
		}
		
		while(j < array2.length) {
			sortedArray[k++] = array2[j];
			j++;
		}
		
		for(int l=0;l<sortedArray.length;l++) {
			System.out.print(sortedArray[l] +" ");
		}
		System.out.println("\nSORTED ARRAY SIZE:"+sortedArray.length);
	}

	private static void insertionSort(int[] array) {
		int k = 0;
		int j = 0;
		int size = array.length;
		for (int i = 1; i < size; i++) {
			k = array[i];
			for (j = i - 1; j >= 0 && k < array[j]; j--) {
				array[j + 1] = array[j];
			}
			System.out.println(j);
			array[j + 1] = k;
		}
	}

	private static void selectionSort(int[] array) {
		int size = array.length;
		for(int i=0;i<size-1;i++) {
			int min = i;
			for(int j = i+1;j<size;j++) {
				if(array[min] > array[j]) {
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
		int size = array.length;
		for (int i = 0; i < size - 1; i++) {
			int flag = 0;
			for (int j = 0; j < size - 1 - i; j++) {
                    if(array[j] > array[j+1]) {
                    	int temp = array[j];
                    	array[j] = array[j+1];
                    	array[j+1] = temp;
                    	flag = 1;
                    }
			}
			if(flag == 0) {
				break;
			}
		}
	}
	
	private static void print(int[] array) {
		for(int i=0;i<array.length;i++) {
			System.out.print(array[i] +" ");
		}
		
	}
}
