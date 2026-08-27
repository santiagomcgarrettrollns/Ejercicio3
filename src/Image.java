/*
* An image is a matrix of pixels.
* Create getters and setters appropiately to get and set individual pixels.
*/
import java.awt.image.BufferedImage;

public class Image {
    private Pixel[][] pixels;

    public Image(int height, int width) {
        this.pixels = new Pixel[height][width];
    }

    public Image(Pixel[][] pixels) {
        this.pixels = pixels;
    }

    public int getHeight() {
        return this.pixels.length;
    }

    public int getWidth() {
        return this.pixels[0].length;
    }

    public Pixel getPixel(int row, int col) {
        return this.pixels[row][col];
    }

    public void setPixel(int row, int col, Pixel p) {
        this.pixels[row][col] = p;
    }

    // --- MÉTODOS AGREGADOS ---

    public Image(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        this.pixels = new Pixel[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                this.pixels[row][col] = new Pixel(bufferedImage.getRGB(col, row));
            }
        }
    }

    public BufferedImage toBufferedImage() {
        int height = getHeight();
        int width = getWidth();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (pixels[row][col] != null) {
                    bufferedImage.setRGB(col, row, pixels[row][col].toARGB());
                }
            }
        }
        return bufferedImage;
    }

    public Image copy() {
        int height = getHeight();
        int width = getWidth();
        Pixel[][] newPixels = new Pixel[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Pixel p = pixels[row][col];
                if (p != null) {
                    newPixels[row][col] = new Pixel(p.getRed(), p.getGreen(), p.getBlue(), p.getAlpha());
                }
            }
        }
        return new Image(newPixels);
    }

    public Pixel[][] getPixels() {
        return pixels;
    }
}