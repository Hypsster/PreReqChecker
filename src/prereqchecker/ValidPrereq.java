package prereqchecker;

import java.util.*;

public class ValidPrereq {
    public static void main(String[] args) {
        System.out.println("HI");
        if (args.length < 3) {
            StdOut.println("Execute: java -cp bin prereqchecker.ValidPrereq <adjacency list INput file> <valid prereq INput file> <valid prereq OUTput file>");
            System.out.println("FAILED");
            return;
        }
        System.out.println("BEGINNING");
        String adjListInputFile = args[0];
        String validPreReqInputFile = args[1];
        String validPreReqOutputFile = args[2];

        HashMap<String, List<String>> graph = new HashMap<>();
        GraphBuilder.constructGraph(adjListInputFile, graph);

        // Read the proposed prerequisite relationship
        StdIn.setFile(validPreReqInputFile);
        String course1 = StdIn.readString();
        String course2 = StdIn.readString();
        System.out.println("READ FILES");
        // Check if adding course2 as a prerequisite of course1 creates a cycle
        boolean createsCycle = checkCycleAfterAddingPrerequisite(graph, course1, course2);

        System.out.println("START OUTPUT");
        // Write the result to the output file
        StdOut.setFile(validPreReqOutputFile);
        StdOut.println(createsCycle ? "NO" : "YES");
        StdOut.close();
        System.out.println("OUTPUTTED");
    }

    private static boolean checkCycleAfterAddingPrerequisite(HashMap<String, List<String>> graph, String course1, String course2) {
        // Tadd new prereq temp
        graph.getOrDefault(course1, new ArrayList<>()).add(course2);

        // Check for the cycles
        boolean hasCycle = hasCycle(graph);

        // Remove the added prereqs
        graph.get(course1).remove(course2);

        return hasCycle;
    }

    private static boolean hasCycle(HashMap<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        for (String node : graph.keySet()) {
            if (hasCycleUtil(node, visited, recStack, graph)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasCycleUtil(String node, Set<String> visited, Set<String> recStack, HashMap<String, List<String>> graph) {
        if (recStack.contains(node)) {
            return true;
        }
        if (visited.contains(node)) {
            return false;
        }

        visited.add(node);
        recStack.add(node);

        List<String> children = graph.get(node);
        if (children != null) {
            for (String child : children) {
                if (hasCycleUtil(child, visited, recStack, graph)) {
                    return true;
                }
            }
        }
        recStack.remove(node);
        return false;
    }
}
