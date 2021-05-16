public enum Couleur {
    NOIR( "NOIR"),
    BLANC( "BLANC"),
    TRANSPARENT( "TRANSPARENT");

    private final String value;

    Couleur(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
