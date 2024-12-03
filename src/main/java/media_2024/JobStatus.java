package media_2024;

public enum JobStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    POSTPONED("Postponed"),
    COMPLETED("Completed"),
    DELAYED("Delayed");

    // This field holds a string representation of each JobStatus
    private final String status;

    JobStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
