
public class Utils {

	/**
	 * Returns the index of an element in an array
	 * @param arr
	 * @param elem
	 * @return found index or -1
	 */
	public static<T> int find(T[] arr, T elem) {
		for (int i = 0; i < arr.length; i++) {
			if (elem.equals(arr[i])) {
				return i;
			}
		}
		return -1;
	}
	
	public static int find(int[] arr, int elem) {
		for (int i=0; i<arr.length;i++) {
			if (elem == arr[i]) {
				return i; 
			}
		}
		return -1;
	}
	
	/**
	 * Counts the number of elements in an array != null
	 * @param arr
	 * @return
	 */
	public static <T> int countElements(T[] arr){
	    int count = 0;
	    for(T elem : arr)
	        if (elem != null)
	            ++count;
	    return count;
	}
	
	public static int countElements(double[] arr){
	    int count = 0;
	    for(double elem : arr)
	        if (elem != 0.0)
	            ++count;
	    return count;
	}
	
}
