package prereqchecker;

import java.util.*;

public class SchedulePlan {
    public static void main(String[] args) {
        if (args.length < 3) {
            StdOut.println("Execute: java -cp bin prereqchecker.SchedulePlan <adjacency list INput file> <schedule plan INput file> <schedule plan OUTput file>");
            return;
        }
        String adjListInputFile = args[0];
        String schedulePlanInputFile = args[1];
        String schedulePlanOutputFile = args[2];

        HashMap<String, List<String>> graph = new HashMap<>();
        GraphBuilder.constructGraph(adjListInputFile, graph);

        StdIn.setFile(schedulePlanInputFile);
        String targetCourse = StdIn.readString();
        int numOfTakenCourses = StdIn.readInt();
        Set<String> takenCourses = new HashSet<>();
        for (int i = 0; i < numOfTakenCourses; i++) {
            takenCourses.add(StdIn.readString());
        }

        // Plan the schedule
        List<List<String>> schedule = planSchedule(graph, targetCourse, takenCourses);
        // Output the schedule
        outputSchedule(schedulePlanOutputFile, schedule);
    }

    private static List<List<String>> planSchedule(HashMap<String, List<String>> graph, String targetCourse, Set<String> takenCourses) {
        List<List<String>> schedule = new ArrayList<>();
        Set<String> prerequisitesMet = new HashSet<>(takenCourses); // Courses for which prerequisites are met
        Set<String> allPrerequisites = new HashSet<>();
        findAllPrerequisites(graph, targetCourse, allPrerequisites); // Find all prerequisites for the target course
        allPrerequisites.removeAll(takenCourses); // Exclude already taken courses

        while (!prerequisitesMet.contains(targetCourse)) {
            List<String> semester = new ArrayList<>();
            boolean courseAdded = false;
            for (String course : allPrerequisites) {
                if (graph.get(course).stream().allMatch(prerequisitesMet::contains)) {
                    semester.add(course);
                    prerequisitesMet.add(course);
                    courseAdded = true;
                }
            }
            allPrerequisites.removeAll(semester);
            if (!courseAdded && !semester.contains(targetCourse)) {
                System.out.println("Stuck at semester: " + (schedule.size() + 1));
                System.out.println("Remaining courses: " + allPrerequisites);
                System.out.println("Prerequisites met: " + prerequisitesMet);
                throw new RuntimeException("Unable to schedule more courses, prerequisites not met");
            }
            schedule.add(semester);
            if (semester.contains(targetCourse)) {
                break;
            }
        }
        return schedule;
    }


    private static void findAllPrerequisites(HashMap<String, List<String>> graph, String course, Set<String> allPrerequisites) {
        if (allPrerequisites.contains(course) || graph.get(course) == null) {
            return;
        }
        for (String prereq : graph.get(course)) {
            if (allPrerequisites.add(prereq)) {
                findAllPrerequisites(graph, prereq, allPrerequisites);
            }
        }
    }


    private static void outputSchedule(String outputFile, List<List<String>> schedule) {
        StdOut.setFile(outputFile);
        StdOut.println(schedule.size()); // Number of semesters
        for (List<String> semester : schedule) {
            for (String course : semester) {
                StdOut.print(course + " ");
            }
            StdOut.println();
        }
        StdOut.close();
    }
}
