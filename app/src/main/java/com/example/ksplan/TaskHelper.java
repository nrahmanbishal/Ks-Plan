package com.example.ksplan;

import java.io.Serializable;

public class TaskHelper implements Serializable {
    private String taskName;
    private String unit;
    private int min;
    private int max;
    private double increment;
    private String infoLink;

    private double resultNumber=0.0;
    private int result;

    public TaskHelper(String taskName, String unit, int min, int max, double increment, String infoLink) {
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

    public double getIncrement() {
        return increment;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setResultNumber(double resultNumber) {
        this.resultNumber = resultNumber;
    }

    public double getResultNumber() {
        return resultNumber;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
