import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

class Image {
    File file = null;
    BufferedImage image;
    int width;
    int height;

    public Image(String path) {
        try {
            file = new File(path);
            image = ImageIO.read(file);
        } catch (Exception e) {
            System.out.println("Error");
        }
        // size of image
        width = image.getWidth();
        height = image.getHeight();
        System.out.println(width + "*" + height);
    }
}

class GrayScale extends Image {
    GrayScale(String path, String outputFileName) {
        super(path);
        System.out.println(height);
        // grayscale Conversion
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                // System.out.print(" " + p + " ");
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = (p & 0xff);

                int avg = (int) Math.sqrt((r * r) + (g * g) + (b * b));
                // int avg = (r + g + b) / 3;
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                image.setRGB(x, y, p);
            }
        }
        // output
        try {
            file = new File(outputFileName);
            ImageIO.write(image, "jpg", file);
        } catch (Exception e) {
            System.out.print("Error Occoured");
        }
    }
}

// addetive noise
class Noise {
    File file;
    BufferedImage image;
    String outputFileName;
    int height;
    int width;

    Noise(BufferedImage image, String outputFileName) {
        this.outputFileName = outputFileName;
        this.image = image;
        height = image.getHeight();
        width = image.getWidth();
    }

    public void addetiveNoise(double percentage) {
        percentage = ((height * width) / 100.0) * percentage;
        int x = 0, y = 0;
        // System.out.println(percentage);
        for (int i = 0; i < (int) percentage; i++) {
            x = randFunction(width - 1);
            y = randFunction(height - 1);
            // System.out.println(x + " " + y);
            int p = image.getRGB(x, y);
            // System.out.print(" " + p + " ");
            int a = (p >> 24) & 0xff;

            p = (a << 24) | (randFunction(255) << 16) | (randFunction(255) << 8) |
                    randFunction(255);

            image.setRGB(x, y, p);
        }
        try {
            file = new File(outputFileName);
            ImageIO.write(image, "jpg", file);
        } catch (Exception e) {
            System.out.print("Error Occoured");
        }
    }

    public int randFunction(int val) {
        int max = val;
        int min = 0;
        double range = (max - min) + 1;
        // System.out.print("Range " + range + " ");
        int r = 0;
        while (true) {
            r = (int) (Math.random() * range) + 0;
            if (r <= val) {
                break;
            }
        }
        return (int) r;
    }
}
// noise end

// addetive Filter start

class ImageWithFilter {
    File file;
    BufferedImage image1;
    BufferedImage image2;
    String outputFileName;

    ImageWithFilter(BufferedImage obj) {
        this.image1 = obj;
        this.image2 = obj;
        // System.out.print("new image: " + image1.getHeight() + "," +
        // image1.getWidth());
    }

    // mean filter start
    public void meanFilter(String outputFileName) {
        // int p = 0;
        for (int y = 0; y < this.image1.getHeight() - 3; y++) {
            for (int x = 0; x < this.image1.getWidth() - 3; x++) {
                // p = this.image1.getRGB(x, y);
                matrixCal(y, x);
                // break;
            }
        }
        try {
            file = new File(outputFileName);
            ImageIO.write(this.image1, "jpg", file);
        } catch (Exception e) {
            System.out.print("Error Occoured");
        }
    }

    public void matrixCal(int y, int x) {
        // int a, r, g, b, p, cal;
        // a = r = g = b = p = cal = 0;
        int cal, rgb_color, p, a;
        p = a = cal = rgb_color = 0;

        for (int i = y; i < y + 3; i++) {
            for (int j = x; j < x + 3; j++) {
                p = image1.getRGB(x, y);
                rgb_color = (p >> 16) & 0xff;
                // System.out.print(" " + x + ", " + y + " ");
                a = (p >> 24) & 0xff;
                // r = (p >> 16) & 0xff;
                // g = (p >> 8) & 0xff;
                // b = (p & 0xff);
                // cal = r + g + b + cal;
                cal = rgb_color + cal;
                // break;
            }

        }
        cal = cal / 9;
        p = (a << 24) | (cal << 16) | (cal << 8) | cal;
        image1.setRGB(x, y, p);
    }
    // mean filter end

    // median filter start

    public void medianFilter(String outputFileName) {
        // int p = 0;
        for (int y = 0; y < this.image2.getHeight() - 3; y++) {
            for (int x = 0; x < this.image2.getWidth() - 3; x++) {
                // p = this.image2.getRGB(x, y);
                matrixCalmedianFilter(y, x);
            }
        }
        try {
            file = new File(outputFileName);
            ImageIO.write(this.image2, "jpg", file);
        } catch (Exception e) {
            System.out.print("Error Occoured");
        }
    }

    public void matrixCalmedianFilter(int y, int x) {
        int rgb_color, p, a;
        p = a = rgb_color = 0;
        int[][] arr = new int[9][2];
        for (int i = y, m = 0; i < y + 3; i++) {
            for (int j = x; j < x + 3; j++, m++) {
                p = image2.getRGB(x, y);
                // rgb_color = (p >> 16) & 0xff;
                // System.out.print(" " + x + ", " + y + " ");
                arr[m][0] = rgb_color; // read color
                arr[m][1] = (p >> 24) & 0xff; // a color
                // break;
            }

        }

        // array sort
        int temp = 0;
        for (int i = 0; i < 9 - 1; i++) {
            for (int j = i; j < 9 - 1; j++) {
                if (arr[i][0] > arr[j + 1][0]) {
                    temp = arr[i][0];
                    arr[i][0] = arr[j + 1][0];
                    arr[j + 1][0] = temp;

                    temp = arr[i][1];
                    arr[i][1] = arr[j + 1][1];
                    arr[j + 1][1] = temp;
                }
            }
        }

        // array sort end

        int median = arr[5][0];
        a = arr[5][1];
        p = (a << 24) | (median << 16) | (median << 8) | median;
        image2.setRGB(x + 1, y + 1, p);
    }

}

public class Main {
    public static void main(String args[]) throws IOException {
        String path = "./flower.jpg"; // image path
        String outputFileName = "grayScale.jpg";// output file name with extension
        GrayScale gray_obj = new GrayScale(path, outputFileName);

        // add noise
        outputFileName = "addednoise.jpg";
        Noise noise_obj = new Noise(gray_obj.image, outputFileName);
        double percentage = 25;
        noise_obj.addetiveNoise(percentage);

        // filter using
        outputFileName = "meanFilter.jpg";//output of meanfilter
        ImageWithFilter obj = new ImageWithFilter(noise_obj.image);
        obj.meanFilter(outputFileName);
        outputFileName = "medianFilter.jpg";
        obj.medianFilter(outputFileName);
    }
}
