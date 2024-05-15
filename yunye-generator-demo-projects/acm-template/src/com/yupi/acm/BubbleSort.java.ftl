/**
* 冒泡 输入模板（多数之和）
* @author ${bubbleSort.author!''}
*/
public class BubbleSort {
public static void bubbleSort(int[] arr) {
int n = arr.length;
boolean swapped;
for (int i = 0; i < n - 1; i++) {
swapped = false;
for (int j = 0; j < n - i - 1; j++) {
if (arr[j] > arr[j + 1]) {
// 交换 arr[j+1] 和 arr[j]
int temp = arr[j];
arr[j] = arr[j + 1];
arr[j + 1] = temp;
swapped = true;
}
}
//  {${bubbleSort.description!''}}
if (!swapped)
break;
}
}

public static void main(String[] args) {
int[] arr = {${bubbleSort.number!''}};
bubbleSort(arr);
System.out.println("Sorted array");
for (int i = 0; i < arr.length; i++) {
System.out.print(arr[i] + " ");
}
}
}
