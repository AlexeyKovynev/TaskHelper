package name.javalex.logic;

import name.javalex.comparators.ProcessMemoryComparator;
import name.javalex.comparators.ProcessNameComparator;
import name.javalex.entities.Process;
import name.javalex.entities.SimplifiedProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Model {

    private List<Process> processes;
    private List<Process> groupedProcesses;

    public List<Process> getProcesses() {

        processes = new ArrayList();

        String line;
        String name = "";
        int pid = 0;
        long memory = 0;
        int counter = 0;

        try {

            // Get system processes
            java.lang.Process p = null;

            p = Runtime.getRuntime().exec("tasklist /fo \"list\"");

            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = input.readLine()) != null) {
                line = line.replace('\u00A0', ' ');

                if (line.startsWith("Image Name")) {
                    name = line.substring(line.lastIndexOf(":") + 1).trim();
                } else if (line.startsWith("PID")) {
                    line = line.substring(line.lastIndexOf(":") + 1).trim();
                    pid = Integer.valueOf(line);
                } else if (line.startsWith("Mem Usage")) {
                    line = line.substring(line.lastIndexOf(":") + 1).replace(" K", "").replace(",", "").trim();
                    memory = Long.valueOf(line);
                }

                counter++;

                if (counter == 6) { // Finished with current task (each system task takes 6 lines of output)

                    // Add info about handled process to collection
                    processes.add(new Process(name, pid, memory));

                    // Reset counter and values
                    counter = 0;
                    name = "";
                    pid = 0;
                    memory = 0;
                }
            }

            // Close reader
            input.close();
        } catch (IOException e) {
            System.out.println("Error occurred while trying to get system processes " + e);
        }

        return processes;
    }

    // Group processes by name and sum up memory usage for same processes
    public List<Process> groupByName(List<Process> proc) {

        // Sort input processes by name
        proc = sortByName(proc);

        groupedProcesses = new ArrayList<>();

        for (int i = 0; i < proc.size(); i++) {

            if (groupedProcesses.isEmpty()) {
                // add first element if empty
                groupedProcesses.add(proc.get(i));
            } else if (groupedProcesses.get(groupedProcesses.size() - 1).equals(proc.get(i))) {
                // increase used memory if task is already here
                long memorySum = groupedProcesses.get(groupedProcesses.size() - 1).getUsedMemory() + proc.get(i).getUsedMemory();
                groupedProcesses.get(groupedProcesses.size() - 1).setUsedMemory(memorySum);
            } else if (!(groupedProcesses.get(groupedProcesses.size() - 1).equals(proc.get(i)))) {
                // add if not already here
                groupedProcesses.add(proc.get(i));
            }
        }
        //System.out.println("groupByName");
        //System.out.println(sortByMemory(groupedProcesses));
        return sortByMemory(groupedProcesses);
    }

    public List<Process> sortByMemory(List<Process> proc) {
        Collections.sort(proc, new ProcessMemoryComparator());

        // I decided to sort by memory in reverse order (from large to small), I will ask next time :)
        Collections.reverse(proc);
        return proc;
    }

    public List<Process> sortByName(List<Process> proc) {
        Collections.sort(proc, new ProcessNameComparator());
        return proc;
    }

    public List<SimplifiedProcess> createSimplifiedProcessList(List<Process> proc) {
        List<SimplifiedProcess> result = new ArrayList<>();
        for (Process pr : proc) {
            result.add(new SimplifiedProcess(pr.getName(), pr.getUsedMemory()));
        }
        return result;
    }

}
