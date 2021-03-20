public class TemporalEdge {

    private int t;
    private Vertex u;
    private Vertex v;

    public TemporalEdge(int t, Vertex u, Vertex v) {
        this.t = t;
        this.u = u;
        this.v = v;
    }

    public void setT(int t) {
        this.t = t;
    }

    public void setU(Vertex u) {
        this.u = u;
    }

    public void setV(Vertex v) {
        this.v = v;
    }

    public int getT() {
        return this.t;
    }

    public Vertex getU() {
        return this.u;
    }

    public Vertex getV() {
        return this.v;
    }
}
