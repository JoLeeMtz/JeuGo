import java.util.ArrayList;

public interface Affichage {
    String JETON = "JETON";
    String NOIR = "X";
    String BLANC = "O";
    String SEP_HORI = "-------------------------------------------------------";
    String SEP_VERT = "|  ";
    String ESPACE = " ";
    int MAX_COLONNE = 9;
    int MAX_RANGEE = 9;
    int MIN_COLONNE = 1;
    int MIN_RANGEE = 1;


    void affichageTableau(ArrayList<Instruction> infos);
    void affichageLegende(ArrayList<Instruction> infos);
    void affichageAQuiLeTour(int nbTour);
    void affichagePoints(Joueur jNoir, Joueur jBlanc);
}
