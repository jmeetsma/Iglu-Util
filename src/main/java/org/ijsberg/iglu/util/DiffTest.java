package org.ijsberg.iglu.util;

import java.util.ArrayList;
import java.util.List;

import org.ijsberg.iglu.util.collection.ArraySupport;

public class DiffTest {
	
	static String str_1 = "Hallo, is daxr de melkboer!";
	static String str_2 = "xHallo, we daar is de melkboer, nogmaals!x";
	
//	static String str_1 = "AXA!x";
//	static String str_2 = "AXA bleh!";
	
	
	Object[] baseArray;
	Object[] otherArray;
	

	int baseArrayIndex = 0;
	int alteredArrayIndex = 0;

	
	boolean finished = false;
	
	Integer[] arrayCopy(char[] array) {
		Integer[] retval = new Integer[array.length];
		for(int i = 0; i < array.length; i++) {
			retval[i] = new Integer(array[i]);
			
		}
//		System.out.println(ArraySupport.format(retval, ","));
		return retval;
	}
	
	public DiffTest(char[] array1, char[] array2) {
		super();
		baseArray = arrayCopy(array1);		
		otherArray = arrayCopy(array2);
	}



	public void determineDiff() {
		
		int[][] matrix = createLcsMatrix(baseArray, otherArray);
//		printMatrix(matrix);
		
		List<DiffItemRecord> result = new ArrayList<DiffItemRecord>(100);
		stepThroughMatrix(result, matrix, baseArray, otherArray, baseArray.length - 1, otherArray.length - 1);
				
		System.out.println(result);
		
	}
	
	public void printMatrix(int[][] matrix) {
		for(int i = 0; i < matrix.length; i++) {
			System.out.println(ArraySupport.format(matrix[i], ","));
		}
	}
	
	
	int[][] createLcsMatrix(Object[] orgininal, Object[] current) {
		int[][] retval = new int[orgininal.length + 1][current.length + 1];
		
		for(int i = 0; i < orgininal.length; i++) {
			for(int j = 0; j < current.length; j++) {
				if(orgininal[i].hashCode() == current[j].hashCode()) {
					retval[i + 1][j + 1] = retval[i][j] + 1;
				} else {
					retval[i + 1][j + 1] = max(retval[i + 1][j], retval[i][j + 1]);
				}
			}
		}
		return retval;
	}

		
	private int max(int i, int j) {
		return i > j ? i : j;
	}

	
	//construct a list containing the path
	void stepThroughMatrix(List<DiffItemRecord> result, int[][] matrix, Object[] original, Object[] current, int i, int j) {
		
		if(i == 0 || j == 0) {
			result.add(0, new DiffItemRecord(i, j, original[i].hashCode()));
			if(i > 0) {
				addTrailingOriginalItems(result, original, i);
			} else if(j > 0) {
				addTrailingCurrentItems(result, current, j);
			}
		} else if (original[i].hashCode() == current[j].hashCode()) {
			addCorrespondingItemsAndProceed(result, matrix, original, current,
					i, j);
		} else {
			addMissingItemsAndProceed(result, matrix, original, current, i, j);
		}
	}

	private void addCorrespondingItemsAndProceed(List<DiffItemRecord> result,
			int[][] matrix, Object[] original, Object[] current, int i, int j) {
		result.add(0, new DiffItemRecord(i, j, original[i].hashCode()));
		stepThroughMatrix(result, matrix, original, current, i - 1, j - 1);
	}

	private void addMissingItemsAndProceed(List<DiffItemRecord> result,
			int[][] matrix, Object[] original, Object[] current, int i, int j) {
		if(matrix[i + 1][j] >= matrix[i][j + 1]) {
			result.add(0, new DiffItemRecord(MISSING_INDEX, j, current[j].hashCode()));
			stepThroughMatrix(result, matrix, original, current, i, j - 1);
		} else {
			result.add(0, new DiffItemRecord(i, MISSING_INDEX, original[i].hashCode()));
			stepThroughMatrix(result, matrix, original, current, i - 1, j);
		}
	}

	private void addTrailingCurrentItems(List<DiffItemRecord> result,
			Object[] current, int j) {
		for(int k = j - 1; k >= 0; k--) {
			result.add(0, new DiffItemRecord(MISSING_INDEX, j, current[k].hashCode()));
		}
	}

	private void addTrailingOriginalItems(List<DiffItemRecord> result,
			Object[] original, int i) {
		for(int k = i - 1; k >= 0; k--) {
			result.add(0, new DiffItemRecord(i, MISSING_INDEX, original[k].hashCode()));
		}
	}
	
	

	public static void main(String[] args) {
		
		new DiffTest(str_1.toCharArray(), str_2.toCharArray()).determineDiff();
		
//		System.out.println(new Integer(10).hashCode());
//		System.out.println(new Character('a').hashCode());
		
	}

	private static final int MISSING_INDEX = -1;

	
	public class DiffItemRecord {
		//-1 indicates nonexistence
		private int originalIndex;
		private int currentIndex;
		private int value;
		
		
		public DiffItemRecord(int originalIndex, int currentIndex, int value) {
			super();
			this.originalIndex = originalIndex;
			this.currentIndex = currentIndex;
			this.value = value;
		}

		public boolean isAdded() {
			return originalIndex == -1;
		}
		
		public boolean isRemoved() {
			return currentIndex == -1;
		}

		public boolean isCorresponding() {
			return originalIndex != MISSING_INDEX && currentIndex != MISSING_INDEX;
		}

		public int getValue() {
			return value;
		}

		public int getOriginalIndex() {
			return originalIndex;
		}

		public int getCurrentIndex() {
			return currentIndex;
		}
		
		public String toString() {
			return (isCorresponding() ? "=" : (isAdded() ? "+" : "-")) + (char)value;
		}
	}
	
	
	public class DiffSubsequenceRecord {
		
		
		/*
		 added / deleted / corresponding 
		 
		 startItemTecord - endItemRecord
		 
		 */
		
		
	}
	//DiffBlockRecord
	
	//DiffReport
	
	public class DiffReport {
		
		private List<DiffItemRecord> itemRecords;
		
		public DiffReport(List<DiffItemRecord> itemRecords) {
			this.itemRecords = itemRecords;
			
			
			
			
			//create blocks
		}
		
		
		
		
		
	}

}
