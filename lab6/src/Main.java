import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrderApp app = new OrderApp();
            app.setVisible(true);
        });
    }
}