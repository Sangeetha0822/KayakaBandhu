package com.android.project.model;

public class Job {
    private long jobID;
    private String title;
    private String description;
    private String fundsAllotted;
    private int numberOfLabourers;
    private int durationInDays;

    public long getJobID() {
        return jobID;
    }

    public void setJobID(long jobID) {
        this.jobID = jobID;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFundsAllotted() {
        return fundsAllotted;
    }

    public void setFundsAllotted(String fundsAllotted) {
        this.fundsAllotted = fundsAllotted;
    }

    public int getNumberOfLabourers() {
        return numberOfLabourers;
    }

    public void setNumberOfLabourers(int numberOfLabourers) {
        this.numberOfLabourers = numberOfLabourers;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }
}
