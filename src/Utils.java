
public class Utils {

	public static int find(int[] arr, int elem) {
		for (int i=0; i<arr.length;i++) {
			if (elem == arr[i]) {
				return i; 
			}
		}
		return -1;
	}
	
	public static<T> int find(T[] arr, T target) {
		for (int i = 0; i < arr.length; i++) {
			if (target.equals(arr[i])) {
				return i;
			}
		}
		return -1;
	}
	
	
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
