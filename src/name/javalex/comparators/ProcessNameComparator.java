package name.javalex.comparators;

import name.javalex.entities.Process;

import java.util.Comparator;

public class ProcessNameComparator implements Comparator<Process> {

    @Override
    public int compare(Process process1, Process process2) {
        return process1.getName().compareToIgnoreCase(process2.getName());
    }
}
