package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

//This class implements DTO pattern
public class ActivityDTO implements Serializable {
	//This attribute is needed to implement the "Serializable" interface.
	private static final long serialVersionUID = 1L;

	private String name;
    private float distance;
    private Duration elapsedTime;
    private String type;
    private Date startDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Duration getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Duration elapsed_time) {
        this.elapsedTime = elapsed_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date start_date) {
        this.startDate = start_date;
    }

    @Override
    public String toString() {
        return "Activity [name=" + name + ", distance=" + distance + ", elapsedTime=" + elapsedTime + ", type=" + type
                + ", startDate=" + startDate + "]";
    }
}