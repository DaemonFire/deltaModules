import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileParser {

    static HashMap<Integer, Vertex> indexToVertex;
    static LinkStream ls;

    public FileParser(String filePath) throws IOException {
        ArrayList<Vertex> vertices = new ArrayList<>();
        int startInstant = Integer.MAX_VALUE;
        int endInstant = 0;
        ls = new LinkStream(vertices);
        indexToVertex = new HashMap<>();
        BufferedReader r = new BufferedReader(new FileReader(filePath));
        String s = r.readLine();
        while (s != null) {
            String[] points = s.replaceAll("\n", "").split(" ");
            Vertex v;
            Vertex u;
            if (indexToVertex.containsKey(Integer.parseInt(points[0]))) {
                u = indexToVertex.get(Integer.parseInt(points[0]));
            } else {
                u = new Vertex();
                indexToVertex.put(Integer.parseInt(points[0]), u);
                ls.getVertices().add(u);
            }
            if (indexToVertex.containsKey(Integer.parseInt(points[1]))) {
                v = indexToVertex.get(Integer.parseInt(points[1]));
            } else {
                v = new Vertex();
                indexToVertex.put(Integer.parseInt(points[1]), v);
                ls.getVertices().add(v);
            }
            ls.getLinks().add(new TemporalEdge(Integer.parseInt(points[2]), u, v));
            if (Integer.parseInt(points[2]) > endInstant) {
                endInstant = Integer.parseInt(points[2]);
            }
            if (Integer.parseInt(points[2]) < startInstant) {
                startInstant = Integer.parseInt(points[2]);
            }

            s = r.readLine();
        }

        ls.setStartInstant(startInstant);
        ls.setEndInstant(endInstant);

        System.out.println("Datafile " + filePath + " parsed");
    }

    public LinkStream getLs() {
        return this.ls;
    }

}
