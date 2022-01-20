package de.lases.selenium.stress;

public record ResponseTimeEntry(String actionName, Long responseTime) {
    public String toString() {
        return "(" + actionName + ", " + responseTime + ")";
    }
}
