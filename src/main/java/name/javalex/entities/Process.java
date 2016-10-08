package name.javalex.entities;

public class Process {
    private String name;
    private int pid;
    private long usedMemory;

    public Process(String name, int pid, long usedMemory) {
        this.name = name;
        this.pid = pid;
        this.usedMemory = usedMemory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }

    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                ", pid='" + pid + '\'' +
                ", usedMemory=" + usedMemory +
                '}';
    }

    // --------------- Equals by NAME ---------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Process process = (Process) o;

        return name.equals(process.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
