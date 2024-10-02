package com.android.project.model;

import java.util.Calendar;
import java.util.List;

public class Attendance {
    private long attendanceID;
    private Calendar date;
    private long labourerID;
    private long jobID;

    public long getAttendanceID() {
        return attendanceID;
    }

    public void setAttendanceID(long attendanceID) {
        this.attendanceID = attendanceID;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public long getLabourerID() {
        return labourerID;
    }

    public void setLabourerID(long labourerID) {
        this.labourerID = labourerID;
    }

    public long getJobID() {
        return jobID;
    }

    public void setJobID(long jobID) {
        this.jobID = jobID;
    }


}
