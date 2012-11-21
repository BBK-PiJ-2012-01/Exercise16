package search;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class BinaryAlgorithmTest {
	
	private List<Integer> getList(Integer[] arr) {
		return new LinkedList<Integer>(Arrays.asList(arr));
	}

	@Test
	public void testEmptyList() {
		List<Integer> lst = getList(new Integer[]{});
		assertFalse(BinaryAlgorithm.contains(lst, 4));
	}
	
	@Test
	public void testSingleList() {
		List<Integer> lst = getList(new Integer[]{1});
		
		assertFalse(BinaryAlgorithm.contains(lst, 0));
		assertTrue(BinaryAlgorithm.contains(lst, 1));
	}
	
	@Test
	public void testLongList() {
		List<Integer> lst = getList(new Integer[]{1,2,3,4,5,6,7,8,9,10});
		
		assertFalse(BinaryAlgorithm.contains(lst, 11));
		assertTrue(BinaryAlgorithm.contains(lst, 3));
	}
	
	@Test
	public void testLongRandomList() {
		List<Integer> lst = new LinkedList<Integer>();
		int val = 0;
		
		for (int i=0; i<1000; ++i) {
			val = (int) Math.round(1000*Math.random());
			lst.add(val);
		}
		Collections.sort(lst);
		
		assertFalse(BinaryAlgorithm.contains(lst, 1001));
		assertTrue(BinaryAlgorithm.contains(lst, val));
	}

}

