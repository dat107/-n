import javax.swing.*;
import java.awt.*;

public class ControlButtonsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
	private JLabel infoLabel;

    public ControlButtonsPanel(SortVisualizer visualizer) {
        setLayout(new BorderLayout());

        // Panel chứa các nút
        JPanel buttonsRow = new JPanel(new FlowLayout());
        JButton start = new JButton("Bắt đầu");
        JButton pause = new JButton("Tạm dừng");
        JButton step = new JButton("Tiếp bước");
        JButton reset = new JButton("Reset");
        JButton compareAll = new JButton("So sánh tất cả");

        buttonsRow.add(start);
        buttonsRow.add(pause);
        buttonsRow.add(step);
        buttonsRow.add(reset);
        buttonsRow.add(compareAll);

        // Label thông tin (so sánh, hoán đổi, thời gian)
        infoLabel = new JLabel("Số so sánh: 0   Số hoán đổi: 0   Thời gian: 0.0s", JLabel.CENTER);
        visualizer.setInfoLabel(infoLabel); // truyền label để SortVisualizer cập nhật

        add(buttonsRow, BorderLayout.NORTH);
        add(infoLabel, BorderLayout.SOUTH);
        
        // Gán sự kiện
        start.addActionListener(e -> visualizer.startSorting());
        pause.addActionListener(e -> visualizer.pause());
        step.addActionListener(e -> visualizer.step());
        reset.addActionListener(e -> visualizer.reset());
        
        compareAll.addActionListener(e -> {
            if (visualizer.getArray() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập hoặc tạo dãy số trước.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int[] inputArray = visualizer.getOriginalArrayBackup();
            if (inputArray == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy mảng gốc để so sánh. Vui lòng tạo hoặc nhập lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Chạy so sánh trong luồng riêng
            new Thread(() -> {
                SortingComparator comparator = new SortingComparator(inputArray.clone());
                comparator.compareAll();
            }).start();
        });
    }
}
