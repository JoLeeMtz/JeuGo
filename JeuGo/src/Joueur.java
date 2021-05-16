public class Joueur {
    private int points;
    private Couleur couleur;

    Joueur(Couleur couleur) {
        this.points = 0;
        this.couleur = couleur;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public void setCouleur(Couleur couleur) {
        this.couleur = couleur;
    }

}
