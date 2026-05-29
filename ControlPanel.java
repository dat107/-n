import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class ControlPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public ControlPanel(SortVisualizer visualizer) {
        setLayout(new GridLayout(2, 1));

        JComboBox<String> algorithmBox = new JComboBox<>(new String[]{
            "Bubble Sort", "Selection Sort", "Insertion Sort", "Quick Sort", "Merge Sort"
        });
        JTextField sizeField = new JTextField("20", 3);
        JTextField inputField = new JTextField(20);
        JButton uploadButton = new JButton("Nhập từ file");
        JButton loadButton = new JButton("Tải dãy");
        JButton randomButton = new JButton("Tạo dãy ngẫu nhiên");

        JPanel topRow = new JPanel();
        topRow.add(new JLabel("Thuật toán:"));
        topRow.add(algorithmBox);
        topRow.add(new JLabel("Số Lượng: "));
        topRow.add(sizeField);
        topRow.add(randomButton);

        JPanel bottomRow = new JPanel();
        bottomRow.add(new JLabel("Nhập dãy:"));
        bottomRow.add(inputField);
        bottomRow.add(uploadButton);
        bottomRow.add(loadButton);

        add(topRow);
        add(bottomRow);

        randomButton.addActionListener(e -> {
            try {
                int size = Integer.parseInt(sizeField.getText().trim()); //lấy sl từ sizefield
                if (size <= 0 || size > 20) throw new NumberFormatException();

                int[] array = new Random().ints(size, 10, 100).toArray(); //sinh mảng radom trong khoảng 10->100
                visualizer.setArray(array);  // Gán vào mô phỏng
                visualizer.setOriginalArrayBackup(array.clone());

                // Hiển thị lại lên ô nhập
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < array.length; i++) {
                    sb.append(array[i]);
                    if (i != array.length - 1) sb.append(", ");
                }
                inputField.setText(sb.toString());
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng hợp lệ (1–20)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
     
        loadButton.addActionListener(e -> {
        	try {
        	    String[] nums = inputField.getText().split(","); //cắt chuỗi inputfield bằng dấu ,
        	    int[] array = new int[nums.length]; //chuyển thành mảng int
        	    for (int i = 0; i < nums.length; i++) {
        	        array[i] = Integer.parseInt(nums[i].trim());
        	    }
        	    visualizer.setArray(array);
        	    visualizer.setOriginalArrayBackup(array.clone());
        	} catch (NumberFormatException ex) {
        	    JOptionPane.showMessageDialog(this, "Vui lòng nhập dãy số hợp lệ, ngăn cách bằng dấu phẩy.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        	}
        });
        
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fileChooser.getSelectedFile();
                    Scanner scanner = new Scanner(file);
                    if (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] nums = line.split(",");
                        int[] array = new int[nums.length]; //cắt các số tạo thành mảng int
                        for (int i = 0; i < nums.length; i++) {
                            array[i] = Integer.parseInt(nums[i].trim());
                        }
                        inputField.setText(line); // Hiển thị lại lên TextField
                        visualizer.setArray(array);
                        visualizer.setOriginalArrayBackup(array.clone());
                    }
                    scanner.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi đọc file hoặc định dạng sai!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        algorithmBox.addActionListener(e -> {
            visualizer.setCurrentAlgorithm((String) algorithmBox.getSelectedItem()); 
        });
    }
}