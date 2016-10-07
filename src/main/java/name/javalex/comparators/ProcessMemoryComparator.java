package name.javalex.comparators;

import name.javalex.entities.Process;

import java.util.Comparator;

public class ProcessMemoryComparator implements Comparator<Process> {


    @Override
    public int compare(Process process1, Process process2) {
        int res = 0;
        if (process1.getUsedMemory() == (process2.getUsedMemory())) {
            res = 0;
        } else if (process1.getUsedMemory() > (process2.getUsedMemory())) {
            res = 1;
        } else if (process1.getUsedMemory() < (process2.getUsedMemory())) {
            res = -1;
        }
        return res;
    }
}
