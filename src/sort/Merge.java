package sort;

import java.util.List;

public class Merge {

	public static void sort(List<Integer> lst) {
		int lst_size = lst.size();
		if (lst_size <= 1)
			return;
		
		sort(lst.subList(0, lst_size/2));
		sort(lst.subList(lst_size/2+1, lst_size));
	}

}
