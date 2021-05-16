import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LireFichier extends Fichier {

    /**
     * Constructeur qui initialise le nom du fichier
     * */
    public LireFichier(String nomFichier) {
        setNomFichier(nomFichier);
    }

    /**
     * Lis fichier + sauvegarde la ligne lu dans ligne
     * */
    public void lecture() {
        try {
            File fichier = new File(getNomFichier());
            Scanner lire = new Scanner(fichier);
            String donnees = "";

            while (lire.hasNextLine()) {
                donnees = lire.nextLine();
            }

            setLigne(donnees);
            lire.close();
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier est introuvable.");
        }
    }

    public String getElement(int positionElem) {
        String element= "";

        if (getLigne().length() > 0) {
            if (positionElem < getLigne().length()) {
                // Set premiere occurence de SEPARATEUR a partir de la position i
                int indexFinElement = getLigne().indexOf(SEPARATEUR, positionElem);

                if (indexFinElement != -1) {
                    // Set l'element de l'index i a indexFinElement
                    element = getLigne().substring(positionElem, indexFinElement);
                } else {
                    // Set l'element de l'index i a indexFinElement
                    element = getLigne().substring(positionElem);
                }
            }
        }
        return element;
    }
}
