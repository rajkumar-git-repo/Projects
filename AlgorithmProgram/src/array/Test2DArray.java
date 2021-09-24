package array;

public class Test2DArray {

	public static void main(String[] args) {
		int[][] array = {{11,12,13,14,15,16},{21,22,23,24,25,26},{31,32,33,34,35,36},{41,42,43,44,45,46},{51,52,53,54,55,56},{61,62,63,64,65,66}};
		printDiagonal(array);
		System.out.println("Array element are:");
		print(array);
	}

	//print array diagonaly
	private static void printDiagonal(int[][] array) {
		int size = array.length;
		System.out.println("Size:"+size);
		int temp=0;
		boolean flag = false;
		int k = 1;
		while(temp < size && temp > -1) {
			if (!flag) {
				int i = temp;
				int j = 0;
				while (i <= temp && j <= temp) {
					System.out.print(" " + array[i--][j++]);
				}
				System.out.println();

				temp++;
			}
			if(temp == size) {
				temp--;
				flag = true;
			}
			if(flag) {
				int last = size-1;
				int i = size-1;
				int j = k;
				while ((i <= last && j <= last)) {
					System.out.print(" " + array[i--][j++]);
				}
				System.out.println();
				k++;
				temp--;
			}
		}
	}

	private static void print(int[][] array) {
        for(int i=0;i<array.length;i++) {
        	for(int j=0;j<array.length;j++) {
        		System.out.print("   "+array[i][j]);
        	}
        	System.out.println();
        }
	}
}
