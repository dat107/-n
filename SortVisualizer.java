import javax.swing.*;
import java.awt.*;

public class SortVisualizer extends JPanel {
    private static final long serialVersionUID = 1L;
	private int[] array;
    private int[] originalArray;
    private int[] originalArrayBackup;
    private JLabel infoLabel; 
    private boolean isRunning = false;
    private boolean paused = false;
    private boolean stepMode = false;
    private final Object lock = new Object();
    private String currentAlgorithm = "Bubble Sort";
    private int comparisons = 0;
    private int swaps = 0;
    private long startTime = 0;
    private int highlightedIndex1 = -1;
    private int highlightedIndex2 = -1;
    private int specialIndex = -1;
    private volatile boolean shouldStop = false;

    public void highlightIndices(int i1, int i2, int special) {
        this.highlightedIndex1 = i1;
        this.highlightedIndex2 = i2;
        this.specialIndex = special;
        repaint();
    }
    
    public void highlightIndices(int i1, int i2) {
        highlightIndices(i1, i2, -1);
    }

    public void setArray(int[] array) {
        this.array = array;
        this.originalArray = array.clone();
        this.originalArrayBackup = array.clone();
        comparisons = 0;
        swaps = 0;
        repaint();
    }
    
    public void setOriginalArrayBackup(int[] arr) {
        this.originalArrayBackup = arr;
    }

    public int[] getOriginalArrayBackup() {
        return originalArrayBackup;
    }


    public void setInfoLabel(JLabel label) {
        this.infoLabel = label;
    }
    
    public void setCurrentAlgorithm(String algorithm) {
        this.currentAlgorithm = algorithm;
    }

    public void startSorting() {
        if (isRunning) {
            resume();
            return;
        }
        isRunning = true;
        shouldStop = false;
        new Thread(() -> { //tạo luồng mới chạy thuật toán
            try {
                startTime = System.currentTimeMillis();
                switch (currentAlgorithm) {
                    case "Bubble Sort" -> SortingAlgorithms.bubbleSort(array, this);
                    case "Selection Sort" -> SortingAlgorithms.selectionSort(array, this);
                    case "Insertion Sort" -> SortingAlgorithms.insertionSort(array, this);
                    case "Quick Sort" -> SortingAlgorithms.quickSort(array, 0, array.length - 1, this);
                    case "Merge Sort" -> SortingAlgorithms.mergeSort(array, 0, array.length - 1, this);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                isRunning = false;
            }
        }).start();
    }

    public void waitIfPaused() throws InterruptedException {
        synchronized (lock) {
            while (paused && !stepMode) lock.wait(); //nếu paused == true thì chờ 
            if (stepMode) stepMode = false; //nếu stepmode bật -> chạy 1 bước rồi reset stepmode
        }
    }

    public void pause() {
        paused = true;
        updateInfoLabel();
    }

    public void resume() {
        synchronized (lock) {
            paused = false;
            lock.notifyAll(); //để đánh thức luồng đang đợi trong waitIfPause
            updateInfoLabel();
        }
    }

    public void step() {
        synchronized (lock) {
            stepMode = true;
            lock.notifyAll(); //luồng thực thi sẽ tiếp tục một bước sau đó thì dừng
        }
    }

    public void reset() {
        shouldStop = true; //ngắt thuật toán đang chạy
        paused = false;
        stepMode = false;
        isRunning = false;
        synchronized (lock) {
            lock.notifyAll(); 
        }

        if (originalArray != null) {
            array = originalArray.clone(); //sao chép lại mảng lúc đầu
            comparisons = 0;
            swaps = 0;
            highlightedIndex1 = -1;
            highlightedIndex2 = -1;
            repaint();
            updateInfoLabel();
        }
    }

    public boolean shouldStop() {
        return shouldStop;
    }

    public void incrementComparisons() {
        comparisons++;
        updateInfoLabel();
    }

    public void incrementSwaps() {
        swaps++;
        updateInfoLabel();
    }

    private void updateInfoLabel() {
        if (infoLabel != null && array != null) {
            infoLabel.setText("Số so sánh: " + comparisons +
                              "   Số hoán đổi: " + swaps +
                              "   Thời gian: " + ((System.currentTimeMillis() - startTime) / 1000.0) + "s");
        }
    }
    
    public int[] getArray() {
        return array;
    }
    public int getComparisons() {
        return comparisons;
    }
    public int getSwaps() {
        return swaps;
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (array == null) return;

        int w = getWidth();
        int h = getHeight() - 40;
        int barWidth = Math.max(w / array.length, 5);

        for (int i = 0; i < array.length; i++) {
        	int barHeight = array[i]*4;
        	if (i == specialIndex) {
        	    g.setColor(Color.BLUE); // màu cho pivot hoặc đặc biệt
        	} else if (i == highlightedIndex1 || i == highlightedIndex2) {
        	    g.setColor(Color.GREEN); // so sánh
        	} else {
        	    g.setColor(Color.RED); // mặc định
        	}

            g.fillRect(i * barWidth, h - barHeight, barWidth - 2, barHeight);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(array[i]), i * barWidth + 2, h - barHeight - 5);
        }
    }
}