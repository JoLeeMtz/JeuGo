public interface CreerInstruction {
    // Permet de creer l'instruction
    void sauvegarderInstruction(String element, int numTour) throws Exception;
    // Retourne le resultat de l'instruction (Jeton/Pass)
    TypeInstruction getElement();
}
