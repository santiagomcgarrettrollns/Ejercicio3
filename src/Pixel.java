/*
* The Pixel class represents an RGB pixel.
* We use `int` as the data type to back up every
* color channel.
*/
public class Pixel {
    public int r;
    public int g;
    public int b;
    public int a = 255; // Canal alpha por defecto (opaco)

    public Pixel(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    // --- MÉTODOS Y CONSTRUCTORES AGREGADOS ---

    public Pixel(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Pixel(int argb) {
        this.a = (argb >> 24) & 0xFF;
        this.r = (argb >> 16) & 0xFF;
        this.g = (argb >> 8) & 0xFF;
        this.b = argb & 0xFF;
    }

    public int toARGB() {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public int getRed() { return r; }
    public int getGreen() { return g; }
    public int getBlue() { return b; }
    public int getAlpha() { return a; }
}