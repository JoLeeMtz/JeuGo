import java.util.ArrayList;
import java.util.Scanner;

public class JeuGo implements MiseAJourTerritoire, RecuperationJeton, MisAJourJeton {
    private String utilisateur;
    private Affichage aff;
    private boolean finJeuTest = false;
    public JeuGo(Affichage aff) {
        this.aff = aff;
    }

    public void startGame(String args) throws Exception {

        // Territoire conquis (les jetons qui se font entourrer)
        Territoire territoire = new Territoire();
        // Sauvegarde des instructions
        ArrayList<Instruction> infos = new ArrayList<>();
        // Joueurs ou seront storer les points
        Joueur joueur1 = new Joueur(Couleur.NOIR);
        Joueur joueur2 = new Joueur(Couleur.BLANC);
        // Instruction courant en format String
        String instruction = "";
        // Instruction precedente
        String instructionPrec = "";
        int nbTour = 0;
        // Indique si la partie est finie
        boolean finJeu = false;
        // Fichier pour faire les tests
        LireFichier fichier = new LireFichier(args);
        // Lire fichier
        fichier.lecture();

        // Main du jeuGo
        System.out.println(args);
        for (int i = 0; i < fichier.getLigne().length(); ++i) {
            // Si n'est pas le premier element
            if (i > 0) {
                // get instruction precedente
                instructionPrec = fichier.getElement(i - instruction.length() - 1);
            }
            // Set la premiere instruction lu
            instruction = fichier.getElement(i);
            // Instation de l'instruction courant
            Instruction instructionCourant = new Instruction(instruction, i);
            // Ajout de l'instruction dans le tableau
            infos.add(instructionCourant);

            // Si (instruction precedent et l'instruction courant) == PASS
            if (instructionPrec.equals(Constantes.PASS)
                    && instructionCourant.getElement().getType().equals(Constantes.PASS)) {
                finJeu = true;
            }

            // Regle du KO
            regleKO(infos);

            // Si l'instructionCourant != PASS
            if (!instructionCourant.getElement().getType().equals(Constantes.PASS)) {
                if (!misAJourTableauJeu(infos, instructionCourant, territoire)) {
                    throw new Exception("Position invalide, déjà occupé");
                }
            }

            // Passe a la prochaine instruction
            i += instruction.length();
            if (i == -1) i = fichier.getLigne().length();

            nbTour = i;
        }

        // FinJeu == 2x suite PASS
        if (finJeu) {
            joueur1.setPoints(territoire.compterPoints(joueur1, infos));
            joueur2.setPoints(territoire.compterPoints(joueur2, infos));

            aff.affichagePoints(joueur1, joueur2);
            System.out.println(Constantes.FIN_JEU);
            System.exit(Constantes.FIN_JEU_NB);
        }

       else {

            // Lecture sur console
            inputLecture( infos, territoire, nbTour );

            joueur1.setPoints( territoire.compterPoints( joueur1, infos ) );
            joueur2.setPoints( territoire.compterPoints( joueur2, infos ) );
            aff.affichagePoints( joueur1, joueur2 );
            System.out.println(Constantes.FIN_JEU);
        }

    }

    private void regleKO(ArrayList<Instruction> infos) throws Exception {
        for (Instruction instr : infos) {
            if (!instr.getElement().getType().equals(Constantes.PASS)) {
                if (!instr.getJeton().isDeleted()) {
                    Jeton jetonAnalyser = getDeletedJeton(infos, instr.getJeton().getPosition());

                    if (jetonAnalyser != null) {
                        if (jetonAnalyser.isDeleted()) {
                            if (jetonAnalyser.getCouleurJeton().equals(instr.getJeton().getCouleurJeton())) {
                                throw new Exception("\n Regle de KO appliquer pour l'instruction : "
                                        + "\n Jeton : " + instr.getJeton().getPosition().toString()
                                        + "\n Couleur : " + instr.getJeton().getCouleurJeton());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Fait une mis a jour des libertes de jetonAnalyser
     * */
    @Override
    public void misAJourJeton(ArrayList<Instruction> infos, Jeton jetonAnalyser) throws Exception {
        jetonAnalyser.addLiberteJeton(infos);
        jetonAnalyser.addLiberteJetonMemeCouleur(infos);
    }
    /**
     * Fait une mis a jour des Jetons, verifie si le Jeton
     * passe en parametre n'affecte pa une capture de territoire
     * */
    @Override
    public void misAJourTerritoire(ArrayList<Instruction> infos, Jeton jetonAnalyser, Territoire territoire) throws Exception {
        // Mis a jour du jeton
        misAJourJeton(infos, jetonAnalyser);

        // Si capture avec Jeton passe en parametre
        if (territoire.captureTerritoire(infos, jetonAnalyser)) {
            // Retirer les Jetons encercler
            territoire.retirerJetonCapturer(infos);
        } else {
            // Set tout les jetonsParcouru = false
            territoire.setJetonsParcouru(infos, Constantes.FAUX);
            // Creer une table des jetons qui ont ete encercler
            ArrayList<Jeton> tableTerritoire = territoire.creerTableauTerritoire(infos, jetonAnalyser);
            // Fait mis a jour sur le tableau infos
            territoire.setTerritoireDsInfos(infos, tableTerritoire);

            // Cas ou 3 extremites trouver et tout les jetons dans le territoire n'ont pas de liberte
            if (territoire.nbExtremite(tableTerritoire) == Constantes.QUATRE_COINS-1 && territoire.checkSiTerritoireAucuneLib(tableTerritoire)) {
                // Set tout les jetonsParcouru = false
                //territoire.setJetonsParcouru(infos, false);
                territoire.captureTerritoire(infos, jetonAnalyser);
                territoire.retirerJetonCapturer(infos);
            }
            // Cas ou 4 extremites trouver
            else if (territoire.nbExtremite(tableTerritoire) == Constantes.QUATRE_COINS) {
                territoire.retirerJetonCapturer(infos);
            }
        }
        // Set tout les jetonsParcouru = false
        territoire.setJetonsParcouru(infos, Constantes.FAUX);
    }

    /**
     * S'occupe de faire l'affichage sur la console
     * du tableau de jeu avec les informations des jetons
     * qui ont ete inserer
     * */
    public void affichage(ArrayList<Instruction> infos) {
        aff.affichageTableau( infos );
        aff.affichageLegende( infos );
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
     * Ecrire via JUnit
     * */
    public void inputLectureJUnit(ArrayList<Instruction> infos, Territoire territoire, int nbTour) throws Exception {
        // Joueurs ou seront storer les points
        Joueur joueur1 = new Joueur(Couleur.NOIR);
        Joueur joueur2 = new Joueur(Couleur.BLANC);
        // Instruction courant en format String
        String instruction = "";
        // Lecture précédente d'un joueur
        String instructionPrec = "";
        // Indique si la partie est finie après la lecture du fichier par les joueurs
        boolean finJeu = false;
        // Apres la fin du fichier, on laisse l'utilisateur jouer en entrant des valeurs qu'on va lire
        ArrayList<String> lecturePrecTab = new ArrayList<>();

        // Fichier creer pour l'usager
        LireFichier fichierUtilisateur = new LireFichier("utilisateur");
        utilisateur = utilisateur.toUpperCase();
        // Set ligne a analyser
        fichierUtilisateur.setLigne(utilisateur);


        for (int i = 0; i < fichierUtilisateur.getLigne().length(); ++i) {
            ++nbTour;
            aff.affichageAQuiLeTour(nbTour);
            // Si n'est pas le premier element
            if (i > 0) {
                // get instruction precedente
                instructionPrec = fichierUtilisateur.getElement(i - instruction.length() - 1);
            }
            // Set la premiere instruction lu
            instruction = fichierUtilisateur.getElement(i);
            // Instation de l'instruction courant
            Instruction instructionCourant = new Instruction(instruction, nbTour+i);

            // Si (instruction precedent et l'instruction courant) == PASS
            if (instructionPrec.equals(Constantes.PASS)
                    && instructionCourant.getElement().getType().equals(Constantes.PASS)) {
                finJeu = true;
                break;
            }


            // Si l'instructionCourant != PASS
            if (!instructionCourant.getElement().getType().equals(Constantes.PASS)) {
                // Ajout de l'instruction dans le tableau
                infos.add(instructionCourant);

                // Regle du KO
                regleKO(infos);

                // Jeton deja existant
                if (!misAJourTableauJeu(infos, instructionCourant, territoire)) {
                    --nbTour;
                    infos.remove(instructionCourant);

                    System.out.println("Position invalide, déjà occupé");
                }
            }

            // Passe a la prochaine instuction
            i += instruction.length();
            if (i == -1) i = fichierUtilisateur.getLigne().length();

            nbTour = i;
        }

        // FinJeu == 2x suite PASS
        if (finJeu) {
            affiScore(infos,   territoire, joueur1 , joueur2);
            affiFin();

        }
    }

    /**
     * Ecrire sur la console manuellement
     * */
    public void inputLecture(ArrayList<Instruction> infos, Territoire territoire, int nbTour) throws Exception {
        // Lecture précédente d'un joueur
        String lecturePrec = "";
        // Indique si la partie est finie après la lecture du fichier par les joueurs
        boolean finJeuJoueur = false;
        // Apres la fin du fichier, on laisse l'utilisateur jouer en entrant des valeurs qu'on va lire
        ArrayList<String> lecturePrecTab = new ArrayList<>();
        int i = 0;

        do {
            ++nbTour;
            aff.affichageAQuiLeTour(nbTour);
            // Creer tampon lecture
            Scanner sc = new Scanner( System.in );

            // Lis l'input
            String instruction = sc.nextLine();
            instruction = instruction.toUpperCase();
            lecturePrecTab.add( instruction );

            // lecturePrec contient toujours 1 instruction, le dernier
            if(i > 0) {
                lecturePrec = lecturePrecTab.get( i - 1 );

                lecturePrecTab.remove( i - 1);
                --i;
            }

            if(instruction.equals( Constantes.PASS ) && instruction.equals( lecturePrec ))
                finJeuJoueur = true;

            if (!instruction.equals(Constantes.PASS)) {
                if (!finJeuJoueur) {
                    Instruction instructionCourant = new Instruction(instruction, nbTour);
                    // Ajout de l'instruction dans le tableau
                    infos.add(instructionCourant);

                    // Regle du KO
                    regleKO(infos);

                    // Jeton deja existant
                    if (!misAJourTableauJeu(infos, instructionCourant, territoire)) {
                        --nbTour;
                        infos.remove(instructionCourant);

                        System.out.println("Position invalide, déjà occupé");
                    }
                }
            }

            ++i;

        } while(!finJeuJoueur);
    }

    /**
     * Fait une mise a jour des jetons, liberte du jeton et les territoires
     * */
    public boolean misAJourTableauJeu(ArrayList<Instruction> infos, Instruction instructionCourant, Territoire territoire) throws Exception {
        // Si instructionCourant != PASS
        if (!instructionCourant.getElement().getType().equals(Constantes.PASS)) {
            boolean dejaCreer = false;
            // Cherche si Jeton deja existant
            for (int i = 0; i < infos.size()-1; ++i) {
                // Si infos != PASS
                if (!infos.get(i).getElement().getType().equals(Constantes.PASS)) {
                    // Si Jeton !isDeleted
                    if (!infos.get(i).getJeton().isDeleted()) {
                        String instrCol = infos.get(i).getJeton().getPosition().getColonne();
                        String instrRan = infos.get(i).getJeton().getPosition().getRangee();
                        Position instrPos = new Position(instrCol, instrRan);

                        // Si Jeton est deja dans tableau & n'est pas supprimer
                        if (instrPos.equals(instructionCourant.getJeton().getPosition())) {
                            dejaCreer = true;
                        }
                    }
                }
            }
            if (!dejaCreer) {
                // Set liberte du jeton qui vient d'etre ajouter
                Jeton jetonAjouter = getJeton(infos, instructionCourant.getJeton().getPosition());
                // Mis a jour du jeton
                misAJourJeton(infos, jetonAjouter);

                // Mis a jour libertes du jeton
                for (int j = 0; j < infos.size(); ++j) {
                    if (!infos.get(j).getElement().getType().equals(Constantes.PASS)) {
                        Jeton jetonAnalyser = infos.get(j).getJeton();

                        // Si Jeton n'est pas supprimer
                        if (!jetonAnalyser.isDeleted()) {
                            // Verifie si nouveau territoire pris par un Jeton
                            misAJourTerritoire(infos, jetonAnalyser, territoire);
                        }
                    }
                }

                territoire.captureToutJetonSeul(infos);
                territoire.retirerJetonCapturer(infos);

                // Appel fonction d'affichage
                affichage(infos);
                return true;
            }
        }

        return false;
    }




    public ArrayList<Instruction> startGameTest(String args, String utilisateur) throws Exception {
        this.utilisateur = utilisateur;
        // Territoire conquis (les jetons qui se font entourrer)
        Territoire territoire = new Territoire();
        // Sauvegarde des instructions
        ArrayList<Instruction> infos = new ArrayList<>();
        // Joueurs ou seront storer les points
        Joueur joueur1 = new Joueur(Couleur.NOIR);
        Joueur joueur2 = new Joueur(Couleur.BLANC);
        // Instruction courant en format String
        String instruction = "";
        // Instruction precedente
        String instructionPrec = "";
        int nbTour = 0;
        // Indique si la partie est finie
        boolean finJeu = false;
        // Fichier pour faire les tests
        LireFichier fichier = new LireFichier(args);
        // Lire fichier
        fichier.lecture();

        // Main du jeuGo
        System.out.println(args);
        for (int i = 0; i < fichier.getLigne().length(); ++i) {
            // Si n'est pas le premier element
            if (i > 0) {
                // get instruction precedente
                instructionPrec = fichier.getElement(i - instruction.length() - 1);
            }
            // Set la premiere instruction lu
            instruction = fichier.getElement(i);
            // Instation de l'instruction courant
            Instruction instructionCourant = new Instruction(instruction, i);
            // Ajout de l'instruction dans le tableau
            infos.add(instructionCourant);

            // Si (instruction precedent et l'instruction courant) == PASS
            if (instructionPrec.equals(Constantes.PASS)
                    && instructionCourant.getElement().getType().equals(Constantes.PASS)) {
                finJeu = true;
            }

            // Regle du KO
            regleKO(infos);

            // Si l'instructionCourant != PASS
            if (!instructionCourant.getElement().getType().equals(Constantes.PASS)) {
                if (!misAJourTableauJeu(infos, instructionCourant, territoire)) {
                    throw new Exception("Position invalide, déjà occupé");
                }
            }

            // Passe a la prochaine instuction
            i += instruction.length();
            if (i == -1) i = fichier.getLigne().length();

            nbTour = i;
        }
        finJeuTest = finJeu;
        // FinJeu == 2x suite PASS
        if (finJeu) {

            affiScore(infos,   territoire, joueur1 , joueur2);
            affiFin();

            //pour tester , faut pas que le prog exit pour que je teste la fin du jeu
            //System.exit(Constantes.FIN_JEU_NB);
        }

        // Utilisateur ne veut pas continuer
        else if (!utilisateur.contains(Constantes.PASS + " " + Constantes.PASS)) {
            // Lecture sur console
            inputLectureJUnit( infos, territoire, nbTour );
            affiScore(infos, territoire, joueur1 , joueur2);

        } else {
            affiScore(infos, territoire, joueur1 , joueur2);
            finJeuTest = true;
        }

        return infos;
    }

    public boolean getPartieFiniFichierTest(){
        return finJeuTest;
    }

    public void affiScore(ArrayList<Instruction> infos,  Territoire territoire, Joueur joueur1 ,Joueur joueur2) throws Exception {
        joueur1.setPoints( territoire.compterPoints( joueur1, infos ) );
        joueur2.setPoints( territoire.compterPoints( joueur2, infos ) );

        aff.affichagePoints( joueur1, joueur2 );
    }
    public boolean affiFin(){

            /*System.out.println(Constantes.FIN_JEU);*/

        return finJeuTest;
    }
    public boolean getPartieFiniConsoleTest(){
        return true;
    }
}
