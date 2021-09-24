package sorting;

import java.util.Arrays;
import java.util.Comparator;

public class SortByMiddleDigit {

	public static void main(String[] args) {
		Integer[] array = {90210,98390,21023,40502,82401,95792};
		sortByMiddleDigit(array);
	}

	private static void sortByMiddleDigit(Integer[] array) {
		Arrays.sort(array, new MyComparator());
		for(int i = 0;i<array.length;i++) {
			System.out.println(array[i]);
		}
	}

}


class MyComparator implements Comparator<Integer>{

	@Override
	public int compare(Integer i1, Integer i2) {
		Integer mid1 = findMiddle((int)i1);
		Integer mid2 = findMiddle((int)i2);
		if(mid1 < mid2) {
			return -1;
		}else if(mid1 > mid2){
			return 1;
		}else {
			return i1.compareTo(i2);
		}
	}
	
	private static int findMiddle(int num) {
		int mid = 0;
		int number1 = num;
		int count1 = 0;
		while(number1 > 0) {
			count1++;
			number1 = number1 /10;
		}
		
		int number2 = num;
		int count2 = 0;
		while(count2 < (count1/2+1)) {
			mid = number2 %10;
			count2++;
			number2 = number2/10;
		}
		return mid;
	}
	
}