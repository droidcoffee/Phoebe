package Algorithm;

/**
 * 冒泡排序
 * 
 * @author coffee <br>
 *         2016-1-2 下午2:37:19
 */
public class BubbleSort extends BaseSort {

	public static void main(String[] args) {
		int[] array = new int[] { 7, 9, 3, 6, 2, 4 };
		print(array);
		sort(array);
	}

	public static void sort(int[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			//
			for (int j = 0; j < array.length - i - 1; j++) {
				if (array[j] > array[j + 1]) {
					swap(array, j, j + 1);
				}
			}
			print(array);
		}
	}
}
