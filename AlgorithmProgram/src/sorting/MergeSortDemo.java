package sorting;

public class MergeSortDemo {

	public static void main(String[] args) {
		int[] array = {4,2,7,3,1,9,33,21,12,0};
		mergeSort(array,0,array.length-1);
		printArray(array);
	}
	
	private static void mergeSort(int[] array, int p, int r) {
		if(p < r) {
			int q = (p+r)/2;
			mergeSort(array, p, q);
			mergeSort(array, q+1, r);
			merge(array,p,q,r);
		}
	}

	private static void merge(int[] array, int p, int q, int r) {
		int lsize=q-p+1;
		int msize=r-q;
		int[] L = new int[lsize];
		int[] M = new int[msize];
		
		for(int i=0;i<lsize;i++) {
			L[i]=array[p+i];
		}
		for(int j=0;j<msize;j++) {
			M[j]=array[q+j+1];
		}
		int i,j,k;
		i=0;
		j=0;
		k=p;
		
		while(i< lsize && j<msize) {
			if(L[i] <= M[j]) {
				array[k] = L[i];
				i++;
			}else {
				array[k] = M[j];
				j++;
			}
			k++;
		}
		while(i<lsize) {
			array[k++]=L[i];
			i++;
		}
		while(j<msize) {
			array[k++]=M[j];
			j++;
		}
	}

	public static void printArray(int arr[]) {
	    int n = arr.length;
	    for (int i = 0; i < n; ++i)
	      System.out.print(arr[i] + " ");
	    System.out.println();
	  }
}
