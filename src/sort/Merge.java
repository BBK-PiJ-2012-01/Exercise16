package sort;

import java.util.LinkedList;
import java.util.List;

public class Merge {

	public static void sortInSitu(List<Integer> lst) {
		int lst_size = lst.size();
		if (lst_size <= 1)
			return;
		
		List<Integer> lst1 = lst.subList(0, lst_size/2);
		List<Integer> lst2 = lst.subList(lst_size/2+1, lst_size);
		
		sortInSitu(lst1);
		sortInSitu(lst2);
		
		//Iterator<Integer> itr1 = lst1.listIterator();
		//Iterator<Integer> itr2 = lst2.listIterator();
		
		int lst1_size = lst1.size();
		
		// NB. lst1.size() <= lst2.size()
		for (int i=0; i<lst1_size; ++i) {
			int popped = lst.remove(0);
			if (popped < lst.get(i+lst1_size))
				lst.add(i+lst1_size, popped);
			else
				lst.add(i+1+lst1_size, popped);
			
		}
		
	}
	
	public static void sort(List<Integer> lst) {
		int lst_size = lst.size();
		if (lst_size <= 1)
			return;
		
		List<Integer> lst1 = new LinkedList<Integer>(lst.subList(0, lst_size/2));
		List<Integer> lst2 = new LinkedList<Integer>(lst.subList(lst_size/2, lst_size));
		
		sort(lst1);
		sort(lst2);
		lst.clear();
		
		for (int i=0; i<lst_size; ++i) {
			if (lst1.isEmpty()) {
				lst.addAll(lst2);
				break;
			} else if (lst2.isEmpty()) {
				lst.addAll(lst1);
				break;
			}
				
			if (lst1.get(0) > lst2.get(0))
				lst.add(lst2.remove(0));
			else 
				lst.add(lst1.remove(0));
		}
				
		return;
	}

}
