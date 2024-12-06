package com.ParkCore.enums;

public enum Classification {
    EXCELLENT("Excellent"),
    GOOD("Good"),
    VERY_GOOD("Very Good"),
    OK("Ok"),
    AVERAGE("Average"),
    POOR("Poor"),
    TERRIBLE("Terrible"),
    HORRIBLE("Horrible");

    private final String classification;

    Classification(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }

}

