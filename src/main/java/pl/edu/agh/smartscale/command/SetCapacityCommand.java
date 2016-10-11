package pl.edu.agh.smartscale.command;

public class SetCapacityCommand implements Command {

    private final int desiredCapacity;

    public SetCapacityCommand(int desired) {
        this.desiredCapacity = desired;
    }

    @Override
    public int getDesiredConsumers() {
        return desiredCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SetCapacityCommand that = (SetCapacityCommand) o;

        return desiredCapacity == that.desiredCapacity;
    }

    @Override
    public int hashCode() {
        return desiredCapacity;
    }
}
