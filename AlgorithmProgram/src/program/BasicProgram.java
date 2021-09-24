package program;

public class BasicProgram {

	public static void main(String[] args) {
		//findBiggestNumber(200,40,72);
		//swap(10,20);
		//checkLeapYear(2016);
		//findSumOfNumber(5432);
		//System.out.println(fact(1));
		//binaryToDecimal(1111);
		//decimalToBinary(25);
		//fibonacciSeries(8);
		//reverseNumber(534563);
		//findLcmAndHcf(12,30);
		//findPrimeNumber(100);
	}
	
	private static void findPrimeNumber(int size) {
		for (int j = 2; j <= size; j++) {
			boolean flag = true;
			int num = j;
			for (int i = 2; i <= num / 2; i++) {
				if (num % i == 0) {
					flag = false;
				}
			}
			if (flag) {
				System.out.print(j+" ");
			}
		}
	}

	private static void findLcmAndHcf(int x, int y) {
       int a,b;
       a=x;b=y;
       while(a != b) {
    	   if(a<b) {
    		   a=a+x;
    	   }else {
    		   b=b+y;
    	   }
       }
       System.out.println("LCM:"+a);
       a=x;b=y;
       while(a!=b) {
    	   if(a>b) {
    		   a=a-b;
    	   }else {
    		   b=b-a;
    	   }
       }
       System.out.println("HCF:"+a);
	}

	private static void reverseNumber(int num) {
		int reverse=0;
		while(num >0) {
			int rem = num%10;
			reverse=(reverse*10)+rem;
			num/=10;
		}
		System.out.println(reverse);
	}

	private static void fibonacciSeries(int num) {
         
		int x,y,z;
		x=0;y=1;
		System.out.print(x+" "+y+" ");
		for(int i=0;i<num;i++) {
			z=x+y;
			System.out.print(z+" ");
			x=y;
			y=z;
		}
	}


	private static void decimalToBinary(int num) {
		StringBuilder builder = new StringBuilder();
		int bin=0;
		int j=1;
		while(num>0) {
			int rem=num%2;
			bin = bin+j*rem;
			j=j*10;
			num/=2;
		}
		System.out.println(bin);
	}

	private static void binaryToDecimal(int num) {
		int dec =0;
		int j=1;
		while(num > 0) {
			System.out.println(num);
			int rem = num %10;
			dec=dec+j*rem;
			j=j*2;
			num=num/10;
			System.out.println(num);
		}
		System.out.println(dec);
	}

	public static int fact(int num) {
		if(num == 0) {
			return 1;
		}
		return num*fact(num-1);
	}

	private static void findSumOfNumber(int num) {
		int sum=0;
		while(num >0) {
			int rem = num %10;
			sum+=rem;
			num/=10;
		}
		System.out.println("Sum:"+sum);
	}

	private static void checkLeapYear(int year) {
		if((year%4 ==0 && year % 100 != 0) || year % 400==0) {
			System.out.println("Leap Year");
		}else {
			System.out.println("Not Leap year");
		}
	}

	private static void swap(int i, int j) {
		System.out.println(i+"--"+j);
		i=i+j;
		j=i-j;
		i=i-j;
		System.out.println(i+"--"+j);
	}

	private static void findBiggestNumber(int i, int j, int k) {
		int num = i>j ? (i>k ? i:k) :(j>k ? j:k);
		System.out.println(num);
	}
}
