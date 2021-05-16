import java.util.ArrayList;

public class Jeton implements RecuperationJeton, TypeInstruction {
    private Couleur couleurJeton;
    private final Position position;
    // Les positions voisin ou il n'y a pas de Jeton
    private ArrayList<Position> libertesDuJeton;
    // Les positions voisin ou il y a un Jeton de meme couleur
    private ArrayList<Position> jetonLibMemeCouleur;
    // Set s'il a ete parcouru par une fonction (pour faire suppression, verifier s'il a ete entourer)
    private boolean jetonLibMemeCouleurParcour;
    // Permet d'identifier s'il est un coin
    private boolean estExtremite;
    // Identifie s'il est supprimer
    private boolean isDeleted;
    // Indique que c'est un jeton (contient position)
    private String info;
    // Indique si le point a ete compter
    private boolean ptsCalculer;

    public Jeton( Couleur couleurJeton, Position position ) {
        this.couleurJeton = couleurJeton;
        this.position = position;
        this.libertesDuJeton = new ArrayList<>();
        this.jetonLibMemeCouleur = new ArrayList<>();
        this.jetonLibMemeCouleurParcour = false;
        this.isDeleted = false;
        this.estExtremite = false;
        this.ptsCalculer = false;
    }

    public Couleur getCouleurJeton() {
        return couleurJeton;
    }
    public void setCouleurJeton(Couleur nouvelleCouleur) {
        this.couleurJeton = nouvelleCouleur;
    }

    public Position getPosition() {
        return position;
    }

    public ArrayList<Position> getLibertesDuJeton() {
        return libertesDuJeton;
    }

    public ArrayList<Position> getJetonLibMemeCouleur() {
        return jetonLibMemeCouleur;
    }

    public boolean getJetonLibMemeCouleurParcour() {
        return jetonLibMemeCouleurParcour;
    }

    public void setLibertesDuJeton( ArrayList<Position> libertesDuJeton ) {
        this.libertesDuJeton = libertesDuJeton;
    }

    public void setJetonLibMemeCouleur( ArrayList<Position> jetonLibMemeCouleur ) {
        this.jetonLibMemeCouleur = jetonLibMemeCouleur;
    }

    public void setJetonLibMemeCouleurParcour( boolean jetonLibMemeCouleurParcour ) {
        this.jetonLibMemeCouleurParcour = jetonLibMemeCouleurParcour;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted( boolean deleted ) {
        isDeleted = deleted;
    }

    public boolean getEstExtremite() {
        return estExtremite;
    }

    public void setEstExtremite(boolean estExtremite) {
        this.estExtremite = estExtremite;
    }

    /**
     * Set liberte, croix (haut, bas, gauche et a droite du jeton)
     * */
    public void addLiberteJeton(ArrayList<Instruction> infos) throws Exception {
        final int decaleNeg = -1;
        final int decalePos = 1;
        // jeton temporaire qui represente jeton voisin du jeton passe en parametre
        Jeton jetonVoisin;
        // tableau de liberte pour jeton passe en parametre
        ArrayList<Position> liberteJeton = new ArrayList<>();


        jetonVoisin = getJetonPosX(infos, decalePos);
        // (jetonVoisin != null) -> interieur du tableau
        if (jetonVoisin != null) {
            // true pas de jeton a la rangee + 1
            if (getJeton(infos, jetonVoisin.getPosition()) == null) {
                liberteJeton.add(jetonVoisin.getPosition());
            }
            // si Jeton est supprimer (compte comme case vide)
            else if (jetonVoisin.isDeleted()) {
                liberteJeton.add(jetonVoisin.getPosition());
            }
        }

        jetonVoisin = getJetonPosX(infos, decaleNeg);
        // (jetonVoisin != null) -> interieur du tableau
        if (jetonVoisin != null) {
            // true pas de jeton a la rangee - 1
            if (getJeton(infos, jetonVoisin.getPosition()) == null) {
                liberteJeton.add(jetonVoisin.getPosition());
            }
            // si Jeton est supprimer (compte comme case vide)
            else if (jetonVoisin.isDeleted()) {
                liberteJeton.add(jetonVoisin.getPosition());
            }
        }

        jetonVoisin = getJetonPosY(infos, decalePos);
        // (jetonVoisin != null) -> interieur du tableau
        if (jetonVoisin != null) {
            // true pas de jeton a la colonne + 1
            if (getJeton(infos, jetonVoisin.getPosition()) == null) {
                liberteJeton.add(jetonVoisin.getPosition());
            }
            // si Jeton est supprimer (compte comme case vide)
            else if (jetonVoisin.isDeleted()) {
                liberteJeton.add(jetonVoisin.getPosition());
            }
        }

        jetonVoisin = getJetonPosY(infos, decaleNeg);
        // (jetonVoisin != null) -> interieur du tableau
        if (jetonVoisin != null) {
            // true pas de jeton a la colonne - 1
            if (getJeton(infos, jetonVoisin.getPosition()) == null) {
                liberteJeton.add(jetonVoisin.getPosition());
            }
            // si Jeton est supprimer (compte comme case vide)
            else if (jetonVoisin.isDeleted()) {
                liberteJeton.add(jetonVoisin.getPosition());
            }
        }

        this.setLibertesDuJeton(liberteJeton);
    }
    /**
     * Set liberte, croix (haut, bas, gauche et a droite du jeton)
     * */
    public void addLiberteJetonMemeCouleur(ArrayList<Instruction> infos) throws Exception {
        final int decaleNeg = -1;
        final int decalePos = 1;
        // jeton temporaire qui represente jeton voisin de meme couleur du jeton passe en parametre
        Jeton jetonVoisin;
        // tableau de liberte pour jeton passe en parametre
        ArrayList<Position> liberteJetonMemeCouleur = new ArrayList<>();


        jetonVoisin = getJetonPosX(infos, decalePos);
        // si jetonVoisin != null -> interieur tableau
        if (jetonVoisin != null) {
            // si Jeton n'est pas supprimer
            if (!jetonVoisin.isDeleted()) {
                // (jetonVoisin == this) -> pas de Jeton a cette position
                if (jetonVoisin.getCouleurJeton().equals(this.getCouleurJeton())) {
                    // != de null -> jeton a la rangee + 1
                    if (getJeton(infos, jetonVoisin.getPosition()) != null) {
                        liberteJetonMemeCouleur.add(jetonVoisin.getPosition());
                    }
                }
            }
        }

        jetonVoisin = getJetonPosX(infos, decaleNeg);
        // si jetonVoisin != null -> interieur tableau
        if (jetonVoisin != null) {
            // si Jeton n'est pas supprimer
            if (!jetonVoisin.isDeleted()) {
                // (jetonVoisin == this) -> pas de Jeton a cette position
                if (jetonVoisin.getCouleurJeton().equals(this.getCouleurJeton())) {
                    // != de null -> jeton a la rangee - 1
                    if (getJeton(infos, jetonVoisin.getPosition()) != null) {
                        liberteJetonMemeCouleur.add(jetonVoisin.getPosition());
                    }
                }
            }
        }

        jetonVoisin = getJetonPosY(infos, decalePos);
        // si jetonVoisin != null -> interieur tableau
        if (jetonVoisin != null) {
            // si Jeton n'est pas supprimer
            if (!jetonVoisin.isDeleted()) {
                // (jetonVoisin == this) -> pas de Jeton a cette position
                if (jetonVoisin.getCouleurJeton().equals(this.getCouleurJeton())) {
                    // != de null -> jeton a la colonne + 1
                    if (getJeton(infos, jetonVoisin.getPosition()) != null) {
                        liberteJetonMemeCouleur.add(jetonVoisin.getPosition());
                    }
                }
            }
        }

        jetonVoisin = getJetonPosY(infos, decaleNeg);
        // si jetonVoisin != null -> interieur tableau
        if (jetonVoisin != null) {
            // si Jeton n'est pas supprimer
            if (!jetonVoisin.isDeleted()) {
                // (jetonVoisin == this) -> pas de Jeton a cette position
                if (jetonVoisin.getCouleurJeton().equals(this.getCouleurJeton())) {
                    // != de null -> jeton a la colonne - 1
                    if (getJeton(infos, jetonVoisin.getPosition()) != null) {
                        liberteJetonMemeCouleur.add(jetonVoisin.getPosition());
                    }
                }
            }
        }

        this.setJetonLibMemeCouleur(liberteJetonMemeCouleur);
    }


    /**
     * Retourne le jeton qui existe a la position passe en parametre
     * retourne null si n'existe pas
     * */
    @Override
    public Jeton getJeton(ArrayList<Instruction> infos, Position pos) {
        for (Instruction elem : infos) {
            // Si elem == JETON
            if (elem.getElement().getType().equals(elem.getElement().JETON)) {
                // Si la position d'elem == pos
                if (elem.getJeton().getPosition().equals(pos)) {
                    // Si elem n'est pas supprimer
                    if (!elem.getJeton().isDeleted())
                        return elem.getJeton();
                }
            }
        }
        return null;
    }

    /**
     * Retourne le jeton qui est supprimer a la position passe en parametre
     * retourne null si n'est pas supprimer ou transparent
     * */
    @Override
    public Jeton getDeletedJeton(ArrayList<Instruction> infos, Position pos) {
        for (Instruction elem : infos) {
            // Si elem == JETON
            if (elem.getElement().getType().equals(elem.getElement().JETON)) {
                // Si la position d'elem == pos
                if (elem.getJeton().getPosition().equals(pos)) {
                    // Si elem est supprimer ou de couleur Transparent
                    if (elem.getJeton().isDeleted() || elem.getJeton().getCouleurJeton().equals(Couleur.TRANSPARENT))
                        return elem.getJeton();
                }
            }
        }
        return null;
    }

    /**
     * Deplace la rangee de jeton decaleX nombre de fois
     * retourne le jeton a cette position
     * retourne null si exterieur du tableau
     * */
    private Jeton getJetonPosX(ArrayList<Instruction> infos, int decaleX) throws Exception {
        // retourne meme rangee si n'existe pas de decalage
        String nouvelleRangee = Rangee.getValueDecalPos(this.getPosition().getRangee(), decaleX);
        Jeton newJeton = null;

        // si nouvelleRangee ne retourne pas la meme rangee
        if (!nouvelleRangee.equals(this.getPosition().getRangee())) {
            // set nouvelle position
            Position nouvellePosition = new Position(this.getPosition().getColonne(), nouvelleRangee);
            // get information du Jeton existant
            Jeton jetonTmp = getJeton(infos, nouvellePosition);

            // si Jeton n'existe pas
            if (jetonTmp == null) {
                // créer le jeton avec la nouvelle position
                newJeton = new Jeton(Couleur.TRANSPARENT, nouvellePosition);
            } else {
                newJeton = jetonTmp;
            }

            return newJeton;
        }

        return newJeton;
    }
    /**
     * Deplace la colonne de jeton decaleY nombre de fois
     * retourne le jeton a cette position
     * retourne null exterieur du tableau
     * */
    private Jeton getJetonPosY(ArrayList<Instruction> infos, int decaleY) throws Exception {
        // retourne meme colonne si n'existe pas de decalage
        String nouvelleColonne = Colonne.getValueDecalPos(this.getPosition().getColonne(), decaleY);
        Jeton newJeton = null;

        if (!nouvelleColonne.equals(this.getPosition().getColonne())) {
            // set nouvelle position
            Position nouvellePosition = new Position(nouvelleColonne, this.getPosition().getRangee());
            // get information du Jeton existant
            Jeton jetonTmp = getJeton(infos, nouvellePosition);

            // si Jeton n'existe pas
            if (jetonTmp == null) {
                // créer le jeton avec la nouvelle position
                newJeton = new Jeton(Couleur.TRANSPARENT, nouvellePosition);
            } else {
                newJeton = jetonTmp;
            }

            return newJeton;
        }

        return newJeton;
    }

    @Override
    public String toString() {
        return "couleurJeton=" + couleurJeton +
                ", position=" + position +
                ", Liberté : " + libertesDuJeton.toString() +
                ", Liberté même couleur : " + jetonLibMemeCouleur.toString() +
                ", isDeleted : " + isDeleted +
                ", jetonLibMemeCouleurParcour : " + jetonLibMemeCouleurParcour;
    }

    @Override
    public String getType() {
        return info;
    }

    @Override
    public void setType(String info) {
        this.info = info;
    }

    public boolean isPtsCalculer() {
        return ptsCalculer;
    }

    public void setPtsCalculer(boolean ptsCalculer) {
        this.ptsCalculer = ptsCalculer;
    }
}
