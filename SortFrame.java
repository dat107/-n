import javax.swing.*;
import java.awt.*;

public class SortFrame extends JFrame {
    private static final long serialVersionUID = 1L;

	public SortFrame() {
        setTitle("Mô phỏng giải thuật sắp xếp");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        SortVisualizer visualizer = new SortVisualizer();
        ControlPanel controlPanel = new ControlPanel(visualizer);
        ControlButtonsPanel buttonsPanel = new ControlButtonsPanel(visualizer);

        add(controlPanel, BorderLayout.NORTH);   
        add(visualizer, BorderLayout.CENTER);    
        add(buttonsPanel, BorderLayout.SOUTH);   
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { 
        	//đảm bảo rằng việc tạo giao diện chạy trên Event Dispatch Thread (EDT) của Swing → tránh lỗi thread trong GUI
            new SortFrame().setVisible(true); //tạo một cửa sổ SortFrame và hiển thị nó
        });
    }
}
