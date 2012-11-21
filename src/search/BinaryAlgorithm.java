package search;

import java.util.List;

public class BinaryAlgorithm {

	public static boolean contains(List<Integer> lst, Integer val) {
		int lst_size = lst.size();
		if (lst_size == 0)
			return false;
		int middle_element = lst.get(lst_size/2);
		
		if (middle_element == val)
			return true;
		
		if (middle_element > val) {
			lst = lst.subList(0, lst_size/2);
		} else {
			lst = lst.subList(lst_size/2+1, lst_size);
		}
		return contains(lst, val); 
	}

}
