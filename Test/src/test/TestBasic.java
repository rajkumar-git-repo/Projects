package test;

public class TestBasic {

	public static void main(String[] args) {
         
         String s2 = new String("raj");
         String s3 = s2.intern();
         
         String s1 = "raj";
         
         System.out.println(s1==s3);
         System.out.println(s2==s3);
	}
}
