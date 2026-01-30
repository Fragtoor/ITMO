package creatures;

public record Wound(String location, boolean isBandaged) {
    public Wound applyPlaster() {
        return new Wound(location, true);
    }
}
