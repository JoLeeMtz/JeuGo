import java.util.ArrayList;

public interface RecuperationJeton {
    Jeton getJeton( ArrayList<Instruction> infos, Position pos);

    Jeton getDeletedJeton(ArrayList<Instruction> infos, Position pos);
}
