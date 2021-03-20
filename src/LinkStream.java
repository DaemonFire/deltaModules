import java.util.ArrayList;

public class LinkStream {

    private ArrayList<Vertex> vertices;
    private ArrayList<TemporalEdge> links;
    private int startInstant;
    private int endInstant;
    private ArrayList<int[][]> matrixSuite;

    public LinkStream(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
        this.links = new ArrayList<>();
        this.startInstant = Integer.MAX_VALUE;
        this.endInstant = 0;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public void setLinks(ArrayList<TemporalEdge> links) {
        this.links = links;
    }

    public void setStartInstant(int startInstant) {
        this.startInstant = startInstant;
    }

    public void setEndInstant(int endInstant) {
        this.endInstant = endInstant;
    }

    public void setMatrixSuite(ArrayList<int[][]> ms) {
        this.matrixSuite = ms;
    }

    public ArrayList<Vertex> getVertices() {
        return this.vertices;
    }

    public ArrayList<TemporalEdge> getLinks() {
        return this.links;
    }

    public int getStartInstant() {
        return this.startInstant;
    }

    public int getEndInstant() {
        return this.endInstant;
    }

    public ArrayList<int[][]> getMatrixSuite() {
        return this.matrixSuite;
    }

    public void computeMatrixSuite() {
        this.matrixSuite = new ArrayList<>();

        for (int t = this.startInstant; t <= this.endInstant; t++) {
            int[][] m = new int[this.vertices.size()][this.vertices.size()];
            for (int i = 0; i < this.vertices.size(); i++) {
                for (int j = 0; j < this.vertices.size(); j++) {
                    m[i][j] = 0;
                }
            }
            matrixSuite.add(m);
        }

        for (TemporalEdge e : this.links) {
            matrixSuite.get(e.getT() - this.startInstant)[this.vertices.indexOf(e.getU())][this.vertices.indexOf(e
                    .getV())] = 1;
            matrixSuite.get(e.getT() - this.startInstant)[this.vertices.indexOf(e.getV())][this.vertices.indexOf(e
                    .getU())] = 1;
        }
    }

}
