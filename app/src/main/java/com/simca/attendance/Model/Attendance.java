package com.simca.attendance.Model;

public class Attendance {

    String div,name,roll_no,subject,timespan;

    public Attendance() {
    }

    public Attendance(String div, String name, String roll_no, String subject, String timespan) {
        this.div = div;
        this.name = name;
        this.roll_no = roll_no;
        this.subject = subject;
        this.timespan = timespan;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTimespan() {
        return timespan;
    }

    public void setTimespan(String timespan) {
        this.timespan = timespan;
    }
}
