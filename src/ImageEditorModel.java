import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageEditorModel {
    private String inputFileName;
    private Image inputImage;
    private Image outputImage;
    private ImageEditor editor;

    // Historial dinámico usando List y ArrayList
    private List<Image> historyImages;
    private List<String> historyActions;

    public ImageEditorModel() {
        this.historyImages = new ArrayList<>();
        this.historyActions = new ArrayList<>();
    }

    public String getInputFileName() {
        return this.inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public Image getInputImage() {
        return this.inputImage;
    }

    public void setInputImage(Image inputImage) {
        this.inputImage = inputImage;
        this.outputImage = inputImage.copy();
        this.editor = new ImageEditor(this.outputImage);
        this.historyImages.clear();
        this.historyActions.clear();
    }

    public Image getOutputImage() {
        return this.outputImage;
    }

    public Image getOriginalImage() {
        return this.inputImage;
    }

    // Filtro negativo mantenido con manejo de excepción
    public Image negativeFilter() throws NoImageLoadedException {
        return applyFilter("Negative");
    }

    // Aplicación genérica de filtros con historial
    public Image applyFilter(String filterType) throws NoImageLoadedException {
        if (this.outputImage == null) {
            throw new NoImageLoadedException("Debe cargar una imagen antes de aplicar un filtro.");
        }

        // Guardar estado previo en el historial
        historyImages.add(outputImage.copy());
        historyActions.add(filterType);

        this.editor = new ImageEditor(this.outputImage);

        switch (filterType) {
            case "Negative":
                this.outputImage = this.editor.negative();
                break;
            case "Grayscale":
                this.outputImage = this.editor.grayscale();
                break;
            case "Binarize":
                this.outputImage = this.editor.blackAndWhite(128);
                break;
        }

        return this.outputImage;
    }

    // Deshacer última operación
    public Image undo() throws EmptyHistoryException {
        if (historyImages.isEmpty()) {
            throw new EmptyHistoryException("No hay operaciones previas en el historial para deshacer.");
        }

        int lastIndex = historyImages.size() - 1;
        this.outputImage = historyImages.remove(lastIndex);
        this.historyActions.remove(lastIndex);
        this.editor = new ImageEditor(this.outputImage);

        return this.outputImage;
    }

    // Reiniciar imagen al estado original
    public Image reset() throws NoImageLoadedException {
        if (inputImage == null) {
            throw new NoImageLoadedException("No hay ninguna imagen cargada para reiniciar.");
        }

        this.outputImage = inputImage.copy();
        this.editor = new ImageEditor(this.outputImage);
        this.historyImages.clear();
        this.historyActions.clear();

        return this.outputImage;
    }

    // Guardar imagen resultante
    public void saveImage(File file) throws NoImageLoadedException, IOException {
        if (outputImage == null) {
            throw new NoImageLoadedException("No hay ninguna imagen para guardar.");
        }
        ImageUtils.save(outputImage, file.getAbsolutePath());
    }

    public List<String> getHistoryActions() {
        return new ArrayList<>(historyActions);
    }

    public int getHistorySize() {
        return historyImages.size();
    }
}