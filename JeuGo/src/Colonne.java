public enum Colonne {
    A( "A"),
    B( "B"),
    C( "C"),
    D( "D"),
    E( "E"),
    F( "F"),
    G("G"),
    H( "H"),
    J( "J");

    private final String value;

    Colonne(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    static public Colonne intAColonne(int colonne) {
        if (colonne == 1)
            return Colonne.A;
        else if (colonne == 2)
            return Colonne.B;
        else if (colonne == 3)
            return Colonne.C;
        else if (colonne == 4)
            return Colonne.D;
        else if (colonne == 5)
            return Colonne.E;
        else if (colonne == 6)
            return Colonne.F;
        else if (colonne == 7)
            return Colonne.G;
        else if (colonne == 8)
            return Colonne.H;

        return Colonne.J;
    }

    static public String getValueDecalPos(String valueCurrent ,int decalage){
        for(int i = 0; i < Colonne.values().length; ++i){
            if(Colonne.values()[i].getValue().equals( valueCurrent )){
                if ((i+decalage) < Colonne.values().length && (i+decalage) >= 0) {
                    return Colonne.values()[i + decalage].getValue();
                }
            }
        }
        return valueCurrent;
    }

    @Override
    public String toString() {
        return value;
    }
}
