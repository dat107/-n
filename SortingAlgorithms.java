public class SortingAlgorithms {
	public static void bubbleSort(int[] arr, SortVisualizer v) throws InterruptedException {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
            	if (v.shouldStop()) return;
            	v.highlightIndices(j, j + 1);
            	v.incrementComparisons();
            	if (arr[j] > arr[j + 1]) {
            	    int t = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = t;
            	    v.incrementSwaps();
            	}
            	v.repaint();
            	Thread.sleep(50); //tạo hiệu ứng chậm để dễ theo dõi
            	v.waitIfPaused(); //hỗ trợ chức năng tạm dừng/từng bước
            }
        }
        v.highlightIndices(-1, -1);
    } //duyệt nhiều vòng, sau mỗi vòng pt lớn sẽ nổi vào cuối mảng

	public static void selectionSort(int[] arr, SortVisualizer v) throws InterruptedException {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < arr.length; j++) {
            	if (v.shouldStop()) return;
            	v.highlightIndices(j, minIdx);
            	v.incrementComparisons();
            	if (arr[j] < arr[minIdx]) minIdx = j; //tìm pt nhỏ nhất
            	v.repaint();
            	Thread.sleep(50);
            	v.waitIfPaused();
            }
            if (minIdx != i) {
                int t = arr[i]; arr[i] = arr[minIdx]; arr[minIdx] = t;
                v.incrementSwaps();
                v.waitIfPaused();
                v.repaint();
                Thread.sleep(50);
            }
        }
        v.highlightIndices(-1, -1);
    } //tìm pt nhỏ nhất đổi về đầu

    public static void insertionSort(int[] arr, SortVisualizer v) throws InterruptedException {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i], j = i - 1;
            while (j >= 0 && arr[j] > key) {
            	if (v.shouldStop()) return;
            	v.highlightIndices(j, j + 1);
            	v.incrementComparisons();
            	
            	arr[j + 1] = arr[j]; //dời pt lớn hơn sang phải
            	v.incrementSwaps();
            	j--;
            	
            	v.repaint();
            	Thread.sleep(50);
            	v.waitIfPaused();
            }
            if (j >= 0) v.incrementComparisons(); //ss cuối cùng ko swap
            arr[j + 1] = key; //chèn key đúng chỗ
            v.repaint();
            Thread.sleep(50);
        }
        v.highlightIndices(-1, -1);
    }//duyệt từ trái sang phải, “chèn” phần tử vào đúng vị trí trong đoạn đã sắp xếp
    
    public static void quickSort(int[] arr, int low, int high, SortVisualizer v) throws InterruptedException {
        if (low < high) {
            int pi = partition(arr, low, high, v); //chia mảng tại pivot
            
            quickSort(arr, low, pi - 1, v); //đệ quy trái
            quickSort(arr, pi + 1, high, v);//đệ quy phải
        }
        v.highlightIndices(-1, -1);
    }

    private static int partition(int[] arr, int low, int high, SortVisualizer v) throws InterruptedException {
        int pivot = arr[high]; //chọn pivot cuối
        int i = low - 1; //vị trí biên trái cho pt nhỏ hơn pivot 

        for (int j = low; j < high; j++) {
            if (v.shouldStop()) return i + 1; //trả về biên tạm thời tránh lỗi biên

            v.highlightIndices(j, i, high);
            v.incrementComparisons();
            Thread.sleep(50);
            v.waitIfPaused();

            if (arr[j] < pivot) { //ss từng pt với pivot, nếu nhỏ hơn
                i++; //mở rộng vùng trái tăng i
                if (i != j) { //đổi chỗ
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                    v.incrementSwaps();
                    v.repaint();
                    Thread.sleep(50);
                    v.waitIfPaused();
                }
            }
        }

        //đặt pivot vào đúng chỗ
        if (i + 1 != high) { 
            int temp = arr[i + 1]; arr[i + 1] = arr[high]; arr[high] = temp;
            v.incrementSwaps();
            v.repaint();
            Thread.sleep(50);
            v.waitIfPaused();
        }

        return i + 1; //trả về pivot mới
    } //chọn pivot, chia mảng thành 2 phần: nhỏ hơn pivot bên trái, lớn hơn pivot bên phải
    
    public static void mergeSort(int[] arr, int left, int right, SortVisualizer v) throws InterruptedException {
        if (left < right) {
            int mid = (left + right) / 2;
            v.highlightIndices(-1, 1, mid);

            mergeSort(arr, left, mid, v); //đệ quy trái
            mergeSort(arr, mid + 1, right, v); //đệ quy phải

            merge(arr, left, mid, right, v); //trộn
        }
        v.highlightIndices(-1, -1, -1); 
    }
    
    private static void merge(int[] arr, int left, int mid, int right, SortVisualizer v) throws InterruptedException {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(arr, left, L, 0, n1);  //copy sang mảng phụ
        System.arraycopy(arr, mid + 1, R, 0, n2); 

        int i = 0, j = 0, k = left; //i duyệt L, j duyệt R, k ghi kết quả trở lại arr từ vị trí left

        while (i < n1 && j < n2) {
        	//Khi cả hai nửa còn phần tử: kiểm tra dừng; highlight vị trí ghi k, vị trí đang xét bên trái (left+i) và ranh mid
            if (v.shouldStop()) return;

            v.highlightIndices(k, left + i, mid); 
            v.incrementComparisons();
            Thread.sleep(50);
            v.waitIfPaused();

            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;	
            }//Chọn phần tử nhỏ hơn để ghi về arr[k]

            v.incrementSwaps();
            v.repaint();
            Thread.sleep(50);
            v.waitIfPaused();

            k++;
        }

        //copy phần còn lại của L[]
        while (i < n1) {
            if (v.shouldStop()) return;
            
            v.highlightIndices(k, left + i, mid);
            arr[k++] = L[i++];
            v.incrementSwaps();
            v.repaint();
            Thread.sleep(50);
            v.waitIfPaused();
        }
        
        //copy phần còn lại của R[]
        while (j < n2) {
            if (v.shouldStop()) return;
            
            v.highlightIndices(k, mid + 1 + j, mid);
            arr[k++] = R[j++];
            v.incrementSwaps();
            v.repaint();
            Thread.sleep(50);
            v.waitIfPaused();
        }
    }//chia mảng thành các mảng con, sau đó trộn chúng lại theo thứ tự.
}