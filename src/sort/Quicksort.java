package sort;

import java.util.Iterator;
import java.util.List;

public class Quicksort {

	public static void sort(List<Integer> lst, boolean inSitu) {
		if (inSitu) {
			quicksortInSitu(lst, 0, lst.size()-1);
		} else {
			
		}
	}
	
	public static int partitionInSitu(List<Integer> lst, int first, int last, int pivot_index) {
		
		int pivot = lst.get(pivot_index);
		
		// Swap pivot with last value
		swap(lst, last, pivot_index);
		
		int new_pivot_index = first;
		
		Iterator<Integer> itr = lst.subList(first, last).listIterator();
		// Iterate through list excluding last value (ie. the pivot)
		for (int i=first; i<last; ++i) {
			// If value is less than pivot, swap with item at pivot_index,
			// then increment pivot_index
			if (itr.next() < pivot) {
				swap(lst, new_pivot_index, i);
				++new_pivot_index;
			}
		}
		
		// Finally, put the pivot back
		swap(lst, new_pivot_index, last);
		
		// And return the pivot index
		return new_pivot_index;
	}
	
	public static void swap(List<Integer> lst, int i, int j) {
		int temp = lst.get(i);
		lst.set(i, lst.get(j));
		lst.set(j, temp);
	}
	
	private static void quicksortInSitu(List<Integer> lst, int first, int last) {
		// Only act if there are at least 2 elements
		if (first < last) {
			//System.out.format("acting on list: %d to %d\n", first, last);
			// Pick pivot index as middle value
			int pivot_index = (last-first)/2 + first;
			//int pivot_index = first;
			
			// Partition the list
			pivot_index = partitionInSitu(lst, first, last, pivot_index);
			
			// quicksort the smaller elements
			quicksortInSitu(lst, first, pivot_index-1);
			
			// quicksort the larger elements
			quicksortInSitu(lst, pivot_index+1, last);
			//System.out.println(Arrays.toString(lst.subList(first, last+1).toArray()));
			
		} 
	}

}
