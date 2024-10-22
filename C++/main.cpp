// Lire l'image
    cv::Mat image = cv::imread(argv[1], cv::IMREAD_GRAYSCALE);

    if (image.empty()) { // Vérification de si l'image existe
        cerr << "Erreur de lecture de l'image!" << endl;
        exit(EXIT_FAILURE);
    }


cv::Mat binarisation(cv::Mat image, char* nomImage){
    
    for (int y = 0; y < image.rows; y++) {
        for (int x = 0; x < image.cols; x++) {
            
            uchar& pixel = image.at<uchar>(y, x); // Accéder aux valeurs RGB de chaque pixel

            // Si la valeur de R, G ou B est supérieur à 128, elle passe à 255 sinon 0
            pixel = pixel > 128?255:0;

        }
    }


    string fichier_modifie = "images/binarise-" + string(nomImage);

    cv::imwrite(fichier_modifie.c_str(), image); // Sauvearde en fichier

    std::cout << "Image binarisée et enregistrée!" << std::endl;

    return image.clone();
}// fin binarisation