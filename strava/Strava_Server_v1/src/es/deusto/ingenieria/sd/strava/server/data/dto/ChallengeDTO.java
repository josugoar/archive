package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

//This class implements DTO pattern
public class ChallengeDTO implements Serializable {
	//This attribute is needed to implement the "Serializable" interface.
	private static final long serialVersionUID = 1L;

	private String name;
    private Date startDate;
    private Date endDate;
    private float distance;
    private Duration time;
    private boolean isCycling;
    private boolean isRunning;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public float getDistance() {
        return distance;
    }
    public void setDistance(float distance) {
        this.distance = distance;
    }
    public Duration getTime() {
        return time;
    }
    public void setTime(Duration time) {
        this.time = time;
    }
    public boolean isCycling() {
        return isCycling;
    }
    public void setCycling(boolean isCycling) {
        this.isCycling = isCycling;
    }
    public boolean isRunning() {
        return isRunning;
    }
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    @Override
    public String toString() {
        return "Challenge [name=" + name + ", startDate=" + startDate + ", endDate=" + endDate + ", distance="
                + distance + ", time=" + time + ", isCycling=" + isCycling + ", isRunning=" + isRunning + "]";
    }
}