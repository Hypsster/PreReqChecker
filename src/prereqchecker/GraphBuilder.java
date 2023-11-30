package prereqchecker;

import java.util.*;

public class GraphBuilder {
    public static void constructGraph(String inputFile, HashMap<String, List<String>> graph) {
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
}
