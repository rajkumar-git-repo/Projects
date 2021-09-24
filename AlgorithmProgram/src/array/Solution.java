package array;

public class Solution {

	public static void main(String[] args) {
		 //int[] array = {1, 3, 6, 4, 1, 2};
		//System.out.println("value:"+solution(array));
		String str = "022";
		System.out.println("count:" + stringSolution(str));
	}

	private static int stringSolution(String str) {
		int count = 0;
		int number = Integer.parseInt(str);
		if(number % 3 == 0) {
			count++;
		}
		char[] charr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		for (int i = 0; i < str.length(); i++) {
			for (int j = 0; j < charr.length; j++) {
				if (str.charAt(i) != charr[j]) {
					String s = str.replace(str.charAt(i), charr[j]);
					int num = Integer.parseInt(s);
					if (num != 0 && num % 3 == 0) {
						System.out.println("num:" + num);
						count++;
					}
				}
			}
		}
		return count;
	}

	//get continues number of length
	private static int solution(int[] array) {
		int value = 0;
		int[] arr = reverse(array);
		boolean flag = false;
		for (int i = 0; i < arr.length - 1; i++) {
			if (array[i] != array[i + 1]) {
				if (arr[i] + 1 != arr[i + 1]) {
					value = arr[i] + 1;
					flag = true;
					break;
				}
			}
		}
		if (!flag) {
			value = arr[arr.length - 1] + 1;
		}
		if (value < 0) {
			value = 1;
		}
		return value;
	}

	public static int[] reverse(int[] array) {
		boolean flag = false;
		for (int i = 0; i < array.length - 1; i++) {
			for (int j = 0; j < array.length - 1 - i; j++) {
				if (array[j] > array[j + 1]) {
					int temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;
					flag = true;
				}
			}
			if (!flag) {
				break;
			}
		}
		return array;
	}

}
