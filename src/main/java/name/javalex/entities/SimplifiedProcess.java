package name.javalex.entities;

public class SimplifiedProcess {
    private String name;
    private long memory;

    public SimplifiedProcess(String name, long memory) {
        this.name = name;
        this.memory = memory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                ", memory=" + memory +
                '}';
    }

    // --------------- Equals by NAME ---------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimplifiedProcess process = (SimplifiedProcess) o;

        return name.equals(process.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
