//Template is created by Zhuoli Xiao, on Sept. 19st, 2016.
//Only used for Lab 226, 2016 Fall. 
//All Rights Reserved. 

//modified by Rich Little on Sept. 23, 2016

import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.util.Random;
import java.lang.Math;

public class LinearSelect {
	//Function to invoke quickSelect
	public static int QS(int[] S, int k){
        if (S.length==1)
        	return S[0];
       
        return quickSelect(0,S.length-1,S,k);


	}
	
	public static int LS(int[] S, int k){
        if (S.length==1)
        	return S[0];
       
        return linearSelect(0,S.length-1,S,k);
	}

    private static int linearSelect(int left,int right, int[] array, int k){
    	//if there is only one element now, just record.
    	if (left>=right){
    		return array[left];
    	}
    	//do the partition 
    	int p=pickCleverPivot(left,right,array);
    	int eIndex=partition(left,right,array,p);
    	//after the partition, do following ops
    	if (k<=eIndex){
    		return linearSelect(left,eIndex-1,array,k);
    	}else if(k==eIndex+1){
    		return array[eIndex];
    	}else{
    		return linearSelect(eIndex+1,right,array,k);
    	}

    }
	//prints the array from the left position to the right
	private static void printArray(int[] array,int left, int right){
		System.out.println("printing the array");
		for(int i = left;i <= right;i++){
			System.out.println("Array value at index: "+i+" is: "+array[i]);
		}
	}
	
	private static int pickCleverPivot(int left, int right, int[] arrayMaster){
		//making a copy of the array to be used in the pivot selection
		int[] array = new int[arrayMaster.length-1];
		for(int i = 0;i <array.length;i++){
			array[i] = arrayMaster[i];
		}
		//base case
		if(right - left < 5){

			for(int i = left + 1;i < right;i++){
				
				int val = array[i];
				int j = left;
				while((j >= left) && (array[j] > val)){
					
					array[j+1] = array[j];
					j--;
				}
				array[j+1] = val;
			}
			int subMedian = (int)Math.ceil((right-left )/2);
			return (left + subMedian);
		}
		
		//recursion step
		int count = left;
		for(int i = left;i <= (int)Math.ceil((right-left)/5);i=i+5){
			int sizeSubgroup = 1;
			for(int j = i + 1;j < i + 5;j++){
				if(j < array.length){
					sizeSubgroup++;
					int val = array[j];
					int k = i;
					while((k >= i) && (array[k] > val)){
						
						array[k+1] = array[k];
						k--;
					}
					array[k+1] = val;
				}
			}
			int median = (int)Math.ceil(sizeSubgroup/2);
			swap(array,median,count);
			count++;
		}
		
		int newRight = left + (int)Math.ceil((right-left)/5);
		return pickCleverPivot(left,newRight,array);
	}
	
    //do quickSelect in a recursive way 
    private static int quickSelect(int left,int right, int[] array, int k){
    	//if there is only one element now, just record.
    	if (left>=right){
    		return array[left];
    	}
    	//do the partition 
    	int p=pickRandomPivot(left,right);
    	int eIndex=partition(left,right,array,p);
    	//after the partition, do following ops
    	if (k<=eIndex){
    		return quickSelect(left,eIndex-1,array,k);
    	}else if(k==eIndex+1){
    		return array[eIndex];
    	}else{
    		return quickSelect(eIndex+1,right,array,k);
    	}

    }
    //do Partition with a pivot
    private static int partition(int left, int right, int[] array, int pIndex){
    	//move pivot to last index of the array
    	swap(array,pIndex,right);

    	int p=array[right];
    	int l=left;
    	int r=right-1;
  
    	while(l<=r){
    		while(l<=r && array[l]<=p){
    			l++;
    		}
    		while(l<=r && array[r]>=p){
    			r--;
    		}
    		if (l<r){
    			swap(array,l,r);
    		}
    	}

        swap(array,l,right);
    	return l;
    }

    //Pick a random pivot to do the QuickSelect
	private static int pickRandomPivot(int left, int right){
		int index=0;
		Random rand= new Random();
		index = left+rand.nextInt(right-left+1);
		return index;  
	}
	//swap two elements in the array
	private static void swap(int[]array, int a, int b){
 		int tmp = array[a];
		array[a] = array[b];
		array[b] = tmp;
	}


	/* main()
	   Contains code to test the QuickSelect. Nothing in this function
	   will be marked. You are free to change the provided code to test your
	   implementation, but only the contents of the QuickSelect class above
	   will be considered during marking.
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
		}
		Vector<Integer> inputVector = new Vector<Integer>();

		int v;
		while(s.hasNextInt() && (v = s.nextInt()) >= 0)
			inputVector.add(v);
		
		int k = inputVector.get(0);

		int[] array = new int[inputVector.size()-1];

		for (int i = 0; i < array.length; i++)
			array[i] = inputVector.get(i+1);

		System.out.printf("Read %d values.\n",array.length);


		long startTime = System.nanoTime();

		int kthsmallest = QS(array,k);

		long endTime = System.nanoTime();

		long totalTime = (endTime-startTime);

		System.out.printf("The %d-th smallest element in the input list of size %d is %d.\n",k,array.length,kthsmallest);
		System.out.printf("Total Time (nanoseconds) for QuickSelect: %d\n",totalTime);
		System.out.println("Running with linearSelect now...");
		long startTimeLS = System.nanoTime();

		int kthsmallestLS = LS(array,k);

		long endTimeLS = System.nanoTime();

		long totalTimeLS = (endTimeLS-startTimeLS);

		System.out.printf("The %d-th smallest element in the input list of size %d is %d.\n",k,array.length,kthsmallestLS);
		System.out.printf("Total Time (nanoseconds) for LinearSelect: %d\n",totalTimeLS);
	}
}