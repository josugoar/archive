package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.io.Serializable;
import java.util.Date;

public class ActivityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private double distance;
    private int elapsedTime;
    private String type;
    private Date startDate;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(final double distance) {
        this.distance = distance;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(final int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "Activity [name=" + name + ", distance=" + distance + ", elapsedTime=" + elapsedTime + ", type=" + type
                + ", startDate=" + startDate + "]";
    }

}