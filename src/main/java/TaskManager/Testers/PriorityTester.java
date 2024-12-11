// Test Priority and priorityService

package TaskManager.Testers;

import java.util.ArrayList;
import java.util.List;

import TaskManager.PriorityService;

public class PriorityTester {
    public static void main(String[] args) {
        PriorityService priorityService = new PriorityService();
        List<Integer> createdPriorityIds = new ArrayList<>();
        System.out.println("---Test Priorities---");
        try {
            // 1. Add Priorities
            priorityService.addPriority("TP-High");
            priorityService.addPriority("TP-Medium");
            createdPriorityIds.add(priorityService.getPriorityByName("TP-High").getId());
            createdPriorityIds.add(priorityService.getPriorityByName("TP-Medium").getId());

            System.out.println("Priorities after addition:");
            priorityService.getPriorities().forEach(System.out::println);

            // 2. Update Priorities
            priorityService.updatePriority(createdPriorityIds.get(0), "TP-Urgent");

            System.out.println("\nPriorities after update:");
            priorityService.getPriorities().forEach(System.out::println);

            // 3. Delete Priorities
            priorityService.deletePriority(createdPriorityIds.get(1));

            System.out.println("\nPriorities after deletion:");
            priorityService.getPriorities().forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Error during testing priorities: " + e.getMessage());
        } finally {
            // Delete testing data
            for (int id : createdPriorityIds) {
                try {
                    priorityService.deletePriority(id);
                } catch (Exception ignored) {
                }
            }
        }
    }
}
