package es.deusto.ingenieria.sd.strava.server.data.domain;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class Activity {

    @PrimaryKey
    private String name;

    @PrimaryKey
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) throws IllegalArgumentException {
        if (email == null) {
            throw new IllegalArgumentException("email == null");
        }

        this.email = email;
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
            throw new IllegalArgumentException("startDate == null");
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
        return true;
    }

}
