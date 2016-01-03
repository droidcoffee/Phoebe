package Algorithm;

/**
 * 快速排序
 * 
 * @author coffee <br>
 *         2016-1-2 下午2:37:57
 */
public class QuickSort extends BaseSort {
	
	public static void main(String[] args) {
		int[] array = { 49, 38, 65, 97, 76, 13, 27 };
		print(array);
		quickSort(array, 0, array.length - 1);
		print(array);
	}

	/**
	 * 先按照数组为数据原型写出算法，再写出扩展性算法。数组{49,38,65,97,76,13,27}
	 * 
	 * @param array
	 * @param left
	 * @param right
	 */
	public static void quickSort(int[] array, int left, int right) {
		if (left < right) {
			// 中轴线
			int pivotPointer = partitionByPivotValue(array, left, right);
			// 对左右数组递归调用快速排序，直到顺序完全正确
			quickSort(array, left, pivotPointer - 1);
			quickSort(array, pivotPointer + 1, right);
		}
	}

	/**
	 * pivotValue作为枢轴，较之小的元素排序后在其左，较之大的元素排序后在其右
	 * 
	 * @param array
	 * @param left
	 * @param right
	 * @return
	 */
	public static int partitionByPivotValue(int[] array, int left, int right) {
		int pivotValue = array[left];
		// 枢轴选定后永远不变，最终在中间，前小后大
		while (left < right) {
			while (left < right && array[right] >= pivotValue) {
				right--;
			}
			// 将比枢轴小的元素移到低端，此时right位相当于空，等待低位比pivotkey大的数补上
			array[left] = array[right];
			while (left < right && array[left] <= pivotValue) {
				left++;
			}
			// 将比枢轴大的元素移到高端，此时left位相当于空，等待高位比pivotkey小的数补上
			array[right] = array[left];
			print(array);
		}
		// 当left == right，完成一趟快速排序，此时left位相当于空，等待pivotkey补上
		array[left] = pivotValue;
		return left;
	}
}