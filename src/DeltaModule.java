import java.util.ArrayList;

public class DeltaModule {

    private int startInstant;
    private int endInstant;
    private ArrayList<Vertex> vertices;

    public DeltaModule(int startInstant, int endInstant) {
        this.startInstant = startInstant;
        this.endInstant = endInstant;
        this.vertices = new ArrayList<>();
    }

    public void setStartInstant(int startInstant) {
        this.startInstant = startInstant;
    }

    public void setEndInstant(int endInstant) {
        this.endInstant = endInstant;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public int getStartInstant() {
        return this.startInstant;
    }

    public int getEndInstant() {
        return this.endInstant;
    }

    public ArrayList<Vertex> getVertices() {
        return this.vertices;
    }

    public boolean isDeltaModule(int delta) {
        return (this.endInstant - this.startInstant + 1 >= delta);
    }

    public void addVertex(Vertex u) {
        if (!this.vertices.contains((u))) {
            this.vertices.add(u);
        }
    }

    public boolean isSame(DeltaModule e){
        if (this.startInstant!=e.startInstant){
            return false;
        }
        if (this.endInstant!=e.getEndInstant()){
            return false;
        }
        ArrayList<Vertex> difference = (ArrayList<Vertex>) this.vertices.clone();
        for (Vertex u : e.getVertices()) {
            if (!difference.contains(u)) {
                return false;
            } else {
                difference.remove(u);
            }
        }
        if (difference.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}
