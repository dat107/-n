public class DummyVisualizer extends SortVisualizer {
	private static final long serialVersionUID = 1L;

	@Override
    public void repaint() {
        // Không cần vẽ gì cả
    }

    @Override
    public void waitIfPaused() {
        // Không cần tạm dừng trong so sánh
    }

    @Override
    public boolean shouldStop() {
        return false;
    }
}
