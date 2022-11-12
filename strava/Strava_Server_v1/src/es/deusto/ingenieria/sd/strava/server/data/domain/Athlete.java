package es.deusto.ingenieria.sd.strava.server.data.domain;

import java.util.Date;
import java.util.List;

public class Athlete {

    private String name;
    private String email;
    private String password;
    private float weight;
    private int height;
    private int restingHeartrate;
    private int maxHeartrate;
    private Date dateofbirth;
    private List<Activity> activities;
    private List<Challenge> challenges;

    public boolean checkAthlete() {
        return checkEmail() && checkPassword() && checkWeight() && checkHeight() && checkRestingHeartrate() && checkMaxHeartrate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean checkEmail() {
        if (email == null) {
            return false;
        }

        return email.endsWith("@gmail.com");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean checkPassword() {
        return true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkWeight() {
        return weight > 0;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean checkHeight() {
        return height > 0;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean checkRestingHeartrate() {
        if (restingHeartrate > maxHeartrate) {
            return false;
        }

        return restingHeartrate >= 0;
    }

    public int getRestingHeartrate() {
        return restingHeartrate;
    }

    public void setRestingHeartrate(int restingHeartrate) {
        this.restingHeartrate = restingHeartrate;
    }

    public boolean checkMaxHeartrate() {
        if (maxHeartrate < restingHeartrate) {
            return false;
        }

        return maxHeartrate >= 0;
    }

    public int getMaxHeartrate() {
        return maxHeartrate;
    }

    public void setMaxHeartrate(int maxHeartrate) {
        this.maxHeartrate = maxHeartrate;
    }

    public Date getDateofbirth() {
        return dateofbirth;
    }
    public void setDateofbirth(Date dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public void addActivities(Activity activity) {
        this.activities.add(activity);
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    public void addChallenge(Challenge challenge) {
        this.challenges.add(challenge);
    }

    @Override
    public String toString() {
        return "Athlete [name=" + name + ", email=" + email + ", password=" + password + ", weight=" + weight
                + ", height=" + height + ", restingHeartrate=" + restingHeartrate + ", maxHeartrate=" + maxHeartrate
                + ", dateofbirth=" + dateofbirth + ", activities=" + activities + "]";
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
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (Float.floatToIntBits(weight) != Float.floatToIntBits(other.weight))
            return false;
        if (height != other.height)
            return false;
        if (restingHeartrate != other.restingHeartrate)
            return false;
        if (maxHeartrate != other.maxHeartrate)
            return false;
        if (dateofbirth == null) {
            if (other.dateofbirth != null)
                return false;
        } else if (!dateofbirth.equals(other.dateofbirth))
            return false;
        if (activities == null) {
            if (other.activities != null)
                return false;
        } else if (!activities.equals(other.activities))
            return false;
        return true;
    }

}
