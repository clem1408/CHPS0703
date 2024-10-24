#include <iostream>
#include <opencv2/opencv.hpp>
#include <string>

using namespace cv;
using namespace std;

std::string getFileExtension(std::string str) {
  return str.substr(str.length() - 4);
}

std::string getFileName(std::string str, std::string type) {
  std::string extension = getFileExtension(str);

  if (str.length() > 4) {
    str.erase(str.length() - 4);
  }

  size_t lastSlashPos = str.find_last_of("/");

  std::string fileName = str.substr(lastSlashPos + 1);

  return "../Images/" + fileName + "_edit/" + fileName + type + extension;
}

cv::Mat binarisation(cv::Mat image, char *nomImage) {
  for (int y = 0; y < image.rows; y++) {
    for (int x = 0; x < image.cols; x++) {
      uchar &pixel =
          image.at<uchar>(y, x);     // Accéder aux valeurs de chaque pixel
      pixel = pixel > 128 ? 255 : 0; // Binarisation
    }
  }

  std::string str(nomImage);

  std ::string fichier_modifie = getFileName(str, "Binarisation");

  cv::imwrite(fichier_modifie.c_str(), image); // Sauvegarde en fichier

  std::cout << "Image binarisée et enregistrée!" << std::endl;
  return image.clone();
}

cv::Mat negatif(cv::Mat image, char *nomImage) {
  for (int y = 0; y < image.rows; y++) {
    for (int x = 0; x < image.cols; x++) {
      uchar &pixel =
          image.at<uchar>(y, x); // Accéder aux valeurs de chaque pixel
      pixel = 255 - pixel;       // Negatif
    }
  }

  std::string str(nomImage);

  std ::string fichier_modifie = getFileName(str, "Negatif");

  cv::imwrite(fichier_modifie.c_str(), image); // Sauvegarde en fichier

  std::cout << "Filtre negatif appliqué et enregistrée!" << std::endl;
  return image.clone();
}

cv::Mat quantification(cv::Mat image, char *nomImage) {
  for (int y = 0; y < image.rows; y++) {
    for (int x = 0; x < image.cols; x++) {
      uchar &pixel =
          image.at<uchar>(y, x); // Accéder aux valeurs de chaque pixel
      pixel = (pixel / 64) * 64; // Quantification
    }
  }

  std::string str(nomImage);

  std ::string fichier_modifie = getFileName(str, "Quantification");

  cv::imwrite(fichier_modifie.c_str(), image); // Sauvegarde en fichier

  std::cout << "Filtre quantificatif appliqué et enregistrée!" << std::endl;
  return image.clone();
}

int main(int argc, char **argv) {
  if (argc != 2) {
    cerr << "Usage: ./program <image_path>" << endl;
    return -1;
  }

  // Lire l'image
  cv::Mat image = cv::imread(argv[1], cv::IMREAD_GRAYSCALE);

  if (image.empty()) {
    cerr << "Erreur de lecture de l'image!" << endl;
    return -1;
  }

  // Binarisation
  cv::Mat imageBinaire = binarisation(image.clone(), argv[1]);

  // Negatif
  cv::Mat imageNegatif = negatif(image.clone(), argv[1]);

  // Quantification
  cv::Mat imageQuantification = quantification(image.clone(), argv[1]);

  return 0;
}
