package name.javalex.model;

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

    // This method will be replaced with CSV parsing instead of list because of easiest multi-language support
    public List<Process> getProcesses() {

        List<Process> processes = new ArrayList<>();

        String line;
        String csvSplitter = "\",\"";

        String name;
        int pid;
        long memory;

        try {

            // Get system processes
            java.lang.Process p;
            p = Runtime.getRuntime().exec("TASKLIST /FO \"CSV\" /NH");

            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = input.readLine()) != null) {

                String[] proc = line.split(csvSplitter);

                name = proc[0].replace("\"", "");
                pid = Integer.valueOf(proc[1]);
                memory = Long.valueOf(proc[4].replaceAll("\\D+", ""));
                processes.add(new Process(name, pid, memory));
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

        List<Process> groupedProcesses = new ArrayList<>();

        for (Process aProc : proc) {

            if (groupedProcesses.isEmpty()) {
                // add first element if empty
                groupedProcesses.add(aProc);
            } else if (groupedProcesses.get(groupedProcesses.size() - 1).equals(aProc)) {
                // increase used memory if task is already here
                long memorySum = groupedProcesses.get(groupedProcesses.size() - 1).getUsedMemory() + aProc.getUsedMemory();
                groupedProcesses.get(groupedProcesses.size() - 1).setUsedMemory(memorySum);
            } else if (!(groupedProcesses.get(groupedProcesses.size() - 1).equals(aProc))) {
                // add if not already here
                groupedProcesses.add(aProc);
            }
        }

        return sortByMemory(groupedProcesses);
    }

    public List<Process> sortByMemory(List<Process> proc) {
        Collections.sort(proc, new ProcessMemoryComparator());

        // I decided to sort by memory in reverse order (from large to small)
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

    // Calculate which process uses more/less memory (opened from XML or current one)
    public String getMemoryDifferences(long openedMemory, long currentMemory) {
        long result = currentMemory - openedMemory;
        String conclusion;
        if (result > 0) {
            conclusion = "Now this process uses " + result + " KB more than it did when selected report was made";
        } else if (result < 0) {
            result = Math.abs(result);
            conclusion = "Now this process uses " + result + " KB less than it did when selected report was made";
        } else {
            conclusion = "This process uses same memory amount as it did when selected report was made";
        }
        return conclusion;
    }
}
