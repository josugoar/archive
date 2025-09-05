package es.deusto.ingenieria.sd.strava.server.data.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "true")
public class Athlete {

    @PrimaryKey
    private String email;

    private String name;
    private Double weight;
    private Integer height;
    private Integer restingHeartRate;
    private Integer maxHeartRate;

    private Date dateOfBirth;

    private LoginType loginType;
    private String password;

    @Persistent(defaultFetchGroup = "true")
    private Set<Activity> activities = new HashSet<>();

    @Persistent(defaultFetchGroup = "true", table="ATHLETE_CHALLENGE")
    @Join(column = "email")
    @Element(column = "name")
    private Set<Challenge> challenges = new HashSet<>();

    public boolean hasChallenge(final Challenge challenge) {
        return challenges.contains(challenge);
    }

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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(final Double weight) throws IllegalArgumentException {
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("weight != null && weight < 0");
        }

        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(final Integer height) throws IllegalArgumentException {
        if (height != null && height < 0) {
            throw new IllegalArgumentException("height != null && height < 0");
        }

        this.height = height;
    }

    public Integer getRestingHeartRate() {
        return restingHeartRate;
    }

    public void setRestingHeartRate(final Integer restingHeartRate) throws IllegalArgumentException {
        if (restingHeartRate != null && restingHeartRate < 0) {
            throw new IllegalArgumentException("restingHeartRate != null && restingHeartRate < 0");
        }

        if (maxHeartRate != null && restingHeartRate > maxHeartRate) {
            throw new IllegalArgumentException("maxHeartRate != null && restingHeartRate > maxHeartRate");
        }

        this.restingHeartRate = restingHeartRate;
    }

    public Integer getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(final Integer maxHeartRate) throws IllegalArgumentException {
        if (maxHeartRate != null && maxHeartRate < 0) {
            throw new IllegalArgumentException("maxHeartRate != null && maxHeartRate < 0");
        }

        if (restingHeartRate != null && maxHeartRate < restingHeartRate) {
            throw new IllegalArgumentException("restingHeartRate != null && maxHeartRate < restingHeartRate");
        }

        this.maxHeartRate = maxHeartRate;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final Date dateOfBirth) throws IllegalArgumentException {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("dateOfBirth == null");
        }

        this.dateOfBirth = dateOfBirth;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(final LoginType loginType) throws IllegalArgumentException {
        if (loginType == null) {
            throw new IllegalArgumentException("loginType == null");
        }

        this.loginType = loginType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) throws IllegalArgumentException {
        if (loginType == LoginType.LOCAL && password == null) {
            throw new IllegalArgumentException("loginType == LoginType.LOCAL && password == null");
        }

        this.password = password;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(final Set<Activity> activities) throws IllegalArgumentException {
        if (activities == null) {
            throw new IllegalArgumentException("activities == null");
        }

        if (activities.contains(null)) {
            throw new IllegalArgumentException("activities.contains(null)");
        }

        this.activities = activities;
    }

    public void addActivity(final Activity activity) throws IllegalArgumentException {
        if (activity == null) {
            throw new IllegalArgumentException("activity == null");
        }

        if (activities.contains(activity)) {
            throw new IllegalArgumentException("activities.contains(activity)");
        }

        this.activities.add(activity);
    }

    public Set<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(final Set<Challenge> challenges) throws IllegalArgumentException {
        if (challenges == null) {
            throw new IllegalArgumentException("challenges == null");
        }

        if (challenges.contains(null)) {
            throw new IllegalArgumentException("challenges.contains(null)");
        }

        this.challenges = challenges;
    }

    public void addChallenge(final Challenge challenge) throws IllegalArgumentException {
        if (challenges == null) {
            throw new IllegalArgumentException("challenges == null");
        }

        this.challenges.add(challenge);
    }

    @Override
    public String toString() {
        return "Athlete [name=" + name + ", email=" + email + ", weight=" + weight + ", height=" + height
                + ", restingHeartRate=" + restingHeartRate + ", maxHeartRate=" + maxHeartRate + ", dateOfBirth="
                + dateOfBirth + ", activities=" + activities + ", challenges=" + challenges + ", loginType=" + loginType
                + ", password=" + password + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Athlete other = (Athlete) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

}
