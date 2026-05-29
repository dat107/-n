import javax.swing.*;

public class SortingComparator {

    private final int[] originalArray;

    public SortingComparator(int[] array) {
        this.originalArray = array.clone(); // đảm bảo không thay đổi mảng gốc
    }

    public void compareAll() {
        String[] algorithms = {
            "Bubble Sort", "Selection Sort", "Insertion Sort", "Quick Sort", "Merge Sort"
        };

        StringBuilder result = new StringBuilder("Kết quả so sánh các thuật toán:\n\n");

        for (String algo : algorithms) {
            int[] arrCopy = originalArray.clone();
            //mỗi thuật toán tạo ra bản sao của mảng gốc, bảo đảm thuật toán sx dữ liệu giống nhau

            // Tạo visualizer tạm để theo dõi số liệu (không hiển thị giao diện)
            DummyVisualizer dummy = new DummyVisualizer();
            dummy.setArray(arrCopy);
            dummy.setCurrentAlgorithm(algo);

            //đo tg chạy
            long startTime = System.currentTimeMillis();
            try {
                switch (algo) {
                    case "Bubble Sort" -> SortingAlgorithms.bubbleSort(arrCopy, dummy);
                    case "Selection Sort" -> SortingAlgorithms.selectionSort(arrCopy, dummy);
                    case "Insertion Sort" -> SortingAlgorithms.insertionSort(arrCopy, dummy);
                    case "Quick Sort" -> SortingAlgorithms.quickSort(arrCopy, 0, arrCopy.length - 1, dummy);
                    case "Merge Sort" -> SortingAlgorithms.mergeSort(arrCopy, 0, arrCopy.length - 1, dummy);
                }
            } catch (InterruptedException e) { //là do các hàm sắp xếp hỗ trợ tạm dừng/bước đơn, nên phải try-catch
                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();

            result.append(algo).append(":\n")
                  .append(" - So sánh: ").append(dummy.getComparisons()).append("\n")
                  .append(" - Hoán đổi: ").append(dummy.getSwaps()).append("\n")
                  .append(" - Thời gian: ").append(endTime - startTime).append(" ms\n\n");
        }

        // Hiển thị kết quả
        JOptionPane.showMessageDialog(null, result.toString(), "So sánh thuật toán", JOptionPane.INFORMATION_MESSAGE);
    }
}
