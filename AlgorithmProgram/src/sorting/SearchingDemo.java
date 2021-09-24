package sorting;

public class SearchingDemo {

	public static void main(String[] args) {
		int[] array = {4,3,1,6,8,9};
		int i = linearSearch(array,4);
		if(i==-1) {
			System.out.println("Linear Item not found");
		}else {
			System.out.println("Linear Item found at position :"+i);
		}
		
		int[] sortedArray = {1,3,4,6,8,9};
		int index = binarySearch(sortedArray,5);
		if(index==-1) {
			System.out.println("Binary Item not found");
		}else {
			System.out.println("Binary Item found at position :"+index);
		}
	}

	private static int binarySearch(int[] array, int item) {
		int low = 0;
		int high = array.length-1;
		
		while(low <= high) {
			int mid = (low+high)/2;
			if(array[mid] < item) {
				low = mid+1;
			}else if(array[mid] > item) {
				high = mid-1;
			}else {
				return mid;
			}
		}
		return -1;
	}

	private static int linearSearch(int[] array, int item) {
		for(int i =0;i<array.length;i++) {
			if(array[i] == item) {
				return i;
			}
		}
		return -1;
	}
}
