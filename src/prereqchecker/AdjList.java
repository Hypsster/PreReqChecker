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

        GraphBuilder.constructGraph(inputFile, graph); // Construct the graph from the input file

        writeGraph(outputFile, graph); // Write the constructed graph to the output file
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
