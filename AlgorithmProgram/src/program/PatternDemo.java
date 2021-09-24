package program;

public class PatternDemo {

	public static void main(String[] args) {
		//pattern1();
		//pattern2();
		//pattern3();
		//pattern4();
		//pattern5();
		//pattern6();
		pattern7();
	}

	private static void pattern7() {
		int size = 5;
		int count=0;
		for (int i = 1; i <= size; i++) {
			count=size;
			for (int j = 1; j <= size-i; j++) {
				System.out.print(" ");
			}
			for (int j = size-i+1; j <= size+(i-1); j++) {
				if(j <size) {
				   System.out.print(count--);
				}else {
					System.out.print(count++);
				}
			}
			System.out.println();
		}
		for (int i = 1; i <= size-1; i++) {
			count=size-i;
			for (int j =1; j <=i; j++) {
				System.out.print(" ");
			}
			for (int j = i+1; j <= (2*(size))-(i+1); j++) {
				if(j <size) {
					   System.out.print(count++);
					}else {
						System.out.print(count--);
					}
			}
			System.out.println();
		}
		
	}

	private static void pattern6() {
		int size = 5;
		for (int i = 1; i <= size; i++) {
			for (int j =1; j <i; j++) {
				System.out.print(" ");
			}
			for (int j = i; j <= (2*size)-i; j++) {
				System.out.print("*");
			}
			System.out.println();
		}
	}

	private static void pattern5() {
		int size = 5;
		for (int i = 1; i <= size; i++) {
			for (int j = 1; j <= size-i; j++) {
				System.out.print(" ");
			}
			for (int j = size-i+1; j <= size+(i-1); j++) {
				System.out.print("*");
			}
			System.out.println();
		}
	}

	private static void pattern4() {
		for (int i = 1; i <= 5; i++) {

			for (int j = 1; j <= 5-i; j++) {
				System.out.print(" ");
			}
			for (int j = 5-i+1; j <= 5; j++) {
				System.out.print("*");
			}
			System.out.println();
		}
	}

	private static void pattern3() {
		for (int i = 1; i <= 5; i++) {
			
			for (int j = 1; j < i; j++) {
			     System.out.print(" ");
			}
			for (int j = i; j <= 5; j++) {
			     System.out.print("*");
			}
			System.out.println();
		}
	}

	private static void pattern2() {
		for(int i=1;i<=5;i++) {
			for(int j=i;j<=5;j++) {
				System.out.print("*");
			}
			System.out.println();
		}
		
	}

	private static void pattern1() {
		for(int i=1;i<=5;i++) {
			for(int j=1;j<=i;j++) {
				System.out.print("*");
			}
			System.out.println();
		}
	}
}
