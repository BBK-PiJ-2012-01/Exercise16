package polynomialRoots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BinarySearch {
	public static final double ACCURACY = 1e-6;
	private static final double MIN_X = -1000.;
	private static final double MAX_X = +1000.;
	private static final double MIN_RES = 0.5;
	private static final double SWITCH_APPROACH_RES = 10.;
	
	private int roots_found = 0;
	private double resolution;
	private List<Double> polynomials;
	private List<Double> diff_polynomials = new ArrayList<Double>();
	private List<Integer> is_positive = new LinkedList<Integer>();;
	private List<Double> x_values = new LinkedList<Double>();
	
	public static List<Double> findRoots(Double[] polynomials) {
		return findRoots(new ArrayList<Double>(Arrays.asList(polynomials)));
	}
	
	public static List<Double> findRoots(List<Double> polynomials) {
		BinarySearch obj = new BinarySearch(polynomials);
		obj.increaseResolutionToFindRoots();
		List<Double> roots = new LinkedList<Double>();
		for (Double[] window : obj.getWindows()) {
			roots.add(obj.findRoot(window));
		}
		return roots;
	}
	
	public Double findRoot(Double[] window, int ...positives) {
		if (positives.length == 0)
			positives = new int[]{getPositivity(eval(window[0])), getPositivity(eval(window[1]))};
		double this_x = (window[0] + window[1])/2;
		int this_pos = getPositivity(eval(this_x));
		if (this_pos == 0)
			return this_x;
		
		for (int i=0; i<2; ++i) {
			if (this_pos != positives[i]) 
				return findRoot(new Double[]{this_x, window[i]}, 
						new int[]{this_pos, positives[i]});
		}
		
		System.out.format("window = %s\n", Arrays.toString(window));
		System.out.format("positives = %s\n", Arrays.toString(positives));
		for (int i=0; i<2; ++i ) {
			System.out.format("eval(%f) = %f\n", window[i], eval(window[i])); 
		}
		throw new RuntimeException("Couldn't find root in window...");
	}
	
	public List<Double[]> getWindows() {
		//List<List<Double>> windows = new LinkedList<List<Double>>();
		List<Double[]> windows = new LinkedList<Double[]>();
		int prev_pos = is_positive.get(0);
		int this_pos;
		double prev_x = x_values.get(0);
		double this_x;
		boolean found0 = false;
		/*
		 * Ignore starting or ending 0.
		 * 10*(-1) => root between x_values giving 1 and -1
		 * -10*1 => root between x_values giving 1 and -1
		 * 100*1 => root between x_values giving 1 and 1
		 */
		for (int i=1; i<x_values.size(); ++i) {
			this_pos = is_positive.get(i);
			
			if (prev_pos != 0) {
				if (this_pos == 0) {
					found0 = true;
				} else {
					this_x = x_values.get(i);
					if (this_pos != prev_pos || found0) {
						//windows.add(new ArrayList<Double>(Arrays.asList(new Double[]{})))
						windows.add(new Double[]{prev_x, this_x});
					}
					prev_pos = this_pos;
					prev_x = this_x;
					found0 = false;
				}
			}
		}
		return windows;
	}
	
	public BinarySearch(List<Double> polynomials) {
		this.polynomials = polynomials;
	}
	
	public void initialiseXValues() {
		x_values.clear();
		is_positive.clear();
		roots_found = 0;
		
		addXValue(MIN_X);
		addXValue(MAX_X);
		resolution = MAX_X - MIN_X;
	}
	
	public void increaseResolutionToFindRoots() {
		initialiseXValues();
		
		int roots_to_find = polynomials.size() - 1;
		while (roots_found < roots_to_find && resolution >= MIN_RES ) {
			doubleResolution();
		}
	}
	
	public void addXValue(double x) {
		int i=0;
		Iterator<Double> itr = x_values.listIterator();
		while (i<x_values.size()) {
			if (itr.next() > x) {
				break;
			}
			++i;
		}
		x_values.add(i, x);
		int new_pos = getPositivity(eval(x));
		is_positive.add(i, new_pos);
		
		updateRootsFound(i);
	}
	
	public void updateRootsFound(int i) {
		// Try again... 
		// 101 => +1
		// 1(-1)1 => +2
		// 000 => +0
		// 010 => +2
		// 001 => +0
		// 011 => +0
		// 0(-1)1 => +1
		// 1(0)(-1) => +0
		
		// (0) => +0
		
		// (0)0 => +0
		// (1)0 or 1(0) => +0
		// (1)(-1) => +1
		int x_total = x_values.size();
		
		if (i-1 >= 0 && i+1 < x_total) {
			// If there are x_values either-side of 'x'
			int a = is_positive.get(i-1);
			int b = is_positive.get(i);
			int c = is_positive.get(i+1);
			
			//System.out.format("Reading [%d,%d,%d]\n", a, b, c);
			
			if ((a==1 && c==1) || (a==-1 && c==-1)) {
				// 1b1, (-1)b(-1)
				if (b == 0)
					// 101 or (-1)0(-1) => +1
					roots_found += 1;
				else if (b != a)
					// 1(-1)1 or (-1)1(-1) => +2
					roots_found += 2;
			} else if (a==0 && c==0) {
				// 0b0
				if (b==1 || b==-1) {
					// 0(-1)0 or 010 => +2
					roots_found += 2;
				}
			} else if (a==0 ^ c==0) {
				// 0b1 or 0b(-1) or 1b0 or (-1)b0
				if (a+b+c == 0)
					// 0(-1)1 etc... => +1
					roots_found += 1;
			}	
		} else { 
			int a, b;
			if (i-1 >= 0) {
				// If there are only x_values lower than 'x'
				a = is_positive.get(i-1);
				b = is_positive.get(i);
			} else if(i+1 < x_total) {
				// If there are only x_values higher than 'x'
				a = is_positive.get(i);
				b = is_positive.get(i+1);
			} else {
				// If there are no other x_values than 'x'
				return;
			}
			
			if ((a==1 && b==-1) || (a==-1 && b==1))
				// 1(-1) or (-1)1
				roots_found += 1;
		}
	}
	
	public void doubleResolution() {
		double old_resolution = resolution;
		resolution = resolution / 2;
		double new_x_offset = x_values.get(0) + resolution;
		int number_to_add = x_values.size()-1;
		for (int i=0; i<number_to_add; ++i) {
			addXValue(new_x_offset + old_resolution*i);
		}
	}
	
	public int getPositivity(double val) {
		return val > ACCURACY? 1 : (val < -ACCURACY? -1 : 0);
	}
	
	public double eval(double x) {
		return eval(x, polynomials);
	}
	
	public void setDifferential() {
		// Start at 1 because d(const)/dx = 0
		for (int i=1; i<polynomials.size(); ++i) {
			// d(ax^i)/dx = aix^(i-1)
			diff_polynomials.add(polynomials.get(i) * i);
		}
	}
	
	public double diff_eval(double x) {
		return eval(x, diff_polynomials);
	}
	
	private double eval(double x, List<Double> given_polynomials) {
		double result = 0;
		for (int i=0; i<given_polynomials.size(); ++i) {
			result += given_polynomials.get(i)*Math.pow(x, (double) i);
		}
		return result;
	}
}
