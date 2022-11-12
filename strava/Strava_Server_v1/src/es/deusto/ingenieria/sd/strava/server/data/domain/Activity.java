package es.deusto.ingenieria.sd.strava.server.data.domain;

import java.time.Duration;
import java.util.Date;

public class Activity {

    private String name;
    private float distance;
    private Duration elapsedTime;
    private String type;
    private Date startDate;

    public boolean checkActivity() {
        return checkDistance() && checkElapsedTime() && checkType() && checkElapsedTime();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean checkDistance() {
        return distance >= 0;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public boolean checkElapsedTime() {
        return !elapsedTime.isNegative();
    }

    public Duration getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Duration elapsed_time) {
        this.elapsedTime = elapsed_time;
    }

    public boolean checkType() {
        return type.equals("running") || type.equals("cycling");
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Activity other = (Activity) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (Float.floatToIntBits(distance) != Float.floatToIntBits(other.distance))
            return false;
        if (elapsedTime == null) {
            if (other.elapsedTime != null)
                return false;
        } else if (!elapsedTime.equals(other.elapsedTime))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        return true;
    }

}
