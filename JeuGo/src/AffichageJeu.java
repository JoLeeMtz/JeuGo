import java.util.ArrayList;

public class AffichageJeu implements Affichage {

    @Override
    public void affichageTableau(ArrayList<Instruction> infos) {
        boolean jetonAfficher = false;
        for (int rangee = Affichage.MAX_RANGEE+1; rangee >= Affichage.MIN_RANGEE; --rangee) {
            // Affiche nombre de rangee
            if (rangee <= Affichage.MAX_RANGEE) {
                System.out.println(Affichage.ESPACE + Affichage.ESPACE + Affichage.SEP_HORI);
                System.out.print(rangee + Affichage.ESPACE);
            }
            // Affiche espace entre les colonnes (A, B, C, D, E, F, G, H, J)
            else {
                System.out.print(Affichage.ESPACE + Affichage.ESPACE + Affichage.ESPACE);
            }

            for (int colonne = Affichage.MIN_COLONNE; colonne <= Affichage.MAX_COLONNE; ++colonne) {
                Colonne col = Colonne.intAColonne(colonne);
                Rangee rang = Rangee.intARangee(rangee);
                if (rangee > Affichage.MAX_RANGEE) {
                    // Affiche nombre de colonnes (A, B, C, D, E, F, G, H, J)
                    System.out.print(Affichage.ESPACE + Affichage.ESPACE + col + Affichage.ESPACE);
                } else {
                    // Affiche permiere barre vertical
                    System.out.print(Affichage.SEP_VERT);
                    for ( Instruction info : infos ) {
                        if ( info.getElement().getType().equals( Affichage.JETON ) ) {
                            if ( info.getJeton().isDeleted() == false ) {
                                // Verifie la colonne
                                if ( info.getJeton().getPosition().getColonne().equals( col.getValue() ) ) {
                                    // Verifie la rangee
                                    if ( info.getJeton().getPosition().getRangee().equals( rang.getValue() ) ) {
                                        // Affiche le jeton noir
                                        if ( info.getJeton().getCouleurJeton().equals( Couleur.NOIR ) ) {
                                            jetonAfficher = true;
                                            System.out.print( Affichage.NOIR );
                                        }
                                        // Affiche le jeton blanc
                                        else if ( info.getJeton().getCouleurJeton().equals( Couleur.BLANC ) ) {
                                            jetonAfficher = true;
                                            System.out.print( Affichage.BLANC );
                                        }
                                    }
                                }
                            }
                     }
                    }
                    // Affiche case vide
                    if (!jetonAfficher) {
                        System.out.print(Affichage.ESPACE);
                    } else {
                        jetonAfficher = false;
                    }
                }

                // Affiche des cases egaux (vide, avec jeton noir et blanc)
                System.out.print(Affichage.ESPACE + Affichage.ESPACE);
            }
            // Passe a la prochaine rangee
            if (rangee > Affichage.MAX_RANGEE) System.out.println();
            // Affiche un le separateur vertical avant de passee a la prochaine rangee
            else System.out.println(Affichage.SEP_VERT);
        }
        // Affiche la derniere separation horizontal
        System.out.println(Affichage.ESPACE + Affichage.ESPACE + Affichage.SEP_HORI);
    }
    @Override
    public void affichageLegende(ArrayList<Instruction> infos) {
        System.out.println(Affichage.SEP_HORI);
        System.out.println("Jeton noir : " + Affichage.NOIR);
        System.out.println("Jeton blanc : " + Affichage.BLANC);
        System.out.println(Affichage.SEP_HORI);

        for (Instruction instr : infos) {
            if (instr.getElement().getType().equals(Affichage.JETON)) {
                System.out.println(instr.getElement().toString());
            }
        }
        System.out.println(Affichage.SEP_HORI);
    }
    @Override
    public void affichageAQuiLeTour(int nbTour) {
        if ((nbTour % 2) == 0) {
            System.out.println( "Veuillez jouer Joueur Noir (X) :" );
        } else {
            System.out.println( "Veuillez jouer Joueur Blanc (O) :" );
        }
    }
    @Override
    public void affichagePoints(Joueur jNoir, Joueur jBlanc) {
        System.out.println();
        System.out.println("Score : ");
        System.out.println("Joueur 1 (avec jeton X) : " + jNoir.getPoints());
        System.out.println("Joueur 2 (avec jeton O) : " + jBlanc.getPoints());

        if (jNoir.getPoints() > jBlanc.getPoints()) {
            System.out.println("Le gagnant est le Joueur 1 (Noir X) !");
        } else if (jNoir.getPoints() < jBlanc.getPoints()) {
            System.out.println("Le gagnant est le Joueur 2 (Blanc O) !");
        } else {
            System.out.println("Aucun gagnant, égalité !");
        }
    }
}
