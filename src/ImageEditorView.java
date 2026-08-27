import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageEditorView extends JFrame {
    JPanel mainPanel = new JPanel();
    JButton loadImageButton = new JButton("Load Image");
    JButton negativeFilterButton = new JButton("Negative");
    JButton grayscaleFilterButton = new JButton("Grayscale");
    
    // --- NUEVOS COMPONENTES AGREGADOS ---
    JButton binarizeFilterButton = new JButton("Binarize");
    JButton saveImageButton = new JButton("Save Image");
    JButton undoButton = new JButton("Undo");
    JButton resetButton = new JButton("Reset");

    JLabel historyCountLabel = new JLabel("Operaciones: 0");
    JTextArea historyTextArea = new JTextArea();

    JFileChooser inputImageChooser = new JFileChooser();
    JFileChooser saveImageChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "png", "jpeg", "jpg");
    
    ImagePanel inputImagePanel;
    ImagePanel outputImagePanel;

    public ImageEditorView() {
        // We are extending the JFrame class, so we MUST call the parent constructor.
        super("Editor UVG");

        // orientation of main panel
        mainPanel.setLayout(new BorderLayout(10, 10));

        // methods on the parent JFrame class
        setSize(1000, 650);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputImageChooser.setFileFilter(filter);
        saveImageChooser.setFileFilter(filter);

        // Panel Superior de Controles
        JPanel controlPanel = new JPanel();
        controlPanel.add(loadImageButton);
        controlPanel.add(negativeFilterButton);
        controlPanel.add(grayscaleFilterButton);
        controlPanel.add(binarizeFilterButton);
        controlPanel.add(undoButton);
        controlPanel.add(resetButton);
        controlPanel.add(saveImageButton);

        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // Panel Central de Imágenes (Original vs Resultado)
        JPanel imagesContainer = new JPanel(new GridLayout(1, 2, 10, 10));
        inputImagePanel = new ImagePanel(null);
        outputImagePanel = new ImagePanel(null);
        
        inputImagePanel.setBorder(BorderFactory.createTitledBorder("Original"));
        outputImagePanel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        
        imagesContainer.add(inputImagePanel);
        imagesContainer.add(outputImagePanel);

        mainPanel.add(imagesContainer, BorderLayout.CENTER);

        // Panel Lateral de Historial
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setPreferredSize(new Dimension(200, 0));
        historyPanel.setBorder(BorderFactory.createTitledBorder("Historial"));

        historyTextArea.setEditable(false);
        historyPanel.add(historyCountLabel, BorderLayout.NORTH);
        historyPanel.add(new JScrollPane(historyTextArea), BorderLayout.CENTER);

        mainPanel.add(historyPanel, BorderLayout.EAST);

        // at last, add the main panel to the JFrame
        add(mainPanel);
    }

    // ################## A section to register action listeners ################
    public void addLoadImageListener(ActionListener listener) {
        loadImageButton.addActionListener(listener);
    }

    public void addNegativeListener(ActionListener listener) {
        negativeFilterButton.addActionListener(listener);
    }

    public void addInputImageChooserListener(ActionListener listener) {
        inputImageChooser.addActionListener(listener);
    }

    // Listeners agregados
    public void addGrayscaleListener(ActionListener listener) {
        grayscaleFilterButton.addActionListener(listener);
    }

    public void addBinarizeListener(ActionListener listener) {
        binarizeFilterButton.addActionListener(listener);
    }

    public void addSaveImageListener(ActionListener listener) {
        saveImageButton.addActionListener(listener);
    }

    public void addUndoListener(ActionListener listener) {
        undoButton.addActionListener(listener);
    }

    public void addResetListener(ActionListener listener) {
        resetButton.addActionListener(listener);
    }

    // ############### A section to trigger actions in the GUI ##################
    public File showInputImageChooser() {
        int returnVal = inputImageChooser.showOpenDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        return inputImageChooser.getSelectedFile();
    }

    public File showSaveImageChooser() {
        int returnVal = saveImageChooser.showSaveDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        return saveImageChooser.getSelectedFile();
    }

    public void showInputImage(BufferedImage image) {
        inputImagePanel.setImage(image);
        revalidate();
        repaint();
    }

    public void showOutputImage(BufferedImage image) {
        outputImagePanel.setImage(image);
        revalidate();
        repaint();
    }

    public void updateHistory(List<String> actions, int size) {
        historyCountLabel.setText("Operaciones: " + size);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < actions.size(); i++) {
            sb.append(i + 1).append(". ").append(actions.get(i)).append("\n");
        }
        historyTextArea.setText(sb.toString());
    }

    public void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}