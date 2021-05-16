public class Pass implements TypeInstruction {
    private String info;

    @Override
    public String toString() {
        return info;
    }

    @Override
    public String getType() {
        return this.info;
    }

    @Override
    public void setType(String info) {
        this.info = info;
    }
}
