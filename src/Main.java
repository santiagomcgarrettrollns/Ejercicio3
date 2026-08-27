import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Inicialización de FlatLaf (debe ir antes de crear cualquier componente GUI)
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("No se pudo cargar el Look and Feel FlatLaf: " + e.getMessage());
        }

        // Ejecución de la interfaz Swing en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            ImageEditorModel model = new ImageEditorModel();
            ImageEditorView view = new ImageEditorView();
            new ImageEditorController(model, view);

            view.setVisible(true);
        });
    }
}