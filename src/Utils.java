
public class Utils {

	public static int find(int[] list, int elem) {
		for (int i=0; i<list.length;i++) {
			if (elem == list[i]) {
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
	
}
