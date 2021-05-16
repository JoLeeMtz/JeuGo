import java.util.ArrayList;

public class Territoire implements RecuperationJeton {
    private ArrayList<Jeton> territoire;
    // Region ou il n'y a pas de Jeton
    ArrayList<Instruction> territoireSansJeton = new ArrayList<>();

    public Territoire() {
        this.territoire = new ArrayList<>();
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
     * Fait la mis a jour de ce qui c'est produit dans Territoire
     * Set les memes infos dans setJetonLibMemeCouleurParcour du 2ieme
     * parametre dans le premier
     * */
    public void setTerritoireDsInfos(ArrayList<Instruction> infos, ArrayList<Jeton> territoire) {
        final boolean PARCOURU = true;

        for (Instruction instr : infos) {
            for (Jeton jeton : territoire) {
                if (!instr.getElement().getType().equals(Constantes.PASS)) {
                    // Si (Jeton dans infos) == (Jeton dans territoire)
                    if (instr.getJeton().equals(jeton)) {
                        // Si (Jeton dans territoire) a ete parcouru
                        if (jeton.getJetonLibMemeCouleurParcour()) {
                            // Set (Jeton dans infos) qu'il a ete parcouru aussi
                            instr.getJeton().setJetonLibMemeCouleurParcour(PARCOURU);
                        }
                    }
                }
            }
        }
    }
    /**
     * Capture les territoires ou les jetons sont encercler
     * (par des jetons de couleur oppose, verifie le cas
     * avec les murs aussi)
     *
     * @return true s'il y a capture de Jeton(s)
     * */
    public boolean captureTerritoire(ArrayList<Instruction> infos, Jeton jeton) {
        final int UN = 1;
        final int DEUX = 2;

        // Si jeton n'a pas de liberte
        if (jeton.getLibertesDuJeton().size() == Constantes.VIDE) {
            // Si (nombre de liberte de meme couleur) == VIDE
            if (jeton.getJetonLibMemeCouleur().size() == Constantes.VIDE) {
                jeton.setJetonLibMemeCouleurParcour(Constantes.VRAI);
                return Constantes.VRAI;
            }
            // Jeton a au moins 1 voisin de meme couleur
            for (int i = 0; i < jeton.getJetonLibMemeCouleur().size(); ++i) {
                // Set informations du Jeton voisin de meme couleur
                Position positionJetonVoisin = jeton.getJetonLibMemeCouleur().get(i);
                Jeton jetonVoisin = getJeton(infos, positionJetonVoisin);

                // Si jetonVoisin n'a pas de liberte
                if (jetonVoisin.getLibertesDuJeton().size() == Constantes.VIDE) {
                    // N'a pas deja ete parcouru
                    if (!jeton.getJetonLibMemeCouleurParcour()) {
                        // Si getJetonLibMemeCouleur contient 1 jeton
                        if (jeton.getJetonLibMemeCouleur().size() == UN && jetonVoisin.getJetonLibMemeCouleur().size() == UN) {
                            // Si encercler
                            if (jeton.getLibertesDuJeton().size() == Constantes.VIDE && jetonVoisin.getLibertesDuJeton().size() == Constantes.VIDE) {
                                jeton.setJetonLibMemeCouleurParcour(Constantes.VRAI);
                                jetonVoisin.setJetonLibMemeCouleurParcour(Constantes.VRAI);

                                return Constantes.VRAI;
                            }
                        }
                        // Si Jeton se situe entourer d'une autre couleur, sauf un cote qui est d'un Jeton de meme couleur
                        else if (jeton.getJetonLibMemeCouleur().size() == UN) {
                            jeton.setJetonLibMemeCouleurParcour(Constantes.VRAI);

                            // Si jetonVoisin == estDejaParcouru
                            if (jetonVoisin.getJetonLibMemeCouleurParcour()) {
                                // S'il existe un autre jeton non parcouru dans les voisins de jetonVoisin
                                if (getJetonNonParcouru(infos, jetonVoisin) != null) {
                                    Jeton jetonTmp = getJetonNonParcouru(infos, jetonVoisin);
                                    captureTerritoire(infos, jetonTmp);
                                } else {
                                    return Constantes.VRAI;
                                }
                            } else {
                                return captureTerritoire(infos, jetonVoisin);
                            }
                        }
                        // Si quantite de Jeton meme couleur est de DEUX
                        else if (jeton.getJetonLibMemeCouleur().size() >= DEUX) {
                            jeton.setJetonLibMemeCouleurParcour(Constantes.VRAI);
                            Jeton jetonTmp = getJetonNonParcouru(infos, jeton);

                            // Si jeton n'a pas de liberte
                            if (jeton.getLibertesDuJeton().size() == Constantes.VIDE && jeton.getJetonLibMemeCouleur().size() == DEUX) {
                                // SetEstExtremite
                                jeton.setEstExtremite(Constantes.VRAI);
                            }

                            // Si jetontTmp est un Jeton pas supprimer
                            if (jetonTmp != null) {
                                if (captureTerritoire(infos, jetonTmp)) {
                                    jetonTmp = getJetonNonParcouru(infos, jeton);

                                    // Si jetonTmp n'a pas d'autre voisin non parcouru
                                    if (jetonTmp == null) {
                                        // Alors cette toute les extremites a faux
                                        setAllExtremite(infos, Constantes.FAUX);
                                        return Constantes.VRAI;
                                    }
                                    else {
                                        return captureTerritoire(infos,jetonTmp);
                                    }
                                }
                            }
                            return Constantes.FAUX;
                        }
                    }
                }
            }
        }
        return Constantes.FAUX;
    }
    /**
     * Capture les territoires ou les jetons sont encercler
     * (par des jetons de couleur oppose, verifie le cas
     * avec les murs aussi)
     *
     * @return true s'il y a capture de Jeton(s)
     * */
    public boolean captureTerritoire1(ArrayList<Instruction> infos, Jeton jeton, Joueur joueur) throws Exception {
        final int UN = 1;
        final int DEUX = 2;


        // Check si jeton a deja ete calculer
        if (!jeton.isPtsCalculer()) {
            // Si jeton n'a pas de liberte
            if (jeton.getLibertesDuJeton().size() == Constantes.VIDE) {
                // Si (nombre de liberte de meme couleur) == VIDE
                if (jeton.getJetonLibMemeCouleur().size() == Constantes.VIDE) {
                    if (verifierCroisMemeCouleur(infos, joueur, jeton.getPosition())) {
                        //jeton.setJetonLibMemeCouleurParcour(Constantes.VRAI);
                        return Constantes.VRAI;
                    }

                }
                // Jeton a au moins 1 voisin de meme couleur
                for (int i = 0; i < jeton.getJetonLibMemeCouleur().size(); ++i) {
                    // Set informations du Jeton voisin de meme couleur
                    Position positionJetonVoisin = jeton.getJetonLibMemeCouleur().get(i);
                    Jeton jetonVoisin = getJeton(infos, positionJetonVoisin);

                    // Si jetonVoisin n'a pas de liberte
                    if (jetonVoisin.getLibertesDuJeton().size() == Constantes.VIDE) {
                        // N'a pas deja ete parcouru
                        if (!jeton.getJetonLibMemeCouleurParcour()) {
                            // Si getJetonLibMemeCouleur contient 1 jeton
                            if (jeton.getJetonLibMemeCouleur().size() == UN && jetonVoisin.getJetonLibMemeCouleur().size() == UN) {
                                // Si encercler
                                if (jeton.getLibertesDuJeton().size() == Constantes.VIDE && jetonVoisin.getLibertesDuJeton().size() == Constantes.VIDE) {
                                    jeton.setJetonLibMemeCouleurParcour(Constantes.VRAI);
                                    jetonVoisin.setJetonLibMemeCouleurParcour(Constantes.VRAI);

                                    return Constantes.VRAI;
                                }
                            }
                            // Si Jeton se situe entourer d'une autre couleur, sauf un cote qui est d'un Jeton de meme couleur
                            else if (jeton.getJetonLibMemeCouleur().size() == UN) {
                                jeton.setJetonLibMemeCouleurParcour(Constantes.VRAI);

                                // Si jetonVoisin == estDejaParcouru
                                if (jetonVoisin.getJetonLibMemeCouleurParcour()) {
                                    // S'il existe un autre jeton non parcouru dans les voisins de jetonVoisin
                                    if (getJetonNonParcouru(infos, jetonVoisin) != null) {
                                        Jeton jetonTmp = getJetonNonParcouru(infos, jetonVoisin);
                                        captureTerritoire1(infos, jetonTmp, joueur);
                                    } else {
                                        return Constantes.VRAI;
                                    }
                                } else {
                                    return captureTerritoire1(infos, jetonVoisin, joueur);
                                }
                            }
                            // Si quantite de Jeton meme couleur est de DEUX
                            else if (jeton.getJetonLibMemeCouleur().size() >= DEUX) {
                                jeton.setJetonLibMemeCouleurParcour(Constantes.VRAI);
                                Jeton jetonTmp = getJetonNonParcouru(infos, jeton);

                                // Si jeton n'a pas de liberte
                                if (jeton.getLibertesDuJeton().size() == Constantes.VIDE && jeton.getJetonLibMemeCouleur().size() == DEUX) {
                                    // SetEstExtremite
                                    jeton.setEstExtremite(Constantes.VRAI);
                                }

                                // Si jetontTmp est un Jeton pas supprimer
                                if (jetonTmp != null) {
                                    if (captureTerritoire1(infos, jetonTmp, joueur)) {
                                        jetonTmp = getJetonNonParcouru(infos, jeton);

                                        // Si jetonTmp n'a pas d'autre voisin non parcouru
                                        if (jetonTmp == null) {
                                            // Alors cette toute les extremites a faux
                                            setAllExtremite(infos, Constantes.FAUX);
                                            return Constantes.VRAI;
                                        } else {
                                            return captureTerritoire1(infos, jetonTmp, joueur);
                                        }
                                    }
                                }
                                return Constantes.FAUX;
                            }
                        }
                    }
                }
            }
        }
        return Constantes.FAUX;
    }
    /**
     * Get jeton voisin de meme couleur qui n'a pas ete parcouru encore
     * S'il y en n'a pas return null
     * */
    public Jeton getJetonNonParcouru(ArrayList<Instruction> infos, Jeton jeton) {
        for (Position pos : jeton.getJetonLibMemeCouleur()) {
            Jeton jetonTmp = getJeton(infos, pos);
            // Si JetonTmp existe
            if (jetonTmp != null) {
                // Si JetonTmp a ete parcouru par liberter meme couleur
                if (!jetonTmp.getJetonLibMemeCouleurParcour()) {
                    return jetonTmp;
                }
            }
        }

        return null;
    }
    /**
     * Reinitialise les jetons a parcouru (true ou false)
     * */
    public void setJetonsParcouru(ArrayList<Instruction> infos, boolean parcouru) {
        for (Instruction instr : infos) {
            if (!instr.getElement().getType().equals(Constantes.PASS)) {
                instr.getJeton().setJetonLibMemeCouleurParcour(parcouru);
            }
        }
    }

    /**
     * Creer un tableau territoire avec les voisins du Jeton
     * passe en parametre et par la suite avec les voisins
     * du voisin du Jeton, etc..
     * */
    public ArrayList<Jeton> creerTableauTerritoire(ArrayList<Instruction> infos, Jeton jeton) {
        Jeton jetonTmp;
        ArrayList<Jeton> territoire = new ArrayList<>();
        final boolean PARCOURU = true;

        if (!jeton.getJetonLibMemeCouleurParcour()) {
            jetonTmp = getJeton(infos, jeton.getPosition());
            // Ajout du Jeton
            territoire.add(jetonTmp);

            // Set Jeton passe en parametre a PARCOURU
            jeton.setJetonLibMemeCouleurParcour(PARCOURU);
        }

        // Si Jeton contient au moins 1 voisin de meme couleur
        if (jeton.getJetonLibMemeCouleur().size() > Constantes.VIDE) {
            for (int i = 0; i < jeton.getJetonLibMemeCouleur().size(); ++i) {
                jetonTmp = getJeton(infos, jeton.getJetonLibMemeCouleur().get(i));
                // Si Jeton existe
                if (jetonTmp != null) {
                    // Si Jeton n'est pas supprimer
                    if (!jetonTmp.isDeleted()) {
                        // Si le tableau territoire ne contient pas deja ce Jeton et n'a pas deja ete parcouru
                        if (!territoire.contains(jetonTmp) && !jetonTmp.getJetonLibMemeCouleurParcour()) {
                            // Ajoute dans tableau
                            territoire.add(jetonTmp);
                            // Set qu'il a deja etait parcouru
                            jetonTmp.setJetonLibMemeCouleurParcour(PARCOURU);

                            territoire.addAll(creerTableauTerritoire(infos, jetonTmp));
                        }
                    }
                }
            }
        }

        this.territoire = territoire;
        return territoire;
    }

    /**
     * Retourne faux si trouve dans le territoire
     * passe en parametre une liberte pour un Jeton
     * */
    public boolean checkSiTerritoireAucuneLib(ArrayList<Jeton> territoire) {
        for (Jeton jeton : territoire) {
            // Si Jeton n'est pas supprimer && a une liberte
            if (!jeton.isDeleted() && jeton.getLibertesDuJeton().size() != Constantes.VIDE)
                return Constantes.FAUX;
        }

        return Constantes.VRAI;
    }

    /**
     * Verifie si un territoire form un carre
     * (qui contient 4 Jeton, avec le getEstExtremite == true)
     * Return true si trouver
     * */
    public int nbExtremite(ArrayList<Jeton> territoire) {
        final int NB_MIN_JETON = 2;
        int nbCoins = 0;

        // Si territoire est creer
        if (territoire != null) {
            // S'il y a au moins NB_MIN_JETON jetons dans le territoire
            if (territoire.size() >= NB_MIN_JETON) {
                for (int i = 0; i < territoire.size(); ++i) {
                    if (territoire.get(i).getEstExtremite()) {
                        ++nbCoins;
                    }
                }
                // Si le territoire n'a pas de liberte
                if (!checkSiTerritoireAucuneLib(territoire)) {
                    nbCoins = 0;
                } else {
                    nbCoins = 4;
                }
            }
        }

        return nbCoins;
    }

    /**
     * Set tout les instructions qui sont un Jeton
     * a estExtremite = estUneExtremite
     * */
    public void setAllExtremite(ArrayList<Instruction> infos, boolean estUneExtremite) {
        for (Instruction instr : infos) {
            if (!instr.getElement().getType().equals(Constantes.PASS)) {
                instr.getJeton().setEstExtremite(estUneExtremite);
            }
        }
    }


    /**
     * Retire les jetons qui ont deja ete parcouru
     * territoire == true, retire ceux qui ont ete
     * */
    public void retirerJetonCapturer(ArrayList<Instruction> infos) {
        final boolean RETIRER = true;

        for (Instruction instr : infos) {
            // Si instruction == PASS
            if (!instr.getElement().getType().equals(Constantes.PASS)) {
                // Si jeton a ete parcouru, on le retire
                if (instr.getJeton().getJetonLibMemeCouleurParcour()) {
                    //instr.getJeton().setCouleurJeton(Couleur.TRANSPARENT);
                    instr.getJeton().setDeleted(RETIRER);
                }
            }
        }
    }
    /**
     * Compte les jetons qui ont deja ete parcouru
     * territoire == true, retire ceux qui ont ete
     * retourne le nombre de points accumuler avec le territoire entourrer
     * */
    public int retirerJetonCapturerSetPts(ArrayList<Instruction> infos) {
        int pts = 0;

        for (Instruction instr : infos) {
            // Si instruction == PASS
            if (!instr.getElement().getType().equals(Constantes.PASS)) {
                // Si jeton a ete parcouru, on le retire
                if (instr.getJeton().getJetonLibMemeCouleurParcour()) {
                    instr.getJeton().setPtsCalculer(Constantes.VRAI);
                    ++pts;
                }
            }
        }
        return pts;
    }
    /**
     * Verifie dans le tableau infos s'il y a un jeton
     * (qui est encercler par des jetons de couleur oppose,
     * verifie le cas avec les murs aussi)
     * */
    public void captureToutJetonSeul(ArrayList<Instruction> infos) {
        // Get instruction 1 par 1
        for (Instruction instr : infos) {
            // Si instr != PASS
            if (!instr.getElement().getType().equals(Constantes.PASS)) {
                Jeton jetonTmp = getJeton(infos, instr.getJeton().getPosition());
                // Si JetonTmp existe / n'est pas supprimer
                if (jetonTmp != null) {
                    // Si jeton n'a pas de liberte
                    if (jetonTmp.getLibertesDuJeton().size() == Constantes.VIDE) {
                        // Si jeton n'a pas de liberte de meme couleur
                        if (jetonTmp.getJetonLibMemeCouleur().size() == Constantes.VIDE) {
                            jetonTmp.setJetonLibMemeCouleurParcour(Constantes.VRAI);
                        }
                    }
                }
            }
        }
    }
    /**
     * Verifie dans le tableau infos s'il y a un jeton
     * (qui est encercler par des jetons de couleur oppose,
     * verifie le cas avec les murs aussi)
     * */
    public void captureToutJetonSeul1(ArrayList<Instruction> infos, Joueur joueur) throws Exception {
        // Get instruction 1 par 1
        for (Instruction instr : infos) {
            // Si instr != PASS
            if (!instr.getElement().getType().equals(Constantes.PASS)) {
                Jeton jetonTmp = getJeton(infos, instr.getJeton().getPosition());
                // Si JetonTmp existe / n'est pas supprimer
                if (jetonTmp != null) {
                    // Si jeton n'a pas de liberte
                    if (jetonTmp.getLibertesDuJeton().size() == Constantes.VIDE) {
                        // Si jeton n'a pas de liberte de meme couleur
                        if (jetonTmp.getJetonLibMemeCouleur().size() == Constantes.VIDE) {
                            if (verifierCroisMemeCouleur(infos, joueur, jetonTmp.getPosition())) {
                                jetonTmp.setJetonLibMemeCouleurParcour(Constantes.VRAI);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Verifie si le jeton est bien encercler par le joueur passer en parametre
     * */
    private boolean verifierCroisMemeCouleur(ArrayList<Instruction> infos, Joueur joueur, Position position) throws Exception {
        final int DECALE = 1;
        // Doit avoir 4 jeton de meme couleur entourer
        final int CROIS_MAX = 4;
        // Compteur de jeton
        int croixMemeCouleur = 0;
        Position positionVoisin;
        Position pos = position;

        if (!Rangee.getValueDecalPos(pos.getRangee(), DECALE).equals(pos.getRangee())) {
            String voisinRangee = Rangee.getValueDecalPos(pos.getRangee(), DECALE);

            positionVoisin = new Position(pos.getColonne(), voisinRangee);
            Jeton jeton = getJeton(infos, positionVoisin);
            if (joueur.getCouleur().equals(jeton.getCouleurJeton())) ++croixMemeCouleur;
        } else ++croixMemeCouleur;
        if (!Rangee.getValueDecalPos(pos.getRangee(), -(DECALE)).equals(pos.getRangee())) {
            String voisinRangee = Rangee.getValueDecalPos(pos.getRangee(), -(DECALE));

            positionVoisin = new Position(pos.getColonne(), voisinRangee);
            Jeton jeton = getJeton(infos, positionVoisin);
            if (joueur.getCouleur().equals(jeton.getCouleurJeton())) ++croixMemeCouleur;
        } else ++croixMemeCouleur;
        if (!Colonne.getValueDecalPos(pos.getColonne(), DECALE).equals(pos.getColonne())) {
            String voisinColonne = Colonne.getValueDecalPos(pos.getColonne(), DECALE);

            positionVoisin = new Position(voisinColonne, pos.getRangee());
            Jeton jeton = getJeton(infos, positionVoisin);
            if (joueur.getCouleur().equals(jeton.getCouleurJeton())) ++croixMemeCouleur;
        } else ++croixMemeCouleur;
        if (!Colonne.getValueDecalPos(pos.getColonne(), -(DECALE)).equals(pos.getColonne())) {
            String voisinColonne = Colonne.getValueDecalPos(pos.getColonne(), -(DECALE));

            positionVoisin = new Position(voisinColonne, pos.getRangee());
            Jeton jeton = getJeton(infos, positionVoisin);
            if (joueur.getCouleur().equals(jeton.getCouleurJeton())) ++croixMemeCouleur;
        } else ++croixMemeCouleur;
        if (croixMemeCouleur == CROIS_MAX) return true;

        return false;
    }
    /**
     * Verifie si les jetons sont bien encercler par le joueur passer en parametre
     * */
    private boolean verifierEntourer(ArrayList<Jeton> tableauTerritoire, ArrayList<Instruction> infos, Joueur joueur) throws Exception {
        /*setJetonsParcouru(tableauTerritoire, Constantes.FAUX);*/
        final int DECALE = 1;

        for (Jeton j : tableauTerritoire) {
            Position positionVoisin;
            Position pos = j.getPosition();

            if (!Rangee.getValueDecalPos(pos.getRangee(), DECALE).equals(pos.getRangee())) {
                String voisinRangee = Rangee.getValueDecalPos(pos.getRangee(), DECALE);

                positionVoisin = new Position(pos.getColonne(), voisinRangee);
                Jeton jeton = getJeton(infos, positionVoisin);
                if (!joueur.getCouleur().equals(jeton.getCouleurJeton()) && !jeton.getCouleurJeton().equals(Couleur.TRANSPARENT)) return false;
            }
            if (!Rangee.getValueDecalPos(pos.getRangee(), -(DECALE)).equals(pos.getRangee())) {
                String voisinRangee = Rangee.getValueDecalPos(pos.getRangee(), -(DECALE));

                positionVoisin = new Position(pos.getColonne(), voisinRangee);
                Jeton jeton = getJeton(infos, positionVoisin);
                if (!joueur.getCouleur().equals(jeton.getCouleurJeton()) && !jeton.getCouleurJeton().equals(Couleur.TRANSPARENT)) return false;
            }
            if (!Colonne.getValueDecalPos(pos.getColonne(), DECALE).equals(pos.getColonne())) {
                String voisinColonne = Colonne.getValueDecalPos(pos.getColonne(), DECALE);

                positionVoisin = new Position(voisinColonne, pos.getRangee());
                Jeton jeton = getJeton(infos, positionVoisin);
                if (!joueur.getCouleur().equals(jeton.getCouleurJeton()) && !jeton.getCouleurJeton().equals(Couleur.TRANSPARENT)) return false;
            }
            if (!Colonne.getValueDecalPos(pos.getColonne(), -(DECALE)).equals(pos.getColonne())) {
                String voisinColonne = Colonne.getValueDecalPos(pos.getColonne(), -(DECALE));

                positionVoisin = new Position(voisinColonne, pos.getRangee());
                Jeton jeton = getJeton(infos, positionVoisin);
                if (!joueur.getCouleur().equals(jeton.getCouleurJeton()) && !jeton.getCouleurJeton().equals(Couleur.TRANSPARENT)) return false;
            }
        }

        return true;
    }


    public int compterPoints(Joueur joueur, ArrayList<Instruction> infos) throws Exception {
        int points = compterNbJetonsRestant(infos, joueur.getCouleur());
        territoireSansJeton = creerTerritoireVide(infos);
        ArrayList<Instruction> jeuComplet = mixTables(territoireSansJeton, infos);
        setLibertes(jeuComplet);

        for (Instruction instr : jeuComplet) {
            if (!instr.getElement().getType().equals(Constantes.PASS)) {
                Jeton jetonAnalyser = getJeton(jeuComplet, instr.getJeton().getPosition());
                // Set tout les jetonsParcouru = false
                setJetonsParcouru(jeuComplet, Constantes.FAUX);
                // Creer une table des jetons qui ont ete encercler
                ArrayList<Jeton> tableTerritoire = creerTableauTerritoire(jeuComplet, jetonAnalyser);
                checkSiPointsDejaAjouter(tableTerritoire, jeuComplet);
                // Fait mis a jour sur le tableau infos
                setTerritoireDsInfos(jeuComplet, tableTerritoire);
                // Cas ou 3 extremites trouver et tout les jetons dans le territoire n'ont pas de liberte
                if (nbExtremite(tableTerritoire) == Constantes.QUATRE_COINS - 1 && checkSiTerritoireAucuneLib(tableTerritoire)) {
                    // Set tout les jetonsParcouru = false
                    //territoire.setJetonsParcouru(infos, false);
                    captureTerritoire1(jeuComplet, jetonAnalyser, joueur);
                    points += retirerJetonCapturerSetPts(jeuComplet);
                }
                // Cas ou 4 extremites trouver
                else if (nbExtremite(tableTerritoire) == Constantes.QUATRE_COINS) {
                    if (verifierEntourer(tableTerritoire, jeuComplet, joueur))
                        points += retirerJetonCapturerSetPts(jeuComplet);
                }
            }
            // Set tout les jetonsParcouru = false
            setJetonsParcouru(jeuComplet, Constantes.FAUX);
        }

        captureToutJetonSeul1(jeuComplet, joueur);
        points += retirerJetonCapturerSetPts(jeuComplet);
        // Set tout les jetonsParcouru = false
        setJetonsParcouru(jeuComplet, Constantes.FAUX);
        setJetonsParcouru(infos, false);
        return points;
    }

    private void checkSiPointsDejaAjouter(ArrayList<Jeton> tableTerritoire, ArrayList<Instruction> jeuComplet) {
        ArrayList<Jeton> territoireTmp = new ArrayList<>();
        territoireTmp.addAll(tableTerritoire);
        for (Jeton jeton : territoireTmp) {
            Jeton jetonAnalyser = getJeton(jeuComplet, jeton.getPosition());
            if (jetonAnalyser.isPtsCalculer()) {
                tableTerritoire.remove(jeton);
            }
        }
    }

    /**
     * Fusionner 2 tableaux ensemble (sans duplicas, cas K.O.)
     * */
    private ArrayList<Instruction> mixTables(ArrayList<Instruction> territoireSansJeton, ArrayList<Instruction> infos) {
        ArrayList<Instruction> tab = new ArrayList<>();

        // territoireSansJeton
        tab.addAll(territoireSansJeton);


        // infos
        for (Instruction instr : infos) {
            if (!instr.getElement().getType().equals(Constantes.PASS)) {
                if (getJeton(tab, instr.getJeton().getPosition()) == null) {
                    tab.add(instr);
                }
            }
        }


        return tab;
    }

    /**
     * Set les libertes de territoireSansJeton
     * RAPPEL liberte pour caseVide contient
     * les jetons de couleur noir/blanc
     * */
    private void setLibertes(ArrayList<Instruction> jeuComplet) throws Exception {
        for (Instruction caseVide : jeuComplet) {
            caseVide.getJeton().addLiberteJetonMemeCouleur(jeuComplet);
            caseVide.getJeton().addLiberteJeton(jeuComplet);
        }
    }

    /**
     * Compte le nombre de Jeton !isDeleted()
     * */
    private int compterNbJetonsRestant(ArrayList<Instruction> infos, Couleur couleur) {
        int pts = 0;

        for (Instruction instr : infos) {
            // Si n'est pas l'instruction PASS
            if (!instr.getElement().getType().equals(Constantes.PASS)) {
                // Si Jeton n'est pas supprimer
                if (!instr.getJeton().isDeleted()) {
                    // Si Jeton est de la meme couleur passer en parametre
                    if (instr.getJeton().getCouleurJeton().equals(couleur)) {
                        instr.getJeton().setPtsCalculer(Constantes.VRAI);
                        ++pts;
                    }
                }
            }
        }
        return pts;
    }

    public ArrayList<Instruction> creerTerritoireVide(ArrayList<Instruction> infos) throws Exception {
        ArrayList<Instruction> territoireVide = new ArrayList<>();

        for (int col = 0; col < Colonne.values().length; ++col) {
            for (int rang = 0; rang < Rangee.values().length; ++rang) {
                // Conversion en position
                Colonne colonne = Colonne.intAColonne(col);
                Rangee rangee = Rangee.intARangee(rang);
                Position position = new Position(colonne.toString(), rangee.toString());

                // Jeton a ajouter dans le tableau
                Instruction jetonVide = new Instruction(colonne.toString() + rangee.toString());

                // Jeton verifier s'il existe
                Jeton jetonAnalyser = getJeton(infos, position);

                // Si n'existe pas
                if (jetonAnalyser == null) {
                    territoireVide.add(jetonVide);
                } else {
                    // Si supprimer ou de couleur transparent
                    if (jetonAnalyser.isDeleted() || jetonAnalyser.getCouleurJeton().equals(Couleur.TRANSPARENT)) {
                        territoireVide.add(jetonVide);
                    }
                }
            }
        }

        return territoireVide;
    }


    /**
     * Creer un tableau territoire avec les voisins du Jeton
     * passe en parametre et par la suite avec les voisins
     * du voisin du Jeton, etc..
     * */
    public ArrayList<Jeton> creerTableauTerritoireVide(ArrayList<Instruction> infos, Jeton jeton) {
        Jeton jetonTmp;
        ArrayList<Jeton> territoire = new ArrayList<>();
        final boolean PARCOURU = true;

        if (!jeton.getJetonLibMemeCouleurParcour()) {
            jetonTmp = getJeton(infos, jeton.getPosition());
            // Ajout du Jeton
            territoire.add(jetonTmp);

            // Set Jeton passe en parametre a PARCOURU
            jeton.setJetonLibMemeCouleurParcour(PARCOURU);
        }

        // Si Jeton contient au moins 1 voisin de meme couleur
        if (jeton.getJetonLibMemeCouleur().size() > Constantes.VIDE) {
            for (int i = 0; i < jeton.getJetonLibMemeCouleur().size(); ++i) {
                jetonTmp = getJeton(infos, jeton.getJetonLibMemeCouleur().get(i));
                // Si Jeton existe
                if (jetonTmp != null) {
                    // Si Jeton n'est pas supprimer
                    if (!jetonTmp.isDeleted()) {
                        // Si le tableau territoire ne contient pas deja ce Jeton et n'a pas deja ete parcouru
                        if (!territoire.contains(jetonTmp) && !jetonTmp.getJetonLibMemeCouleurParcour()) {
                            // Ajoute dans tableau
                            territoire.add(jetonTmp);
                            // Set qu'il a deja etait parcouru
                            jetonTmp.setJetonLibMemeCouleurParcour(PARCOURU);

                            territoire.addAll(creerTableauTerritoire(infos, jetonTmp));
                        }
                    }
                }
            }
        }

        this.territoire = territoire;
        return territoire;
    }
}
