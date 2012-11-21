package sort;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.sun.net.httpserver.Authenticator.Success;

public class QuicksortTest {

	@Test
	public void testInSitu() {
		List<Integer> quick_sorted_insitu = new LinkedList<Integer>();
		List<Integer> quick_sorted_exsitu = new LinkedList<Integer>();
		List<Integer> inbuilt_sorted = new LinkedList<Integer>();
		int val;
		
		for (int i=0; i<1000; ++i) {
			val = (int) Math.round(Math.random() * 1000);
			quick_sorted_insitu.add(val);
			inbuilt_sorted.add(val);
		}
		
		Collections.sort(inbuilt_sorted);
		Quicksort.sort(quick_sorted_insitu, true);
		Quicksort.sort(quick_sorted_exsitu, false);
		
		assertEquals(inbuilt_sorted, quick_sorted_insitu);
		//assertEquals(inbuilt_sorted, quick_sorted_exsitu);
	}
	
	@Test
	public void testPartitionLargeInSitu() {
		List<Integer> lst = new LinkedList<Integer>();
		
		for (int i=0; i<100; ++i) {
			lst.add((int) Math.round(Math.random() * 1000));
		}
		
		for (int i=0; i<100; ++i) {
			testRandomPartition(lst, i);
		}
		
	}
	
	private void testRandomPartition(List<Integer> lst, int pivot_index) {
		lst = new LinkedList<Integer>(lst); 
		int pivot_val = lst.get(pivot_index);
		int lst_size = lst.size();
		int new_pivot_index = Quicksort.partitionInSitu(lst, 0, lst_size-1, pivot_index);
		
		// List must be same size
		assertEquals(lst_size, lst.size());
		
		// Pivot value should be at the new pivot index
		assertEquals(pivot_val, (int) lst.get(new_pivot_index));
		
		for (int val : lst.subList(0, new_pivot_index)) {
			if (val > pivot_val) {
				System.out.format("Bad number (%d)\n",val);
				System.out.format("Pivot value (%d) at index %d\n",pivot_val, new_pivot_index);
				System.out.format("Old pivot index %d\n",pivot_index);
				System.out.println(Arrays.toString(lst.toArray()));
				fail("Found large number in lower partition");
			}
		}
		
		for (int val : lst.subList(new_pivot_index, lst.size())) {
			if (val < pivot_val) {
				System.out.format("Bad number (%d)\n",val);
				System.out.format("Pivot value (%d) at index %d\n",pivot_val, new_pivot_index);
				System.out.format("Old pivot index %d\n",pivot_index);
				System.out.println(Arrays.toString(lst.toArray()));
				fail("Found small number in higher partition");
			}
		}
	}
	
	@Test
	public void testPartitionSmallInSitu() {
		List<Integer> lst = new LinkedList<Integer>(Arrays.asList(new Integer[]{3,1}));
		Quicksort.partitionInSitu(lst, 0, 1, 1);
		assertArrayEquals(new Integer[]{1,3}, lst.toArray(new Integer[0]));
		
		lst = new LinkedList<Integer>(Arrays.asList(new Integer[]{3,1}));
		Quicksort.partitionInSitu(lst, 0, 1, 0);
		assertArrayEquals(new Integer[]{1,3}, lst.toArray(new Integer[0]));
	}
	
	@Test
	public void testSwap() {
		List<Integer> lst = new LinkedList<Integer>(Arrays.asList(new Integer[]{3,2,1}));
		Quicksort.swap(lst, 0, 2);
		assertEquals(new LinkedList<Integer>(Arrays.asList(new Integer[]{1,2,3})), lst);
		Quicksort.swap(lst, 0, 1);
		assertEquals(new LinkedList<Integer>(Arrays.asList(new Integer[]{2,1,3})), lst);
		Quicksort.swap(lst, 1, 2);
		assertEquals(new LinkedList<Integer>(Arrays.asList(new Integer[]{2,3,1})), lst);
	}
}
