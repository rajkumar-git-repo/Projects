package program;

public class TestProgram {

	public static void main(String[] args) {
		//findBigegstNUmber(10000, 433, 111);
		//swap(10,20);
		//checkLeapYear(2000);
		//findSumOfNumber(538472);
		//System.out.println(fact(-1));
		//binaryToDecimal(0011);
		//sumOfSeries(6);//1+2+4+7+11+16+.......
		//fibonacciSeries(8);
		//printArmstrongNumber(1000);
		//reverseNumber(534563);
		//decimalToBinay(13);
	    //findLcmAndHcf(12,30);
		//findPrimeNumber(100);
		
	}

	private static void findPrimeNumber(int size) {
		for(int i =1; i<=size;i++) {
			boolean flag=true;
			int num = i;
			for(int j=2 ;j<=num/2;j++) {
				if(num % j ==0) {
					flag = false;
				}
			}
			if(flag) {
				System.out.println(i);
			}
		}
	}

	private static void findLcmAndHcf(int x, int y) {
		int a,b;
		a=x ;b=y;
		while(a != b) {
			if(a < b) {
				a=a+x;
			}else {
				b=b+y;
			}
		}
		System.out.println("Lcm:"+a);
		
		a=x;b=y;
		while(a!=b) {
			if(a>b) {
				a=a-b;
			}else {
				b=b-a;
			}
		}
		System.out.println("Hcf:"+a);
	}

	private static void reverseNumber(int num) {
		int reverse = 0;
		while(num > 0) {
			int rem = num %10;
			reverse = reverse*10+rem;
			num = num/10;
		}
		System.out.println(reverse);
	}

	private static void decimalToBinay(int num) {
		int sum=0;
		StringBuilder bin = new StringBuilder();
		int j = 1;
		while(num > 0) {
			int rem = num %2;
			sum = (sum*10)+rem;
			bin.append(rem);
			j=10;
			num = num/2;
		}
		System.out.println(sum);
		System.out.println(bin.reverse());
	}

	private static void printArmstrongNumber(int size) {
		int i=2;
		while(i < 1000) {
			int num = i;
			int sum =0;
			while(num > 0) {
				int rem = num %10;
				sum = sum+(rem*rem*rem);
				num = num/10;
			}
			if(sum == i) {
				System.out.println(i);
			}
			i++;
		}
		
	}

	private static void fibonacciSeries(int size) {
		int x = 0;
		int y =1;
		int z;
		System.out.println("Series : "+y);
		for(int i=1;i<size;i++) {
			z = x+y;
			x=y;
			y=z;
			System.out.println(z);
		}
	}

	private static void sumOfSeries(int size) {
		int sum =0;
		int temp = 1;
		for(int i=1;i<=size;i++) {
			sum =sum+temp;
			//System.out.println("sum:"+sum);
			System.out.println(temp);
			temp=temp+i;
		}
		System.out.println("Sum:"+sum);
	}

	private static void binaryToDecimal(int num) {
		int sum =0;
		int j=1;
		while(num >0) {
			int rem = num %10;
			sum = sum+j*rem;
			j=j*2;
			num = num/10;
		}
		System.out.println("Decimal:"+sum);
	}

	private static long fact(int num) {
		long fact = 1;

		if (num == 0) {
			fact = 1;
		} else {
			fact = num * fact(num - 1);
		}
		return fact;
	}

	private static void findSumOfNumber(int num) {
		int sum = 0;
		int product = 1;
		while(num > 0) {
			int rem = num % 10;
			sum = sum+rem;
			product = product*rem;
			num = num / 10;
		}
		System.out.println("Sum:"+sum);
		System.out.println("Product:"+product);
	}

	private static void checkLeapYear(int year) {
		if ((year % 100 != 0 && year % 4 == 0) || year % 400 == 0) {
			System.out.println("Leap Year");
		}else {
			System.out.println("Not Leap Year");
		}
		
	}

	private static void swap(int i, int j) {
		System.out.println("A="+i+" B="+j);
         i=i+j;
         j=i-j;
         i=i-j;
         System.out.println("A="+i+" B="+j);
	}

	private static void findBigegstNUmber(int i, int j, int k) {
		int big = i > j ? (i > k ? i : k ): (j > k ? j : k);
		System.out.println("Big:"+big);
	}
	
}
