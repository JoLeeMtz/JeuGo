public class Instruction implements CreerInstruction{
    // Une instruction est soit un PASS ou un JETON
    private final String PASS = "PASS";
    private final String JETON = "JETON";
    private TypeInstruction element;

    @Override
    public void sauvegarderInstruction(String element, int numTour) throws Exception {
        final int PREMIER = 0;
        final int DEUXIEME = 1;
        String rangee = element.substring(DEUXIEME);
        Position posYX;

        // Instruction PASS
        if (element.equals(PASS)) {
            Pass passe = new Pass();
            passe.setType(PASS);
            this.element = passe;
        }
        // Instructions Positionnement (ex: 2A)
        else {
            Jeton jeton;
            posYX = new Position(String.valueOf(element.charAt(PREMIER)), rangee);

            if ((numTour % 2) == 0) {
                jeton = new Jeton(Couleur.NOIR, posYX);
            } else {
                jeton = new Jeton(Couleur.BLANC, posYX);
            }
            jeton.setType(JETON);
            this.element = jeton;
        }
    }

    public Instruction(String element) throws Exception {
        final int PREMIER = 0;
        final int DEUXIEME = 1;
        String rangee = element.substring(DEUXIEME);

        Position posYX = new Position(String.valueOf(element.charAt(PREMIER)), rangee);
        Jeton jeton = new Jeton(Couleur.TRANSPARENT, posYX);

        jeton.setType(JETON);
        this.element = jeton;
    }
    public Instruction(String elem, int numTour) throws Exception {
        sauvegarderInstruction(elem, numTour);
    }

    @Override
    public TypeInstruction getElement() {
        return this.element;
    }

    public Jeton getJeton() {
        return (Jeton) element;
    }
}
