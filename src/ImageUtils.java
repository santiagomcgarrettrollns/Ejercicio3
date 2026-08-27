import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtils {
    public static Image load(String filename) throws InvalidImageFormatException, IOException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new InvalidImageFormatException("El archivo no existe: " + filename);
        }

        try {
            BufferedImage img = ImageIO.read(file);
            if (img == null) {
                throw new InvalidImageFormatException("El archivo no es un formato de imagen válido: " + filename);
            }

            int height = img.getHeight();
            int width = img.getWidth();

            // Create pixel matrix here with appropiate dimensions.
            Pixel[][] pixels = new Pixel[height][width];

            // loop over BufferedImage
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int packed = img.getRGB(col, row);
                    int r = (packed >> 16) & 0xFF;
                    int g = (packed >> 8) & 0xFF;
                    int b = packed & 0xFF;
                    pixels[row][col] = new Pixel(r, g, b);
                }
            }

            return new Image(pixels);
        } finally {
            // Requerimiento: bloque finally ejecutado independientemente del resultado
            System.out.println("Proceso de lectura de imagen finalizado para: " + filename);
        }
    }

    public static void save(Image image, String filename) throws IOException {
        BufferedImage img = toBufferedImage(image);

        File file = new File(filename);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        String format = "png";
        int dot = filename.lastIndexOf('.');
        if (dot != -1 && dot < filename.length() - 1) {
            format = filename.substring(dot + 1).toLowerCase();
        }

        try {
            ImageIO.write(img, format, file);
        } finally {
            // Requerimiento: bloque finally para cierre o limpieza post-escritura
            System.out.println("Proceso de guardado de imagen finalizado en: " + filename);
        }
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image == null) return null;
        
        int height = image.getHeight();
        int width = image.getWidth();

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Pixel pixel = image.getPixel(row, col);

                int r = pixel.r & 0xFF;
                int g = pixel.g & 0xFF;
                int b = pixel.b & 0xFF;
                output.setRGB(col, row, (r << 16) | (g << 8) | b);
            }
        }

        return output;
    }
}