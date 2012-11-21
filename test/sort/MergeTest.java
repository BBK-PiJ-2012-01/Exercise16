package sort;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class MergeTest {

	@Test
	public void test() {
		List<Integer> merge_sorted = new LinkedList<Integer>();
		List<Integer> inbuilt_sorted = new LinkedList<Integer>();
		int val;
		
		for (int i=0; i<10; ++i) {
			val = (int) Math.round(Math.random() * 100);
			merge_sorted.add(val);
			inbuilt_sorted.add(val);
		}
		
		Collections.sort(inbuilt_sorted);
		Merge.sort(merge_sorted);
		
		assertEquals(inbuilt_sorted, merge_sorted);
	}

}
