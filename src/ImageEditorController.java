import java.io.File;
import javax.swing.JOptionPane;

public class ImageEditorController {
    private ImageEditorView view;
    private ImageEditorModel model;

    public ImageEditorController(ImageEditorModel model, ImageEditorView view) {
        this.view = view;
        this.model = model;

        // hookup action listeners
        this.view.addLoadImageListener(e -> handleLoadImage());
        this.view.addNegativeListener(e -> handleNegativeFilter());

        // --- LISTENERS AGREGADOS PARA CUMPLIR CON MVC Y EXCEPCIONES ---
        this.view.addGrayscaleListener(e -> handleFilter("Grayscale"));
        this.view.addBinarizeListener(e -> handleFilter("Binarize"));
        this.view.addSaveImageListener(e -> handleSaveImage());
        this.view.addUndoListener(e -> handleUndo());
        this.view.addResetListener(e -> handleReset());
    }

    public void handleLoadImage() {
        File selectedFile = view.showInputImageChooser();
        if (selectedFile == null) {
            return;
        }

        try {
            // mutate the application state
            model.setInputFileName(selectedFile.getAbsolutePath());
            model.setInputImage(ImageUtils.load(selectedFile.getAbsolutePath()));
        } catch (InvalidImageFormatException e) {
            view.showErrorDialog(e.getMessage());
        } catch (Exception e) {
            view.showErrorDialog("couldn't load image: " + e.getMessage());
        }

        // we updated the state of the model, we must re-draw the view layer
        refresh();
    }

    // What do we want to do when someone presses the
    // negative filter button?
    private void handleNegativeFilter() {
        try {
            Image negative = this.model.negativeFilter();
            refresh();
        } catch (NoImageLoadedException e) {
            view.showErrorDialog(e.getMessage());
        }
    }

    // --- MÉTODOS DE MANEJO AGREGADOS ---

    private void handleFilter(String filterType) {
        try {
            this.model.applyFilter(filterType);
            refresh();
        } catch (NoImageLoadedException e) {
            view.showErrorDialog(e.getMessage());
        }
    }

    private void handleUndo() {
        try {
            this.model.undo();
            refresh();
        } catch (EmptyHistoryException e) {
            view.showErrorDialog(e.getMessage());
        }
    }

    private void handleReset() {
        try {
            this.model.reset();
            refresh();
        } catch (NoImageLoadedException e) {
            view.showErrorDialog(e.getMessage());
        }
    }

    private void handleSaveImage() {
        File destination = view.showSaveImageChooser();
        if (destination == null) {
            return;
        }

        try {
            this.model.saveImage(destination);
            JOptionPane.showMessageDialog(null, "Imagen guardada correctamente.");
        } catch (NoImageLoadedException e) {
            view.showErrorDialog(e.getMessage());
        } catch (Exception e) {
            view.showErrorDialog("Error al guardar la imagen: " + e.getMessage());
        }
    }

    // call the view to re-draw the application state
    private void refresh() {
        if (model.getInputImage() != null) {
            view.showInputImage(ImageUtils.toBufferedImage(model.getOriginalImage()));
        }
        if (model.getOutputImage() != null) {
            view.showOutputImage(ImageUtils.toBufferedImage(model.getOutputImage()));
        }
        view.updateHistory(model.getHistoryActions(), model.getHistorySize());
    }
}