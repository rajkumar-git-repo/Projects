package array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestArray {
	

	
	public static void main(String[] args) {
		int[] array = { 4, 5, 2, 1, 7, 8, 3 };
		//int[] arrayFirst = {2, 3, 4, 8, 10, 16};
		//int[] arraySecond = {4, 8, 14, 40};
		//String[] array1 = {"Python", "JAVA", "PHP", "C#", "C++", "SQL"};
	    //String[] array2 = {"MySQL", "SQL", "SQLite", "Oracle", "PostgreSQL", "DB2", "JAVA"};
		sortArray(array);
		//sumArrayValue(array);
		//printGrid();
		//deleteGivenElement(array,1);
		//findMaxMin(array);
		//findSecondMaxMin(array);
		//reverse(array);
		//finDuplicate(array);
	    //findCommonString(array1,array2);
	    //removeDuplicate(array);
	    //findSumPair(array,6);
	    //findCommonNumber(array,arrayFirst,arraySecond);
	    //moveAllZeroAtEnd(array);
	    //findEvenOddCount(array);
	    //findSpecificNumber(array, 77, 65);
	    //findNumberOfUniqueElement(array);
	    //findLongestConsecutiveSequenceLength(array);
		//findSumOfUniqueTriplets(array,2);
		//findMejorityElement(array);
		//findFisrtSecondSmallest(array);
		//shift01(array);
		//cyclicRotation(array);
		//arrangePositiveNegativeValue(array);
		//createBiggestNumber(array);
		print(array);
	}
	
	//9=[9, 90, 991, 95, 99]
	private static void getBiggestNumberStartWithSameDigit(int[] array) {
		
	}
	
	//7,9,90,99,89,12,5,3
	private static void createBiggestNumber(int[] array) {
		int size = array.length;
		HashMap<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		//sortArray(array);
		StringBuilder builder = new StringBuilder();
		for(int i= 0; i< size;i++) {
			List<Integer> list = new ArrayList<Integer>();
			String str = array[i]+"";
			for (int j = 0; j <= 9; j++) {
				if (str.substring(0, 1).equals(j+"")) {
					System.out.println(j+"----"+str.substring(0, 1));
					map = getDigitMap(j, array[i], list, map);
					builder.append(str);
				}
			}
		}
		System.out.println(map);
	}

	private static HashMap<Integer, List<Integer>> getDigitMap(int key, int value, List<Integer> list , HashMap<Integer, List<Integer>> map){
		if(map.containsKey(key)) {
			List<Integer> tempList = map.get(key);
			tempList.add(value);
			Collections.sort(tempList);
			map.put(key, tempList);
			
		}else {
		    list.add(value);
		    map.put(key, list);
		}
		return map;
	}
	
	private static void arrangePositiveNegativeValue(int[] array) {
		int z=0;
		for(int i=0;i<array.length;i++) {
			if(array[i] < 0) {
				int temp = array[i];
				array[i] = array[z];
				array[z] = temp;
				z++;
			}
		}
	}


	private static void cyclicRotation(int[] array) {
		  int size = array.length;
          for(int i=size-1;i>0;i--) {
        	  int temp = array[i];
        	  array[i]= array[i-1];
        	  array[i-1] = temp;
          }
	}


	//shift all 0 to left and 1 to right
	private static void shift01(int[] array) {
        int x = 0;
        int y = 1;
        int z = 0;
        for(int i=0;i<array.length;i++) {
        	if(array[i] == x) {
        		int temp = array[i];
        		array[i] = array[z];
        		array[z] = temp;
        		z++;
        	}
        }
	}

	private static void findFisrtSecondSmallest(int[] array) {
          int small = array[0];
          int sec_small = Integer.MAX_VALUE;
          for(int i=0;i<array.length;i++) {
        	  if(small > array[i]) {
        		  sec_small = small;
        		  small = array[i];
        	  }else if(sec_small > array[i]) {
        		  sec_small = array[i];
        	  }
          }
          
          System.out.println("SMall:"+small+"  Second Small:"+sec_small);
	}



	private static void findMejorityElement(int[] array) {
		int size = array.length/2;
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(int i=0;i<array.length;i++) {
			int temp = array[i];
			if(map.containsKey(temp)) {
				map.put(temp, map.get(temp)+1);
			}else {
				map.put(temp, 1);
			}
		}
		System.out.println(map);
		
		for(Map.Entry<Integer, Integer>  s    : map.entrySet()){
			if(s.getValue() > size) {
				System.out.println("Majority Element:"+s.getKey());
			}
		}
	}



	private static void findSumOfUniqueTriplets(int[] array, int data) {
         for(int i=0;i<array.length;i++) {
        	 for(int j=i+1;j<array.length;j++) {
        		 for(int k=j+1;k<array.length;k++) {
                	 if(array[i]+array[j]+array[k] == data) {
                		 System.out.println(array[i]+" "+array[j]+" "+array[k]);
                	 }
                 }
             }
         }
		
	}

	private static void findLongestConsecutiveSequenceLength(int[] array) {
		HashMap<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
		for(int i=0; i< array.length; i++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			int temp1 = array[i];
			list.add(temp1);
			for(int j=0;j<array.length;) {
				int temp2 = array[j];
				j++;
				if((temp1 +1) == temp2) {
					j = i+1;
					list.add(temp2);
					temp1=temp1+1;
				}
			}
			System.out.println(list);
			map.put(list.size(), list);
		}
		System.out.println(map);
	}

	private static void findNumberOfUniqueElement(int[] array) {
		int count = array.length;
		for(int i=0;i<array.length;i++) {
			for(int j=i+1;j<array.length;j++) {
				if(array[i] == array[j]) {
					System.out.println("-----------------"+array[i]);
					count--;
					i++;
				}
			}
		}
		System.out.println("Unique array lentgh:"+count);
	}

	public static void findSpecificNumber(int[] array_nums, int num1, int num2) {
		boolean flag = true;
	    for (int number : array_nums) {
	      boolean r = number != num1 && number != num2;
	      if (r) {
	         flag = false;;
	         break;
	        }
	     }
	    System.out.println("Return :"+flag);
	}
	
	
	private static void findEvenOddCount(int[] array) {
         int even = 0;
         int odd = 0;
         for(int i=0;i<array.length;i++) {
        	 if(array[i]%2 == 0) {
        		 even++;
        	 }else {
        		 odd++;
        	 }
         }
         System.out.println("Even:"+even+", Odd:"+odd);
	}


	private static void moveAllZeroAtEnd(int[] array) {
         int count = 0;
         for(int i=0; i<array.length; i++) {
        	 if(array[i] != 0) {
        		int temp = array[count];
        		array[count] =  array[i];
        		array[i] = temp;
        		count++;
        	 }
         }
	}


	//first method
	/*private static void findCommonNumber(int[] array, int[] arrayFirst, int[] arraySecond) {
            for(int i = 0;i<array.length;i++) {
            	for(int j=0; j<arrayFirst.length; j++) {
            		for(int k=0; k<arraySecond.length; k++) {
                		if(array[i]==arrayFirst[j] && arrayFirst[j]==arraySecond[k]) {
                			System.out.println("Common Element:"+array[i]);
                		}
                	}
            	}
            }
	}*/


    //second method -find common element from three sorted array
	private static void findCommonNumber(int[] array, int[] arrayFirst, int[] arraySecond) {
		int size1 = array.length;
		int size2 = arrayFirst.length;
		int size3 = arraySecond.length;
		int x=0;
		int y=0;
		int z=0;
		while(x<size1 && y<size2 && z<size3) {
			if(array[x] == arrayFirst[y] && arraySecond[z] == arrayFirst[y]) {
				System.out.println("Common:"+array[x]);
				x++;
				y++;
				z++;
			}
			else if(array[x] < arrayFirst[y]) {
				x++;
			}else if(arrayFirst[y] < arraySecond[z]) {
				y++;
			}else {
				z++;
			}
		}
	}


	private static void findMissingNumber(int[] array) {
            int total_element = 8;
            int sum =0;
            int total_sum = total_element *(total_element+1)/2;
            for(int i : array) {
            	sum =sum + i;
            }
            
            System.out.println("Missing element:"+(total_sum-sum));
	}



	private static void findSumPair(int[] array, int data) {
		  for(int i=0;i<array.length;i++) {
			  for(int j=i+1;j<array.length;j++) {
				  if(array[i]+array[j] == data){
					  System.out.println("Pair are:{"+array[i]+","+array[j]+"}");
					  break;
				  }
			  }
		  }
	}



	private static void removeDuplicate(int[] array) {
        int[] uni_array = new int[array.length];
        int count=0;
		for(int i=0;i<array.length;i++) {
			boolean flag = false;
			for(int j=i+1 ;j<array.length;j++) {
				if(array[i] == array[j]) {
					flag = true;
 				}
			}
			if(!flag) {
				uni_array[count++]=array[i];
			}
		}
		
		System.out.println("Unique array:");
		for(int i = 0;i<count;i++) {
			System.out.println(uni_array[i]);
		}
	}



	private static void findCommonString(String[] array1, String[] array2) {
         for(int i=0; i<array1.length; i++) {
        	 for(int j = 0;j<array2.length;j++) {
	        	 if(array1[i].trim().equals(array2[j].toString().trim())) {
	        		 System.out.println("Common:"+array1[i]);
	        	 }
        	 }
         }
	}



	private static void finDuplicate(int[] array) {
		boolean flag = false;
		for(int i=0;i<array.length;i++) {
			for(int j =i+1;j<array.length;j++) {
				if(array[i] == array[j]) {
					System.out.println("Duplicate value:"+array[i]);
					flag = true;
					break;
				}
			}
		}
		if(!flag) {
			System.out.println("No duplicates found!");
		}
	}



	private static void reverse(int[] array) {
        for(int i=0;i<array.length/2;i++) {
        	int temp = array[i];
        	array[i] = array[array.length-1-i];
        	array[array.length-1-i] = temp;
        }
	}



	private static void findSecondMaxMin(int[] array) {
		//for min value
		int min = Integer.MAX_VALUE;
		int Min = Integer.MAX_VALUE;
		//for max value 
		int max = Integer.MIN_VALUE;
		int Max = Integer.MIN_VALUE;
		for(int i= 0; i<array.length;i++) {
			if(array[i] > max) {
				Max = max;
				max = array[i];
			}else if(array[i] > Max) {
				Max = array[i];
			}

			if (array[i] < min) {
				Min = min;
				min = array[i];
			} else if (array[i] < Min) {
				Min = array[i];
			}
	 
		}
		System.out.println("Max:"+Max+" Min:"+Min);
	}

	private static void findMaxMin(int[] array) {
		  int max = array[0];
		  int min = array[0];
		  for(int i = 0;i<array.length;i++) {
			  if(array[i] > max) {
				  max = array[i];
			  }
			  if(array[i] < min){
				  min = array[i];
			  }
		  }
		  System.out.println("Max:"+max+" Min:"+min);
	}

	private static void deleteGivenElement(int[] array, int data) {
		for(int i=0;i<array.length;i++) {
			if(array[i] == data) {
				for(int j = i; j< array.length-1;j++) {
				      array[j] = array[j+1];
				}
			}
		}
	}

	private static void printGrid() {
		for(int i = 0;i<10;i++) {
			for(int j=0;j<10;j++) {
				System.out.print(" - ");
			}
			System.out.print(" - \n");
		}
	}

	private static void sumArrayValue(int[] array) {
		int sum = 0;
		for(int i= 0;i<array.length;i++) {
			sum+=array[i];
		}
		System.out.println("Sum="+sum);
	}

	private static void sortArray(int[] array) {
         for(int i =0;i<array.length;i++) {
        	 for(int j= i+1 ;j<array.length;j++) {
        		 if(array[i] > array[j]) {
        			 int temp = array[i];
        			 array[i] = array[j];
        			 array[j] = temp;
        		 }
        	 }
         }
	}
	
	
   
	private static void print(int[] array) {
		System.out.println("\nARRAY ELEMENTS ARE:");
		for(int i=0;i<array.length;i++) {
			System.out.print(" "+array[i]);
		}
	}
}

