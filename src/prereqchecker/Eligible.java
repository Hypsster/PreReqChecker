package prereqchecker;

import java.util.*;

public class Eligible {
    public static void main(String[] args) {
        if (args.length < 3) {
            StdOut.println("Execute: java -cp bin prereqchecker.Eligible <adjacency list INput file> <eligible INput file> <eligible OUTput file>");
            return;
        }
        String adjListInputFile = args[0];
        String eligibleInputFile = args[1];
        String eligibleOutputFile = args[2];

        HashMap<String, List<String>> graph = new HashMap<>();
        GraphBuilder.constructGraph(adjListInputFile, graph);

        Set<String> completedCourses = new HashSet<>();
        readCompletedCourses(eligibleInputFile, completedCourses);

        includeAllPrerequisites(graph, completedCourses);

        List<String> eligibleCourses = findEligibleCourses(graph, completedCourses);

        writeEligibleCourses(eligibleOutputFile, eligibleCourses);
    }

    private static void readCompletedCourses(String inputFile, Set<String> completedCourses) {
        StdIn.setFile(inputFile);
        int numOfCourses = StdIn.readInt();
        for (int i = 0; i < numOfCourses; i++) {
            completedCourses.add(StdIn.readString());
        }
    }

    private static void includeAllPrerequisites(HashMap<String, List<String>> graph, Set<String> completedCourses) {
        Set<String> toExplore = new HashSet<>(completedCourses);

        while (!toExplore.isEmpty()) {
            Set<String> newCourses = new HashSet<>();
            for (String course : toExplore) {
                List<String> prereqs = graph.get(course);
                if (prereqs != null) {
                    for (String prereq : prereqs) {
                        if (completedCourses.add(prereq)) {
                            newCourses.add(prereq);
                        }
                    }
                }
            }
            toExplore = newCourses;
        }
    }

    private static List<String> findEligibleCourses(HashMap<String, List<String>> graph, Set<String> completedCourses) {
        List<String> eligibleCourses = new ArrayList<>();
        for (String course : graph.keySet()) {
            if (!completedCourses.contains(course) && completedCourses.containsAll(graph.get(course))) {
                eligibleCourses.add(course);
            }
        }
        return eligibleCourses;
    }

    private static void writeEligibleCourses(String outputFile, List<String> eligibleCourses) {
        StdOut.setFile(outputFile);
        for (String course : eligibleCourses) {
            StdOut.println(course);
        }
        StdOut.close();
    }
}
