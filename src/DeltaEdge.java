public class DeltaEdge {

    private int startInstant;
    private int endInstant;
    private Vertex u;
    private Vertex v;

    public DeltaEdge(int startInstant, int endInstant, Vertex u, Vertex v) {
        this.startInstant = startInstant;
        this.endInstant = endInstant;
        this.u = u;
        this.v = v;
    }

    public void setStartInstant(int startInstant) {
        this.startInstant = startInstant;
    }

    public void setEndInstant(int endInstant) {
        this.endInstant = endInstant;
    }

    public void setU(Vertex u) {
        this.u = u;
    }

    public void setV(Vertex v) {
        this.v = v;
    }

    public int getStartInstant() {
        return this.startInstant;
    }

    public int getEndInstant() {
        return this.endInstant;
    }

    public Vertex getU() {
        return this.u;
    }

    public Vertex getV() {
        return this.v;
    }

    public boolean isDeltaEdge(int delta) {
        return (this.endInstant - this.startInstant + 1 >= delta);
    }
}
