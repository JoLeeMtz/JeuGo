public enum Rangee {
    UN("1"),
    DEUX("2"),
    TROIS("3"),
    QUATRE("4"),
    CINQ("5"),
    SIX("6"),
    SEPT("7"),
    HUIT("8"),
    NEUF("9");
    private final String value;

    Rangee(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    static public Rangee intARangee(int rangee) {
        if (rangee == 1)
            return Rangee.UN;
        else if (rangee == 2)
            return Rangee.DEUX;
        else if (rangee == 3)
            return Rangee.TROIS;
        else if (rangee == 4)
            return Rangee.QUATRE;
        else if (rangee == 5)
            return Rangee.CINQ;
        else if (rangee == 6)
            return Rangee.SIX;
        else if (rangee == 7)
            return Rangee.SEPT;
        else if (rangee == 8)
            return Rangee.HUIT;

        return Rangee.NEUF;
    }

    /**
     * Decale la position de rangee
     * Si le decalage va plus grand que Rangee.values().length ou plus petit que 0 retourne le meme valueCurrent
     * */
    static public String getValueDecalPos(String valueCurrent ,int decalage){
        for(int i = 0; i < Rangee.values().length; ++i){
            if(Rangee.values()[i].getValue().equals( valueCurrent )){
                if ((i+decalage) < Rangee.values().length && (i+decalage) >= 0) {
                    return Rangee.values()[i + decalage].getValue();
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
