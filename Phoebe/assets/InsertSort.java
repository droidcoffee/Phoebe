package Algorithm;

/**
 * 插入排序
 * 
 * @author coffee <br>
 *         2016-1-3 下午11:51:20
 */
public class InsertSort extends BaseSort {

	public static void main(String[] args) {
		int[] array = new int[] { 8, 7, 4, 6, 9, 1, 2 };
		print(array);
		insertSort(array);
	}

	public static void insertSort(int[] array) {
		// 要插入的元素
		int insert = 0;
		int j = 0;
		for (int i = 1; i < array.length; i++) {
			insert = array[i];
			// 从i的前一个元素往前遍历
			j = i - 1;
			while (j >= 0 && insert < array[j]) {
				swap(array, j + 1, j);
				j--;
			}
			print(array);
		}
	}
}
