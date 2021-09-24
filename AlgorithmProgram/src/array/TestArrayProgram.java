package array;
import java.io.*;
import java.util.*;

class TestArrayProgram {
	static int findLongestConseqSubseq(int arr[], int n)
	{
		HashSet<Integer> S = new HashSet<Integer>();
		int ans = 0;
		for (int i = 0; i < n; ++i)
			S.add(arr[i]);
		
		System.out.println(S);
		for (int i = 0; i < n; ++i)
		{
			System.out.println("i="+i);
			if (!S.contains(arr[i] - 1))
			{
				int j = arr[i];
				System.out.println("j="+j);
				while (S.contains(j))
					j++;
				if (ans < j - arr[i])
					ans = j - arr[i];
			}
		}
		return ans;
	}
	
	public static void main(String args[])
	{
		int arr[] = { 1, 9, 3, 10, 4, 20, 2 };
		int n = arr.length;
		System.out.println(
			"Length of the Longest consecutive subsequence is "
			+ findLongestConseqSubseq(arr, n));
	}
}
