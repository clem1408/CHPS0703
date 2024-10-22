import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Arrays;

public class Main {

    public static BufferedImage newImage;
    public static BufferedImage newImage2;
    public static BufferedImage newImage3;
    public static BufferedImage image;

    public static int grayFromPixel(int pixel) {
        return (pixel >> 16) & 0xff;
    }

    public static int[] historigramme(String nom_fichier) {
        int[] tab = new int[256];

        for (int i = 0; i < tab.length; i++) {
            tab[i] = 0;
        }

        // Creation historigramme
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                // Récupère la couleur du pixel (valeur en niveaux de gris)
                int pixel = image.getRGB(x, y);

                int gray = grayFromPixel(pixel);
                tab[gray]++;
            }
        }

        return tab;
    }

    public static int[] historigrammeCumule(String nom_fichier) {
        int[] tab = new int[256];

        for (int i = 0; i < tab.length; i++) {
            tab[i] = 0;
        }

        int compteur = 0;

        // Creation historigramme
        for (int k = 0; k < 256; k++) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    // Récupère la couleur du pixel (valeur en niveaux de gris)
                    int pixel = image.getRGB(x, y);

                    int gray = grayFromPixel(pixel);

                    if (gray == k) {
                        compteur++;
                    }
                }
            }
            tab[k] = compteur;
        }

        return tab;
    }

    public static void binarisation() {
        try {
            // Charge l'image en niveaux de gris depuis un fichier
            File imageFile = new File("saturne.png");
            image = ImageIO.read(imageFile);

            // Recupere les dimensions de l'image
            int width = image.getWidth();
            int height = image.getHeight();

            // Créer une nouvelle image avec les mêmes dimensions
            newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Récupère la couleur du pixel (valeur en niveaux de gris)
                    int pixel = image.getRGB(x, y);

                    int gray = grayFromPixel(pixel);

                    if (gray > 127) {
                        gray = 0;
                    } else if (gray <= 127) {
                        gray = 255;
                    }
                    int newPixel = (255 << 24) | (gray << 16) | (gray << 8) | gray;

                    newImage.setRGB(x, y, newPixel);
                }
            }

            File outputFile = new File("saturne_binarisation.png");
            ImageIO.write(newImage, "png", outputFile);

            System.out.println("Nouvelle image modifiée créée avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    public static void negatif() {
        try {
            // Charge l'image en niveaux de gris depuis un fichier
            File imageFile = new File("saturne.png");
            image = ImageIO.read(imageFile);

            // Recupere les dimensions de l'image
            int width = image.getWidth();
            int height = image.getHeight();

            // Créer une nouvelle image avec les mêmes dimensions
            newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Récupère la couleur du pixel (valeur en niveaux de gris)
                    int pixel = image.getRGB(x, y);

                    int gray = grayFromPixel(pixel);

                    gray = 255 - gray;

                    int newPixel = (255 << 24) | (gray << 16) | (gray << 8) | gray;

                    newImage.setRGB(x, y, newPixel);
                }
            }

            File outputFile = new File("saturne_negatif.png");
            ImageIO.write(newImage, "png", outputFile);

            System.out.println("Nouvelle image modifiée créée avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    public static void quantification() {
        try {
            // Charge l'image en niveaux de gris depuis un fichier
            File imageFile = new File("saturne.png");
            image = ImageIO.read(imageFile);

            // Recupere les dimensions de l'image
            int width = image.getWidth();
            int height = image.getHeight();

            // Créer une nouvelle image avec les mêmes dimensions
            newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Récupère la couleur du pixel (valeur en niveaux de gris)
                    int pixel = image.getRGB(x, y);

                    int gray = grayFromPixel(pixel);

                    gray = ((int) gray / 64) * 64;

                    int newPixel = (255 << 24) | (gray << 16) | (gray << 8) | gray;

                    newImage.setRGB(x, y, newPixel);
                }
            }

            File outputFile = new File("saturne_quantification.png");
            ImageIO.write(newImage, "png", outputFile);

            System.out.println("Nouvelle image modifiée créée avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    public static void rehaussement() {
        try {
            // Charge l'image en niveaux de gris depuis un fichier
            File imageFile = new File("saturne_moche.png");
            image = ImageIO.read(imageFile);

            // Recupere les dimensions de l'image
            int width = image.getWidth();
            int height = image.getHeight();

            // Créer une nouvelle image avec les mêmes dimensions
            newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            int[] tab = historigramme("saturne_moche");

            int v1 = 0, v2 = 0;

            for (int i = 0; i < tab.length - 1; i++) {
                if (tab[i] == 0 && tab[i + 1] != 0)
                    v1 = i;
                if (tab[i] != 0 && tab[i + 1] == 0)
                    v2 = i;
            }

            float a = 255 / (v2 - v1);
            float b = -(255 * v1) / (v2 - v1);

            // Parcours image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Récupère la couleur du pixel (valeur en niveaux de gris)
                    int pixel = image.getRGB(x, y);

                    int gray = grayFromPixel(pixel);

                    gray = (int) (a * gray + b);

                    // Corrige les pixels négatifs
                    if (gray < 0) {
                        gray = -2 * gray;
                    }
                    // Augmentation luminosité
                    if (gray + 25 > 255) {
                        gray = 255;
                    } else {
                        gray += 25;
                    }

                    int newPixel = (255 << 24) | (gray << 16) | (gray << 8) | gray;

                    newImage.setRGB(x, y, newPixel);
                }
            }

            File outputFile = new File("saturne_rehaussement.png");
            ImageIO.write(newImage, "png", outputFile);

            System.out.println("Nouvelle image modifiée créée avec succès !");

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    public static void egalisationHistorigramme() {
        try {
            // Charge l'image en niveaux de gris depuis un fichier
            File imageFile = new File("saturne.png");
            image = ImageIO.read(imageFile);

            // Recupere les dimensions de l'image
            int width = image.getWidth();
            int height = image.getHeight();

            // Créer une nouvelle image avec les mêmes dimensions
            newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            int[] tab = historigrammeCumule("saturne");

            for (int i = 0; i < 256; i++) {
                double temp = ((255.0 / (image.getHeight() * image.getWidth())) * (double) tab[i]);
                tab[i] = (int) temp;
            }

            // Parcours image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Récupère la couleur du pixel (valeur en niveaux de gris)
                    int pixel = image.getRGB(x, y);

                    int gray = grayFromPixel(pixel);

                    gray = tab[gray];

                    int newPixel = (255 << 24) | (gray << 16) | (gray << 8) | gray;

                    newImage.setRGB(x, y, newPixel);
                }
            }

            File outputFile = new File("saturne_egalise.png");
            ImageIO.write(newImage, "png", outputFile);

            System.out.println("Nouvelle image modifiée créée avec succès !");

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    public static void erosionImage(String input, String output) {
        try {
            // Charge l'image en niveaux de gris depuis un fichier
            File imageFile = new File(input);
            BufferedImage image = ImageIO.read(imageFile);

            // Récupère les dimensions de l'image
            int width = image.getWidth();
            int height = image.getHeight();

            // Créer une nouvelle image avec les mêmes dimensions
            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    int temp = 0;

                    if (grayFromPixel(image.getRGB(x, y - 1)) == 0) {
                        temp++;
                    }
                    if (grayFromPixel(image.getRGB(x - 1, y)) == 0) {
                        temp++;
                    }
                    if (grayFromPixel(image.getRGB(x + 1, y)) == 0) {
                        temp++;
                    }
                    if (grayFromPixel(image.getRGB(x, y - 1)) == 0) {
                        temp++;
                    }

                    int newGray = (temp == 4) ? 0 : 255; // 255 si toujours blanc, sinon 0
                    int newPixel = (255 << 24) | (newGray << 16) | (newGray << 8) | newGray;
                    newImage.setRGB(x, y, newPixel);
                }
            }

            File outputFile = new File(output);
            ImageIO.write(newImage, "png", outputFile);
            System.out.println("Nouvelle image modifiée créée avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    public static void dilatationImage(String input, String output) {
        try {
            // Charge l'image en niveaux de gris depuis un fichier
            File imageFile = new File(input);
            BufferedImage image = ImageIO.read(imageFile);

            // Récupère les dimensions de l'image
            int width = image.getWidth();
            int height = image.getHeight();

            // Créer une nouvelle image avec les mêmes dimensions
            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    int temp = 0;

                    if (grayFromPixel(image.getRGB(x, y - 1)) == 255) {
                        temp++;
                    }
                    if (grayFromPixel(image.getRGB(x - 1, y)) == 255) {
                        temp++;
                    }
                    if (grayFromPixel(image.getRGB(x + 1, y)) == 255) {
                        temp++;
                    }
                    if (grayFromPixel(image.getRGB(x, y - 1)) == 255) {
                        temp++;
                    }

                    int newGray = (temp == 4) ? 255 : 0; // 255 si toujours blanc, sinon 0
                    int newPixel = (255 << 24) | (newGray << 16) | (newGray << 8) | newGray;
                    newImage.setRGB(x, y, newPixel);
                }
            }

            File outputFile = new File(output);
            ImageIO.write(newImage, "png", outputFile);
            System.out.println("Nouvelle image modifiée créée avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    public static void ouverture() {
        erosionImage("saturne_binarisation.png", "saturne_ouverture.png");
        dilatationImage("saturne_ouverture.png", "saturne_ouverture.png");
    }

    public static void fermeture() {
        dilatationImage("saturne_binarisation.png", "saturne_fermeture.png");
        erosionImage("saturne_fermeture.png", "saturne_fermeture.png");
    }

    public static void filtreMoyen(int window) {
        try {
            // Charger l'image en niveaux de gris
            File imageFile = new File("saturne.png");
            BufferedImage image = ImageIO.read(imageFile);

            // Récupérer les dimensions de l'image
            int width = image.getWidth();
            int height = image.getHeight();
            int halfSize = window / 2;

            // Créer une nouvelle image pour stocker le résultat
            BufferedImage moyenneImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // Parcourir chaque pixel de l'image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int sum = 0;
                    int count = 0;

                    // Appliquer le kernel pour la moyenne
                    for (int m = -halfSize; m <= halfSize; m++) {
                        for (int n = -halfSize; n <= halfSize; n++) {
                            int newY = y + m;
                            int newX = x + n;

                            // Vérifier les limites de l'image
                            if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                                int pixelValue = grayFromPixel(image.getRGB(newX, newY));
                                sum += pixelValue;
                                count++;
                            }
                        }
                    }

                    // Calculer la valeur moyenne du pixel
                    int meanValue = sum / count;

                    // Créer un pixel en niveaux de gris (R = G = B = meanValue)
                    int newPixel = (255 << 24) | (meanValue << 16) | (meanValue << 8) | meanValue;
                    moyenneImage.setRGB(x, y, newPixel);
                }
            }

            // Enregistrer l'image résultante
            File outputFile = new File("saturne_moyen.png");
            ImageIO.write(moyenneImage, "png", outputFile);

            System.out.println("Image enregistrée sous : saturne_moyen.png");

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    public static void filtreGaussien() {
        try {
            // Charger l'image en niveaux de gris
            File imageFile = new File("saturne.png");
            BufferedImage image = ImageIO.read(imageFile);

            // Récupérer les dimensions de l'image
            int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            int[][] tab = {
                    { 1, 2, 1 },
                    { 2, 4, 2 },
                    { 1, 2, 1 }
            };

            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    int sum = 0;

                    sum += tab[0][0] * grayFromPixel(image.getRGB(x - 1, y - 1));
                    sum += tab[0][1] * grayFromPixel(image.getRGB(x, y - 1));
                    sum += tab[0][2] * grayFromPixel(image.getRGB(x + 1, y - 1));
                    sum += tab[1][0] * grayFromPixel(image.getRGB(x - 1, y));
                    sum += tab[1][1] * grayFromPixel(image.getRGB(x, y));
                    sum += tab[1][2] * grayFromPixel(image.getRGB(x + 1, y));
                    sum += tab[2][0] * grayFromPixel(image.getRGB(x - 1, y + 1));
                    sum += tab[2][1] * grayFromPixel(image.getRGB(x, y + 1));
                    sum += tab[2][2] * grayFromPixel(image.getRGB(x + 1, y + 1));

                    int newGray = sum / 16;
                    int newPixel = (255 << 24) | (newGray << 16) | (newGray << 8) | newGray;
                    newImage.setRGB(x, y, newPixel);
                }
            }

            // Enregistrer l'image résultante
            File outputFile = new File("saturne_gauss.png");
            ImageIO.write(newImage, "png", outputFile);

            System.out.println("Image enregistrée sous : saturne_gauss.png");

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    public static void filtreRang(int window) {
        try {
            // Charger l'image en niveaux de gris
            File imageFile = new File("saturne.png");
            BufferedImage image = ImageIO.read(imageFile);

            // Récupérer les dimensions de l'image
            int width = image.getWidth();
            int height = image.getHeight();
            int halfSize = window / 2;

            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            BufferedImage newImage2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            BufferedImage newImage3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // Parcourir chaque pixel de l'image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int min = 255;
                    int max = 0;
                    int tab[] = new int[window * window];
                    int tmp = 0;

                    // Appliquer le kernel pour la moyenne
                    for (int m = -halfSize; m <= halfSize; m++) {
                        for (int n = -halfSize; n <= halfSize; n++) {
                            int newY = y + m;
                            int newX = x + n;

                            // Vérifier les limites de l'image
                            if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                                if (grayFromPixel(image.getRGB(newX, newY)) < min) {
                                    min = grayFromPixel(image.getRGB(newX, newY));
                                }
                                if (grayFromPixel(image.getRGB(newX, newY)) > max) {
                                    max = grayFromPixel(image.getRGB(newX, newY));
                                }

                                tab[tmp] = grayFromPixel(image.getRGB(newX, newY));

                                tmp++;
                            }
                        }
                    }

                    Arrays.sort(tab);

                    int median = tab[tab.length / 2];

                    int newGray = min;
                    int newPixel = (255 << 24) | (newGray << 16) | (newGray << 8) | newGray;
                    newImage.setRGB(x, y, newPixel);

                    newGray = max;
                    newPixel = (255 << 24) | (newGray << 16) | (newGray << 8) | newGray;
                    newImage2.setRGB(x, y, newPixel);

                    newGray = median;
                    newPixel = (255 << 24) | (newGray << 16) | (newGray << 8) | newGray;
                    newImage3.setRGB(x, y, newPixel);
                }
            }

            // Enregistrer l'image résultante
            File outputFile = new File("saturne_min.png");
            ImageIO.write(newImage, "png", outputFile);

            System.out.println("Image enregistrée sous : saturne_min.png");

            // Enregistrer l'image résultante
            outputFile = new File("saturne_max.png");
            ImageIO.write(newImage2, "png", outputFile);

            System.out.println("Image enregistrée sous : saturne_max.png");

            // Enregistrer l'image résultante
            outputFile = new File("saturne_median.png");
            ImageIO.write(newImage3, "png", outputFile);

            System.out.println("Image enregistrée sous : saturne_median.png");

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    public static void sobel() {
        try {
            // Charger l'image en niveaux de gris
            File imageFile = new File("saturne.png");
            BufferedImage image = ImageIO.read(imageFile);

            // Récupérer les dimensions de l'image
            int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            int[][] tab = {
                    { -1, 0, 1 },
                    { -2, 0, 2 },
                    { -1, 0, 1 }
            };

            int[][] tab2 = {
                    { -1, -2, -1 },
                    { 0, 0, 0 },
                    { 1, 2, 1 }
            };

            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    int sum = 0;

                    sum += tab[0][0] * grayFromPixel(image.getRGB(x - 1, y - 1));
                    sum += tab[0][1] * grayFromPixel(image.getRGB(x, y - 1));
                    sum += tab[0][2] * grayFromPixel(image.getRGB(x + 1, y - 1));
                    sum += tab[1][0] * grayFromPixel(image.getRGB(x - 1, y));
                    sum += tab[1][1] * grayFromPixel(image.getRGB(x, y));
                    sum += tab[1][2] * grayFromPixel(image.getRGB(x + 1, y));
                    sum += tab[2][0] * grayFromPixel(image.getRGB(x - 1, y + 1));
                    sum += tab[2][1] * grayFromPixel(image.getRGB(x, y + 1));
                    sum += tab[2][2] * grayFromPixel(image.getRGB(x + 1, y + 1));

                    sum = (sum < 0) ? 0 : sum;
                    sum = (sum > 255) ? 255 : sum;

                    int newGray = sum;
                    int newPixel = (255 << 24) | (newGray << 16) | (newGray << 8) | newGray;
                    newImage.setRGB(x, y, newPixel);
                }
            }

            // Enregistrer l'image résultante
            File outputFile = new File("saturne_sobel.png");
            ImageIO.write(newImage, "png", outputFile);

            image = ImageIO.read(outputFile);

            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    int sum = 0;

                    sum += tab2[0][0] * grayFromPixel(image.getRGB(x - 1, y - 1));
                    sum += tab2[0][1] * grayFromPixel(image.getRGB(x, y - 1));
                    sum += tab2[0][2] * grayFromPixel(image.getRGB(x + 1, y - 1));
                    sum += tab2[1][0] * grayFromPixel(image.getRGB(x - 1, y));
                    sum += tab2[1][1] * grayFromPixel(image.getRGB(x, y));
                    sum += tab2[1][2] * grayFromPixel(image.getRGB(x + 1, y));
                    sum += tab2[2][0] * grayFromPixel(image.getRGB(x - 1, y + 1));
                    sum += tab2[2][1] * grayFromPixel(image.getRGB(x, y + 1));
                    sum += tab2[2][2] * grayFromPixel(image.getRGB(x + 1, y + 1));

                    sum = (sum < 0) ? 0 : sum;
                    sum = (sum > 255) ? 255 : sum;

                    int newGray = sum;
                    int newPixel = (255 << 24) | (newGray << 16) | (newGray << 8) | newGray;
                    newImage.setRGB(x, y, newPixel);
                }
            }

            outputFile = new File("saturne_sobel.png");
            ImageIO.write(newImage, "png", outputFile);

            System.out.println("Image enregistrée sous : saturne_sobel.png");

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        binarisation();

        negatif();

        quantification();

        rehaussement();

        egalisationHistorigramme();

        erosionImage("saturne_binarisation.png", "saturne_erosion.png");

        dilatationImage("saturne_binarisation.png", "saturne_dilatation.png");

        ouverture();

        fermeture();

        filtreMoyen(11);

        filtreGaussien();

        filtreRang(11);

        sobel();
    }
}
