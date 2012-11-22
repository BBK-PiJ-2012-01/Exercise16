package polynomialRoots;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class BinarySearchTest {
	private BinarySearch obj;
	
	@Before
	public void setUp() {
		obj = new BinarySearch(Arrays.asList(new Double[]{0., 1.}));
	}
	
	@Test
	public void test() {
		testRootFinder(new Double[]{0.,1.}, new Double[]{0.});
		testRootFinder(new Double[]{0.,0.,1.}, new Double[]{0.});
		testRootFinder(new Double[]{-6.,11.,-6., 1.}, new Double[]{1.,2.,3.});
	}
	
	private void testRootFinder(Double[] polynomials, Double[] expected) {
		List<Double> result = BinarySearch.findRoots(polynomials);
		System.out.format("Roots of %s are %s\n", Arrays.toString(polynomials), Arrays.toString(result.toArray()));
		assertDoubleArrayEquals(expected, result.toArray(new Double[0]));
	}

	@Test
	public void testAddXValue() {
		obj.addXValue(1);
		obj.addXValue(0);
		obj.addXValue(-1);
		
		List<Double> x_values = (List<Double>) get(obj, "x_values");
		assertDoubleArrayEquals(new Double[]{-1., 0., 1.}, x_values.toArray(new Double[0]));
		
		List<Integer> is_positive = (List<Integer>) get(obj, "is_positive");
		assertArrayEquals(new Integer[]{-1, 0, 1}, is_positive.toArray(new Integer[0]));
	}
	
	@Test
	public void testEvalLinear() {
		assertEquals(4, obj.eval(4), BinarySearch.ACCURACY);
		assertEquals(0, obj.eval(0), BinarySearch.ACCURACY);
		assertEquals(-5, obj.eval(-5), BinarySearch.ACCURACY);
	}
	
	@Test
	public void testEvalQuadratic() {
		obj = new BinarySearch(Arrays.asList(new Double[]{0., 0., 1.}));
		assertEquals(16, obj.eval(4), BinarySearch.ACCURACY);
		assertEquals(0, obj.eval(0), BinarySearch.ACCURACY);
		assertEquals(25, obj.eval(-5), BinarySearch.ACCURACY);
	}
	
	@Test
	public void testUpdateRootsFound() {
		checkUpdateRootsFound(new Integer[]{0,0,0}, 1, 0);
		
		checkUpdateRootsFound(new Integer[]{0,1,0}, 1, 2);
		checkUpdateRootsFound(new Integer[]{0,-1,0}, 1, 2);
		
		checkUpdateRootsFound(new Integer[]{1,1,1}, 1, 0);
		checkUpdateRootsFound(new Integer[]{-1,-1,-1}, 1, 0);
		
		checkUpdateRootsFound(new Integer[]{1,0,1}, 1, 1);
		checkUpdateRootsFound(new Integer[]{-1,0,-1}, 1, 1);
		
		checkUpdateRootsFound(new Integer[]{1,-1,1}, 1, 2);
		checkUpdateRootsFound(new Integer[]{-1,1,-1}, 1, 2);
		
		checkUpdateRootsFound(new Integer[]{0,-1,1}, 1, 1);
		checkUpdateRootsFound(new Integer[]{0,1,-1}, 1, 1);
		checkUpdateRootsFound(new Integer[]{-1,1,0}, 1, 1);
		checkUpdateRootsFound(new Integer[]{1,-1,0}, 1, 1);
		
		checkUpdateRootsFound(new Integer[]{0,1,1}, 1, 0);
		checkUpdateRootsFound(new Integer[]{1,1,0}, 1, 0);
		checkUpdateRootsFound(new Integer[]{0,-1,-1}, 1, 0);
		checkUpdateRootsFound(new Integer[]{-1,-1,0}, 1, 0);
		
		checkUpdateRootsFound(new Integer[]{0,0,1}, 1, 0);
		checkUpdateRootsFound(new Integer[]{1,0,0}, 1, 0);
		checkUpdateRootsFound(new Integer[]{0,0,-1}, 1, 0);
		checkUpdateRootsFound(new Integer[]{-1,0,0}, 1, 0);
		
		checkUpdateRootsFound(new Integer[]{-1,0,1}, 1, 0);
		checkUpdateRootsFound(new Integer[]{1,0,-1}, 1, 0);
		
		checkUpdateRootsFound(new Integer[]{0,0}, 0, 0);
		checkUpdateRootsFound(new Integer[]{0,0}, 1, 0);
		checkUpdateRootsFound(new Integer[]{1,1}, 1, 0);
		checkUpdateRootsFound(new Integer[]{1,1}, 0, 0);
		checkUpdateRootsFound(new Integer[]{-1,-1}, 1, 0);
		checkUpdateRootsFound(new Integer[]{-1,-1}, 0, 0);
		
		checkUpdateRootsFound(new Integer[]{0,1}, 0, 0);
		checkUpdateRootsFound(new Integer[]{0,1}, 1, 0);
		checkUpdateRootsFound(new Integer[]{1,0}, 0, 0);
		checkUpdateRootsFound(new Integer[]{1,0}, 1, 0);
		checkUpdateRootsFound(new Integer[]{0,-1}, 0, 0);
		checkUpdateRootsFound(new Integer[]{0,-1}, 1, 0);
		checkUpdateRootsFound(new Integer[]{-1,0}, 0, 0);
		checkUpdateRootsFound(new Integer[]{-1,0}, 1, 0);
		

		checkUpdateRootsFound(new Integer[]{-1,1}, 0, 1);
		checkUpdateRootsFound(new Integer[]{-1,1}, 1, 1);
		checkUpdateRootsFound(new Integer[]{1,-1}, 0, 1);
		checkUpdateRootsFound(new Integer[]{1,-1}, 1, 1);
		
		checkUpdateRootsFound(new Integer[]{1}, 0, 0);
		checkUpdateRootsFound(new Integer[]{0}, 0, 0);
		checkUpdateRootsFound(new Integer[]{-1}, 0, 0);
	}
	
	private void checkUpdateRootsFound(Integer[] positives, int added_index, int roots_added) {
		assertTrue(positives.length > 0);
		obj = new BinarySearch(Arrays.asList(new Double[]{0., 1.}));
		for (int i=0; i<positives.length; ++i) {
			obj.addXValue(0);
		}
		set(obj, "roots_found", 0);
		set(obj, "is_positive", new ArrayList<Integer>(Arrays.asList(positives)));
		obj.updateRootsFound(added_index);
		int roots = (Integer) get(obj, "roots_found");
		assertEquals(roots_added, roots);
	}
	
	private void assertDoubleArrayEquals(Double[] exp, Double[] got) {
		assertEquals(exp.length, got.length);
		
		for (int i=0; i<exp.length; ++i) {
			assertEquals(exp[i], got[i], BinarySearch.ACCURACY);
		}
	}
	
	@Test
	public void testGetPositivity() {
		assertEquals(1, obj.getPositivity(1));
		assertEquals(0, obj.getPositivity(0));
		assertEquals(-1, obj.getPositivity(-1));
	}
	
	private Object get(BinarySearch obj, String field_str) {
		Field field = null;
		try {
			field = BinarySearch.class.getDeclaredField(field_str);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail("Field not accessible");
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail("No such field");
		}
		field.setAccessible(true);
		try {
			return field.get(obj);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Field argument was bad");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Field not accessible");
		}
		return null;
	}
	
	private void set(BinarySearch obj, String field_str, Object val) {
		Field field = null;
		try {
			field = BinarySearch.class.getDeclaredField(field_str);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail("Field not accessible");
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail("No such field");
		}
		field.setAccessible(true);
		try {
			field.set(obj, val);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Field argument was bad");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Field not accessible");
		}
	}
	
	@Test
	public void testDoubleResolution() {
		obj.initialiseXValues();
		List<Double> x_values = (List<Double>) get(obj, "x_values");
		assertDoubleArrayEquals(new Double[]{-1000., 1000.}, x_values.toArray(new Double[0]));
		
		obj.doubleResolution();
		x_values = (List<Double>) get(obj, "x_values");
		assertDoubleArrayEquals(new Double[]{-1000., 0., 1000.}, x_values.toArray(new Double[0]));
		
		obj.doubleResolution();
		x_values = (List<Double>) get(obj, "x_values");
		assertDoubleArrayEquals(new Double[]{-1000., -500., 0., 500., 1000.}, x_values.toArray(new Double[0]));
		
		//List<Integer> is_positive = (List<Integer>) get(obj, "is_positive");
		//assertArrayEquals(new Integer[]{-1, 0, 1}, is_positive.toArray(new Integer[0]));
	}
	
	private void checkDifferential(Double[] polynomials, Double[] expected) {
		obj = new BinarySearch(Arrays.asList(polynomials));
		obj.setDifferential();
		List<Double> diff_polynomials = (List<Double>) get(obj, "diff_polynomials");
		assertDoubleArrayEquals(expected, diff_polynomials.toArray(new Double[0]));

	}
	
	@Test
	public void testDifferential() {
		checkDifferential(new Double[]{1.,2.}, new Double[]{2.});
		checkDifferential(new Double[]{0.,2.,4.}, new Double[]{2.,8.});
		checkDifferential(new Double[]{0.,2.,4.,5.}, new Double[]{2.,8.,15.});
	}
	
}
