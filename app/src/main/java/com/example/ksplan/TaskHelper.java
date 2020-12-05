package com.example.ksplan;

import java.io.Serializable;

public class TaskHelper implements Serializable {
    private String taskName;
    private String unit;
    private int min;
    private int max;
    private int increment;
    private String infoLink;

    public TaskHelper(String taskName, String unit, int min, int max, int increment, String infoLink) {
        this.taskName = taskName;
        this.unit = unit;
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.infoLink = infoLink;
    }

    public TaskHelper(String taskName, String unit, int min, int max) {
        this.taskName = taskName;
        this.unit = unit;
        this.min = min;
        this.max = max;
    }

    public TaskHelper(String taskName, String unit, int max) {
        this.taskName = taskName;
        this.unit = unit;
        this.max = max;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getUnit() {
        return unit;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getIncrement() {
        return increment;
    }

    public String getInfoLink() {
        return infoLink;
    }
}
