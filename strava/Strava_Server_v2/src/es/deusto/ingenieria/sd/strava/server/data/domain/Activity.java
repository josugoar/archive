package es.deusto.ingenieria.sd.strava.server.data.domain;

import java.util.Date;

public class Activity {

    private String name;
    private double distance;
    private int elapsedTime;
    private SportType type;
    private Date startDate;

    public String getName() {
        return name;
    }

    public void setName(final String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("name == null");
        }

        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(final double distance) throws IllegalArgumentException {
        if (distance < 0) {
            throw new IllegalArgumentException("distance < 0");
        }

        this.distance = distance;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(final int elapsedTime) throws IllegalArgumentException {
        if (elapsedTime < 0) {
            throw new IllegalArgumentException("elapsedTime < 0");
        }

        this.elapsedTime = elapsedTime;
    }

    public SportType getType() {
        return type;
    }

    public void setType(final SportType type) throws IllegalArgumentException {
        if (type == null) {
            throw new IllegalArgumentException("type == null");
        }

        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) throws IllegalArgumentException {
        if (startDate == null) {
            throw new IllegalArgumentException(" == null");
        }

        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "Activity [name=" + name + ", distance=" + distance + ", elapsedTime=" + elapsedTime + ", type=" + type
                + ", startDate=" + startDate + "]";
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Activity other = (Activity) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (Double.doubleToLongBits(distance) != Double.doubleToLongBits(other.distance))
            return false;
        if (elapsedTime != other.elapsedTime)
            return false;
        if (type != other.type)
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        return true;
    }

}
