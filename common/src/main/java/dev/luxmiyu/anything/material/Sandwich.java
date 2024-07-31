package dev.luxmiyu.anything.material;

public record Sandwich(String start, String end) {
    public boolean test(String name) {
        if (!name.startsWith(this.start)) return false;
        if (!name.endsWith(this.end)) return false;

        return true;
    }
}
