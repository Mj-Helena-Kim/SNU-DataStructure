import java.io.*;
import java.util.*;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static void swap(int[] value, int i, int j){
		int n = value[i];
		value[i] = value[j];
		value[j] = n;
	}


	private static int[] DoBubbleSort(int[] value){
		for(int i = value.length - 1; i >= 0; i--)
			for(int j = 0; j < i; j++)
				if(value[j] > value[j + 1])
					swap(value, j, j + 1);

		return value;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value){
		int insertVal = 0;
		for(int i = 1; i < value.length; i++){
			insertVal = value[i];
			int j = i - 1;
			for( ; j >= 0; j--){
				if(insertVal >= value[j])	break;
				value[j+1] = value[j];
			}
			value[j + 1] = insertVal;
		}
		return value;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] value){
		buildHeap(value);
		for(int i = value.length-1; i >= 0; i--){
			value[i]  = deleteMax(value, i);
		}
		return value;
	}

	private static void buildHeap(int A[]){
		for(int i = (A.length-2)/2; i >= 0; i--)
			percolateDown(A, i, A.length);
	}

	private static void percolateDown(int A[], int k, int n){
		int leftChild = 2*k+1;
		int rightChild = 2*k+2;

		if(leftChild < n){
			if(rightChild < n && A[leftChild] < A[rightChild])	leftChild = rightChild;
			if(A[k] < A[leftChild]){
				swap(A, k, leftChild);
				percolateDown(A, leftChild, n);
			}
		}
	}
	
	private static int deleteMax(int A[], int i){
		int max = A[0];
		swap(A, 0, i);
		percolateDown(A, 0, i);
		return max;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoMergeSort(int[] value){
		mergeSort(value, 0, value.length - 1);
		return value;
	}

	private static void mergeSort(int A[], int p, int r){
		if(p < r){
			int q = (p + r)/2;
			mergeSort(A, p, q);
			mergeSort(A, q+1, r);
			merge(A, p, q, r);
		}
	}

	private static void merge(int A[], int p, int q, int r){
		int[] tmp = new int[r - p + 1];
		int i = p;
		int j = q + 1;
		int t = 0;
		while((i <= q) && (j <= r)){
			if(A[i] < A[j])	tmp[t++] = A[i++];
			else	tmp[t++] = A[j++];
		}
		for(;i <= q;i++)	tmp[t++] = A[i]; //if left side is left
		for(;j <= r; j++)	tmp[t++] = A[j]; //if right side is left
		for(int k = p; k <= r; k++)	A[k] = tmp[k-p]; //copy tmp to A
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] value){
		quickSort(value, 0, value.length - 1);
		return value;
	}

	private static void quickSort(int[] A, int p, int r){

		if(p < r){
			int q = partition(A, p, r);
			quickSort(A, p, q-1);
			quickSort(A, q+1, r);
		}
	}

	private static int partition(int[] A, int p, int r){
		int pivot = A[r];
		int i = p;
		for(int j = p; j < r; j++){
			if(A[j] < pivot)
				swap(A, i++, j);
		}
		swap(A, i, r);

		return i;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSort(int[] value){
		int maxVal = 0;
		int digit = 1;

		for(int i = 0; i < value.length; i++)
			if(maxVal < Math.abs(value[i]))	maxVal = Math.abs(value[i]);
		while(maxVal >= 10){
			maxVal /= 10;
			digit++;
		}

		for(int i = 1; i <= digit; i++)
			countingSort(value, i); //counting sort on kth digit

		return value;
	}

	private static void countingSort(int[] A, int k){
		int[] tmp = new int[20];
		for(int i = 0; i < 20; i++)
			tmp[i] = 0;
		int[] cloneA = A.clone();
		int toSort = 0;
		long d = (long)Math.pow(10, k - 1);

		for(int i = 0; i < A.length; i++){
			toSort = (int)(A[i]/d) % 10 + 10;
			tmp[toSort]++;
		}

		for(int i = 1; i < 20; i++)
			tmp[i] += tmp[i - 1];

		for(int i = A.length - 1; i  >= 0; i--){
			toSort = (int)(cloneA[i]/d) % 10 + 10;
			A[tmp[toSort]-1] = cloneA[i];
			tmp[toSort]--;
		}
	}
}