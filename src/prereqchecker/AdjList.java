package prereqchecker;

import java.util.*;

public class AdjList {
    public static void main(String[] args) {
        if (args.length < 2) {
            StdOut.println("Execute: java -cp bin prereqchecker.AdjList <adjacency list INput file> <adjacency list OUTput file>");
            return;
        }
        String inputFile = args[0];
        String outputFile = args[1];
        HashMap<String, List<String>> graph = new HashMap<>();
        constructGraph(inputFile, graph);
        writeGraph(outputFile, graph);
    }

    private static void constructGraph(String inputFile, HashMap<String, List<String>> graph) {
        StdIn.setFile(inputFile);
        int numCourses = StdIn.readInt();
        for (int i = 0; i < numCourses; i++) {
            String course = StdIn.readString();
            graph.putIfAbsent(course, new ArrayList<>());
        }
        int numPrerequisites = StdIn.readInt();
        for (int i = 0; i < numPrerequisites; i++) {
            String course = StdIn.readString();
            String prerequisite = StdIn.readString();
            graph.get(course).add(prerequisite);
        }
    }

    private static void writeGraph(String outputFile, HashMap<String, List<String>> graph) {
        StdOut.setFile(outputFile);
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            StdOut.print(entry.getKey());
            for (String prereq : entry.getValue()) {
                StdOut.print(" " + prereq);
            }
            StdOut.println();
        }
        StdOut.close();
    }
}
