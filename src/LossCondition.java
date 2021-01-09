public enum LossCondition {
    WORLDWIDE_PANIC("8 outbreaks occur"),
    SPREAD_TOO_MUCH("Not enough disease cubes are left when needed"),
    OUT_OF_TIME("Not enough player cards are left when needed");

    private String cause;

    LossCondition(String cause) {
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }
}
