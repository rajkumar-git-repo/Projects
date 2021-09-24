package sorting;

public class QuickSortDemo {
	
	static int[] array = {18,16,8,12,15,6,3,9,1,21};//{3,1,6,2,8,11,5};

	public static void main(String[] args) {
		quickSort(0,array.length-1);
		printArray(array);
	}

	private static void quickSort(int l, int h) {
         if(l<h) {
        	 int j = partition(l,h);
        	 quickSort(l, j-1);
        	 quickSort(j+1, h);
         }
	}

	private static int partition(int l, int h) {
		int pivot = array[l];
		int i = l-1;
		int j = h;
		while (i < j) {
			do {
				i++;
			} while (array[i] <= pivot);

			do {
				j--;
			} while (array[j] > pivot);
			
			if (i < j) {
				int temp = array[i];
				array[i] = array[j];
				array[j] = temp;
			}
		}
		int temp = array[j];
		array[j] = array[l];
		array[l] = temp;
		
		return j;
	}
	
	public static void printArray(int arr[]) {
	    int n = arr.length;
	    for (int i = 0; i < n; ++i)
	      System.out.print(arr[i] + " ");
	    System.out.println();
	  }
}
