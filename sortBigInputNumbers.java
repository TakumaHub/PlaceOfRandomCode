/*  Solution to Hackerrank challenge, Day 2 of Week 29. Easy Difficulty.
    Big Sorting: Sorting input numbers far bigger than longs.
    
    Usage:  First input number is the amount of numbers to sort.
            Subsequent input numbers are the numbers to sort.
    Output: Sorted numbers printed, one on each line.
*/

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class sortBigInputNumbers{

    
    static class sortLength implements Comparator<String> {
        public int compare(String s1, String s2){
            return s1.length() - s2.length();
        }
    }
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String[] unsorted = new String[n];
        for(int unsorted_i=0; unsorted_i < n; unsorted_i++){
            unsorted[unsorted_i] = in.next();
        }
        
        // pass 1, sort by grouping with size of string
        Arrays.sort(unsorted, new sortLength());  

        // pass 2, sort by leading number for each group of same-length strings.
        // Worst case O(nlogn) * O(n) ?
        // If all string lengths different, O(1) * O(n), as no sorting
        // If all string lengths the same, O(nlogn) * 1 + n, as sort once + add to list gives the n
        // If mix of string lengths, O(aloga * (n-a))? which is basically O(nlogn * n)
        // Get length of first string, then add subsequent strings into new String array until longer string reached. 
        List <String> sorted = new ArrayList<String>();
        int i = 0;  // this value has a max of n, which is under 2x10^5
        int start = 0;
        while(i < n){
            List <String> myList = new ArrayList<String>();
            while(i<n && unsorted[i].length() == unsorted[start].length()){
                myList.add(unsorted[i]);    // add all same-length strings to myList.
                i++;
            }
            //out of above loop if length of next string is greater.
            //if(myList.length > 1)     // attempt to make it skip sort for 1 element in list.
            Collections.sort(myList);   // sort the list of same-length strings
            sorted.addAll(myList);      // add this list to our sorted array
            start = i;  // new comparison
        }

        // print
        for (String num: sorted){
            System.out.println(num);
        }
        
    }
}
