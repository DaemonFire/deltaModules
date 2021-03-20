import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

public class DeltaModules {

    //CHANGE DIRECTORY PATH AND VALUE OF GAMMA HERE
    private static String directory = "data/enron";
    private static int delta = 323;

    public static void main(String[] args) {

        File f = new File(directory);
        String filePath;

        String[] pathList = f.list();
        String[] dataSets = new String[pathList.length];

        for (int i = 0; i < pathList.length; i++) {
            dataSets[i] = directory + "/" + pathList[i];

        }

        ArrayList<String> output = new ArrayList<>();
        Path fichier = Paths.get("results.csv");

        for (String filepath : dataSets) {
            String line = new String();
            System.out.println("COMPUTING FOR " + filepath);
            line = line.concat(filepath + ",");

            LinkStream ls = initiate(filepath);

            System.out.println("Number of vertices : " + ls.getVertices().size() + ", Number of edges : " + ls
                    .getLinks()

                    .size() + ", for " + (ls.getEndInstant() - ls.getStartInstant()) + " instants");
            line = line.concat(ls.getVertices().size() + "," + ls.getLinks().size() + "," + (ls.getEndInstant() - ls
                    .getStartInstant
                            ()) + ",,");
            line = line.concat(DeltaModules.compute(ls));
            output.add(line);

        }

        try {
            Files.write(fichier, output, Charset.forName("UTF-8"));
        } catch (IOException e) {
            System.err.println("Issue while writing the result file");
        }

    }

    public static String compute(LinkStream ls) {

        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();
        ArrayList<String> output = new ArrayList<>();
        String line = new String();
        ArrayList<DeltaModule> deltaModules = new ArrayList<>();

        deltaTwins = computeEternalTwinsNaively(ls, line);

        deltaTwins = computeEternalTwinsMEI(ls, line);

        deltaTwins = computeEternalTwinsMLEI(ls, line);

        deltaTwins = computeDeltaTwinsNaively(ls, line, delta);

        deltaTwins = computeDeltaTwinsMEI(ls, line, delta);

        deltaTwins = computeDeltaTwinsMLEI(ls, line, delta);

        line = line.concat(computeEternalModulesMEI(ls, line));

        line = line.concat(computeEternalModulesMLEI(ls, line));

        line = line.concat(computeDeltaModulesMEI(ls, line, delta));

        line = line.concat(computeDeltaModulesMLEI(ls, line, delta));

        output.add(line);

        System.out.println("Computation done ");
        return line;
    }

    static private LinkStream initiate(String filePath) {
        FileParser fp = null;
        try {
            fp = new FileParser(filePath);
        } catch (IOException e) {
            System.err.println("C'est cassé");
        }

        LinkStream ls = fp.getLs();
        return ls;

    }

    public static ArrayList<DeltaEdge> computeEternalTwinsNaively(LinkStream ls, String line) {

        System.out.println("COMPUTING ETERNAL TWINS NAIVELY");
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();
        try {
            Date startTime = new Date();

            deltaTwins = TwinAlgorithms.naivelyComputeEternalTwins(ls);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(deltaTwins.size() + "," + timeElapsed + ",");
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat(",OUT OF MEMORY,");
        }
        System.out.println("We have " + deltaTwins.size() + " eternal twins");
        return deltaTwins;
    }

    public static ArrayList<DeltaEdge> computeEternalTwinsMEI(LinkStream ls, String line) {

        System.out.println("COMPUTING ETERNAL TWINS USING EDGES ITERATION");
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();
        try {
            Date startTime = new Date();
            deltaTwins = TwinAlgorithms.computeEternalTwinsByEdgesIteration(ls);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(timeElapsed + ",");
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat("OUT OF MEMORY,");
        }
        System.out.println("We have " + deltaTwins.size() + " eternal twins");
        return deltaTwins;
    }

    public static ArrayList<DeltaEdge> computeEternalTwinsMLEI(LinkStream ls, String line) {

        System.out.println("COMPUTING ETERNAL TWINS USING EDGES ITERATION WITHOUT MATRICES");
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();

        try {
            Date startTime = new Date();
            deltaTwins = TwinAlgorithms.computeEternalTwinsByEdgesIterationWithoutMatrices(ls);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(timeElapsed + ",,");
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat("OUT OF MEMORY,,");
        }
        System.out.println("We have " + deltaTwins.size() + " eternal twins");
        return deltaTwins;
    }

    public static ArrayList<DeltaEdge> computeDeltaTwinsNaively(LinkStream ls, String line, int delta) {

        System.out.println("COMPUTING " + delta + "-TWINS NAIVELY");
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();

        try {
            Date startTime = new Date();
            deltaTwins = TwinAlgorithms.naivelyComputeDeltaTwins(ls, delta);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(deltaTwins.size() + "," + timeElapsed + ",");
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat(",OUT OF MEMORY,");
        }
        System.out.println("We have " + deltaTwins.size() + " " + delta + "-twins");
        return deltaTwins;
    }

    public static ArrayList<DeltaEdge> computeDeltaTwinsMEI(LinkStream ls, String line, int delta) {

        System.out.println("COMPUTING " + delta + "-TWINS USING EDGES ITERATION");
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();

        try {
            Date startTime = new Date();
            deltaTwins = TwinAlgorithms.computeDeltaTwinsByEdgesIteration(ls, delta);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(timeElapsed + ",");
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat("OUT OF MEMORY,");
        }
        System.out.println("We have " + deltaTwins.size() + " " + delta + "-twins");
        return deltaTwins;
    }

    public static ArrayList<DeltaEdge> computeDeltaTwinsMLEI(LinkStream ls, String line, int delta) {

        System.out.println("COMPUTING " + delta + "-TWINS USING EDGES ITERATION WITHOUT MATRICES");
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();

        try {
            Date startTime = new Date();
            deltaTwins = TwinAlgorithms.computeDeltaTwinsByEdgesIterationWithoutMatrices(ls, delta);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(timeElapsed + ",,");

        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat("OUT OF MEMORY,,");
        }
        System.out.println("We have " + deltaTwins.size() + " " + delta + "-twins");
        return deltaTwins;
    }

    public static String computeEternalModulesMEI(LinkStream ls, String line) {
        System.out.println("COMPUTING ETERNAL MODULES USING EDGES ITERATION");
        ArrayList<DeltaModule> deltaModules = new ArrayList<>();

        try {
            Date startTime = new Date();
            deltaModules = TwinAlgorithms.computeEternalModulesByEdgeIteration(ls);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(timeElapsed + ",,");

        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat("OUT OF MEMORY,,");
        }
        System.out.println("We have " + deltaModules.size() + " eternal modules");
        return line;
    }

    public static String computeEternalModulesMLEI(LinkStream ls, String line) {
        System.out.println("COMPUTING ETERNAL MODULES USING EDGES ITERATION WITHOUT MATRICES");
        ArrayList<DeltaModule> deltaModules = new ArrayList<>();

        try {
            Date startTime = new Date();
            deltaModules = TwinAlgorithms.computeEternalModulesByEdgeIterationWithoutMatrices(ls);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(timeElapsed + ",,");

        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat("OUT OF MEMORY,,");
        }
        System.out.println("We have " + deltaModules.size() + " eternal modules");
        return line;
    }

    public static String computeDeltaModulesMEI(LinkStream ls, String line, int delta) {
        System.out.println("COMPUTING " + delta + "-MODULES USING EDGES ITERATION");
        ArrayList<DeltaModule> deltaModules = new ArrayList<>();

        try {
            Date startTime = new Date();
            deltaModules = TwinAlgorithms.computeDeltaModulesByEdgeIteration(ls, delta);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(timeElapsed + ",,");

        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat("OUT OF MEMORY,,");
        }
        System.out.println("We have " + deltaModules.size() + " " + delta + "-modules");
        return line;
    }

    public static String computeDeltaModulesMLEI(LinkStream ls, String line, int delta) {
        System.out.println("COMPUTING " + delta + "-MODULES USING EDGES ITERATION WITHOUT MATRICES");
        ArrayList<DeltaModule> deltaModules = new ArrayList<>();

        try {
            Date startTime = new Date();
            deltaModules = TwinAlgorithms.computeDeltaModulesByEdgeIterationWithoutMatrices(ls, delta);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(timeElapsed + ",,");

        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat("OUT OF MEMORY,,");
        }
        System.out.println("We have " + deltaModules.size() + " " + delta + "-modules");
        return line;
    }
}
