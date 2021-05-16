import java.security.InvalidParameterException;

public class Position {
    private final String rangee;
    private final String colonne;

    public Position( String colonne, String rangee) throws Exception {
        if(estRangee(rangee) && estColonne(colonne)) {
            this.rangee = rangee;
            this.colonne = colonne;
        }
        else{
            //sans égale à null , le final fera une erreur si j'initialise pas les deux finals
            throw new InvalidParameterException("Position recu est invalide!");
        }
    }


    private boolean estRangee(String rangee){
        for(Rangee rangeeTemp : Rangee.values()){
            if(rangeeTemp.getValue().equals( rangee )){
                return true;
            }
        }
        return false;
    }
    private boolean estColonne(String colonne){
        for(Colonne colonneTemp : Colonne.values()){
            if(colonneTemp.getValue().equals( colonne )){
                return true;
            }
        }
        return false;
    }
    //faudra changer Rangee et Colonne par string et faire rangee.getValue()
    public String getRangee(){
        return rangee;
    }

    public String getColonne(){
        return colonne;
    }

    @Override
    public String toString() {
        return colonne + rangee;
    }

    @Override
    public boolean equals( Object obj ) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Position)) {
            return false;
        }
        Position pos = (Position) obj;

        return this.colonne.equals( pos.colonne ) && this.rangee.equals( pos.rangee );
    }
}
