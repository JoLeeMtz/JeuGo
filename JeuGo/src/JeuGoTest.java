import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class JeuGoTest {
    /**
     * Test passé (pour comptage de points) :
     * 1.txt
     * 2.txt
     * 3.txt
     * 5.txt
     * 0 : 21(jeton), 2, 31, 54(resultat final)
     * X : 18(jeton), 7, 25(resultat final)
     * */
    final String FIN_JEU = "PASS PASS";
    JeuGo jeu;

    @BeforeEach
    void setUp() {
        Affichage aff = new Affichage() {
            @Override
            public void affichageTableau( ArrayList<Instruction> infos ) {
                AffichageJeu affJeu2 = new AffichageJeu();
                affJeu2.affichageTableau(infos);
            }

            @Override
            public void affichageLegende( ArrayList<Instruction> infos ) {
                AffichageJeu affJeu2 = new AffichageJeu();
                affJeu2.affichageLegende(infos);
            }

            @Override
            public void affichageAQuiLeTour(int nbTour) {
                AffichageJeu affJeu2 = new AffichageJeu();
                affJeu2.affichageAQuiLeTour(nbTour);
            }

            @Override
            public void affichagePoints(Joueur jNoir, Joueur jBlanc) {
                AffichageJeu affJeu2 = new AffichageJeu();
                affJeu2.affichagePoints(jNoir, jBlanc);
            }
        };
        jeu = new JeuGo(aff);
    }

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    @Before
    public void setStreams() {
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }
    @After
    public void restoreInitialStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }


    @Test
    void testKO() {
        String messageExpected = "\n Regle de KO appliquer pour l'instruction : "
                + "\n Jeton : F5"
                + "\n Couleur : NOIR";

        Throwable exception = assertThrows(Exception.class, () -> {
            jeu.startGameTest("4.txt", FIN_JEU);
        });

        assertEquals(exception.getMessage(), messageExpected);
    }

    @Test
    void testKOEntrerUsager() {
        String messageExpected = "\n Regle de KO appliquer pour l'instruction : "
                + "\n Jeton : C7"
                + "\n Couleur : NOIR";
        Throwable exception = assertThrows(Exception.class, () -> {
            jeu.startGameTest("6.txt", "C7");
        });

        assertEquals(exception.getMessage(), messageExpected);
    }

    @Test
    void testEntrerUsager() throws Exception {
        jeu.startGameTest("3_2.txt", "C9 A2 A1 PASS PASS");
        boolean val = jeu.affiFin();
        assertEquals(true, val);
    }

    @Test
    void testScoreNoir() throws Exception {
        ArrayList<Instruction> infos = jeu.startGameTest( "3.txt", "C9" );
        Joueur joueur = new Joueur(Couleur.NOIR);
        Territoire terr = new Territoire();
        int points = terr.compterPoints(joueur, infos);
        //faut régler le score 1st
        assertEquals( 3, points );
    }

    @Test
    void testScoreNoir2() throws Exception {
        ArrayList<Instruction> infos = new ArrayList<>();
        infos = jeu.startGameTest( "test.txt", "G2"  );
        Joueur joueur = new Joueur(Couleur.NOIR);
        Territoire terr = new Territoire();
        int points = terr.compterPoints(joueur, infos);

        //faut régler le score 1st
        assertEquals( 3, points );
    }

    @Test
    void testScoreBlanc() throws Exception {
        ArrayList<Instruction> infos = jeu.startGameTest( "3.txt", "c9" );
        Joueur joueur = new Joueur(Couleur.BLANC);
        Territoire terr = new Territoire();
        int points = terr.compterPoints(joueur, infos);

        //faut régler le score 1st
        assertEquals( 5, points );
    }

    @Test
    void testPartieFini() throws Exception {

        jeu.startGameTest( "3.txt", null );
        boolean expected = jeu.getPartieFiniFichierTest();

        assertTrue( expected );
    }


    @Test
    void testCoordonneHorsTab1() {
        String messageExpected = "Position recu est invalide!";
        Throwable exception = assertThrows(Exception.class, () -> {
            new Instruction( "J10" );
        });

        assertEquals(exception.getMessage(), messageExpected);
    }


    @Test
    void testCoordonneHorsTab2() {
        String messageExpected = "Position recu est invalide!";
        Throwable exception = assertThrows(Exception.class, () -> {
            new Instruction( "A0" );
        });

        assertEquals(exception.getMessage(), messageExpected);
    }


    @Test
    void jeuComplet() throws Exception {
        jeu.startGameTest( "test3.txt", null );
        boolean expected = jeu.getPartieFiniFichierTest();

        assertTrue( expected );
    }
}