package prereqchecker;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class NeedToTake {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Execute: java NeedToTake <adjacency list input file> <need to take input file> <need to take output file>");
            return;
        }

        String adjListInputFile = args[0];
        String needToTakeInputFile = args[1];
        String needToTakeOutputFile = args[2];

        // Read the graph and calculate all prerequisites
        HashMap<String, Set<String>> allPrerequisites = readGraphAndCalculatePrerequisites(adjListInputFile);

        // Read the target course and taken courses
        String targetCourse;
        Set<String> takenCourses = new HashSet<>();
        try (Scanner scanner = new Scanner(new File(needToTakeInputFile))) {
            targetCourse = scanner.nextLine();
            int numOfTakenCourses = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < numOfTakenCourses; i++) {
                takenCourses.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Find missing prerequisites
        Set<String> missingPrereqs = new HashSet<>(allPrerequisites.getOrDefault(targetCourse, new HashSet<>()));
        for (String course : takenCourses) {
            missingPrereqs.removeAll(allPrerequisites.getOrDefault(course, new HashSet<>()));
        }
        missingPrereqs.removeAll(takenCourses); // Remove courses already taken

        // Write missing prerequisites to output file
        try (PrintWriter writer = new PrintWriter(needToTakeOutputFile)) {
            for (String course : missingPrereqs) {
                writer.println(course);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, Set<String>> readGraphAndCalculatePrerequisites(String inputFile) {
        HashMap<String, List<String>> graph = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(inputFile))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(" ");
                if (parts.length >= 2) {
                    graph.computeIfAbsent(parts[0], k -> new ArrayList<>()).add(parts[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String, Set<String>> allPrerequisites = new HashMap<>();
        for (String course : graph.keySet()) {
            Set<String> prereqs = new HashSet<>();
            findAllPrerequisites(course, prereqs, graph);
            allPrerequisites.put(course, prereqs);
        }
        return allPrerequisites;
    }

    private static void findAllPrerequisites(String course, Set<String> allPrerequisites, HashMap<String, List<String>> graph) {
        if (graph.containsKey(course)) {
            for (String prereq : graph.get(course)) {
                if (allPrerequisites.add(prereq)) {
                    findAllPrerequisites(prereq, allPrerequisites, graph);
                }
            }
        }
    }
}