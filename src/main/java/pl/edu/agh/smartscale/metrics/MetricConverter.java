package pl.edu.agh.smartscale.metrics;

public class MetricConverter {

    private static final String PREFIX = "HyperFlow.";
    private static final String TASKS_LEFT = "nTasksLeft";
    private static final String OUTPUTS_LEFT = "nOutputsLeft";
    private static final String TASKS_PROCESSED = "nTasksProcessed";
    private static final String ALL_TASKS = "nTasks";
    private static final String STAGE = "stage";
    private static final String CONSUMERS = "nConsumers";

    public MetricData convert(String data) {
        int tasksLeft = -1;
        int outputsLeft = -1;
        int tasksProcessed = -1;
        int allTasks = -1;
        int stage = -1;
        int consumers = -1;
        for (String line : data.split(System.getProperty("line.separator"))) {
            if (line.startsWith(PREFIX + TASKS_LEFT)) {
                tasksLeft = Integer.parseInt(line.split("\\s")[1]);
            } else if (line.startsWith(PREFIX + OUTPUTS_LEFT)) {
                outputsLeft = Integer.parseInt(line.split("\\s")[1]);
            } else if (line.startsWith(PREFIX + TASKS_PROCESSED)) {
                tasksProcessed = Integer.parseInt(line.split("\\s")[1]);
            } else if (line.startsWith(PREFIX + ALL_TASKS)) {
                allTasks = Integer.parseInt(line.split("\\s")[1]);
            } else if (line.startsWith(PREFIX + STAGE)) {
                stage = Integer.parseInt(line.split("\\s")[1]);
            } else if (line.startsWith( PREFIX + CONSUMERS)) {
                consumers = Integer.parseInt(line.split("\\s")[1]);
            }
        }
        return MetricData.builder()
                .tasksLeft(tasksLeft)
                .outputsLeft(outputsLeft)
                .tasksProcessed(tasksProcessed)
                .allTasks(allTasks)
                .stage(stage)
                .consumers(consumers)
                .build();
    }
}