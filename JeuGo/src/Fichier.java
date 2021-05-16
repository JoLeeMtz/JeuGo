public class Fichier {
    final char SEPARATEUR = ' ';
    private String nomFichier;
    private String ligne;

    // GETTERS
    public String getNomFichier() {
        return nomFichier;
    }
    public String getLigne() {
        return ligne;
    }

    // SETTERS
    protected void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }
    protected void setLigne(String ligne) {
        this.ligne = ligne;
    }
}
