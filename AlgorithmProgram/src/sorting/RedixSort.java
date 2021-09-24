package sorting;

public class RedixSort {

	public static void main(String[] args) {
		int[] array = {121, 432, 564, 23, 10000, 45, 788};
		redixSort(array);
	}

	private static void redixSort(int[] array) {
       int max = array[0];
       int count = 0;
       for(int i=0;i<array.length;i++) {
    	   if(max < array[i]) {
    		   max= array[i];
    	   }
       }
       while(max > 0) {
    	   count++;
    	   max=max/10;
       }
       int place = 1;
		while (count > 0) {
			for (int i = 0; i < array.length - 1; i++) {
				for (int j = 0; j < array.length - i - 1; j++) {
					int num1 = array[j]/place;
					int num2 = array[j + 1]/place;
					int rem1 = num1 % 10;
					int rem2 = num2 % 10;
					if (rem1 > rem2) {
						int temp = array[j];
						array[j] = array[j + 1];
						array[j + 1] = temp;
					}
				}
			}
			place=place*10;
			count--;
		}
       
       for(int i=0;i<array.length;i++) {
    	   System.out.print(array[i]+" ");
       }
	}
}
