package es.deusto.ingenieria.sd.strava.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.imageio.plugins.tiff.TIFFTagSet;
import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import es.deusto.ingenieria.sd.strava.server.data.domain.Activity;
import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.domain.Challenge;

public class DAO implements IDAO {

    private PersistenceManagerFactory pmf;

    private static IDAO instance;

    private DAO() {
        pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
    }

    public static IDAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }

        return instance;
    }

    private <T> void storeObject(T object) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            System.err.println("   * Storing an object: " + object);
            pm.makePersistent(object);
            tx.commit();
        } catch (Exception ex) {
            System.err.println("   $ Error storing an object: " + ex.getMessage());
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            pm.close();
        }
    }

    private <T> T getObject() {
    }

    private <T> List<T> getObjects(Class<T> cls) {
        PersistenceManager pm = pmf.getPersistenceManager();
        pm.getFetchPlan().setMaxFetchDepth(3);

        Transaction tx = pm.currentTransaction();
        List<T> objects = new ArrayList<>();

        try {
            System.out.println("   * Retrieving an Extent for Objects.");

            tx.begin();
            Extent<T> extent = pm.getExtent(cls, true);

            for (T object : extent) {
                objects.add(object);
            }

            tx.commit();
        } catch (Exception ex) {
            System.out.println("   $ Error retrieving an extent: " + ex.getMessage());
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            pm.close();
        }

        return objects;
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getObjects(Class<T> cls, String condition) {
        PersistenceManager pm = pmf.getPersistenceManager();
        pm.getFetchPlan().setMaxFetchDepth(3);

        Transaction tx = pm.currentTransaction();
        List<T> objects = new ArrayList<>();

        try {
            System.out.println("   * Retrieving an Extent for Objects.");

            tx.begin();
            Extent<T> extent = pm.getExtent(cls, true);
            Query<T> query = pm.newQuery(extent, condition);

            for (T object : (List<T>) query.execute()) {
                objects.add(object);
            }

            tx.commit();
        } catch (Exception ex) {
            System.out.println("   $ Error retrieving an extent: " + ex.getMessage());
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            pm.close();
        }

        return objects;
    }

    private <T> void updateObject(T object) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.makePersistent(object);
            tx.commit();
        } catch (Exception ex) {
            System.out.println("   $ Error updating an Object: " + ex.getMessage());
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            pm.close();
        }
    }

    private <T> void deleteObject(T object) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.deletePersistent(object);
            tx.commit();
        } catch (Exception ex) {
            System.out.println("   $ Error deleting an Object: " + ex.getMessage());
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            pm.close();
        }
    }

    public <T> void deleteAllObjects(Class<T> cls) {
        System.out.println("- Cleaning the DB...");
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query<T> query = pm.newQuery(cls);
            System.out.println(" * '" + query.deletePersistentAll() + "' objects deleted from the DB.");

            tx.commit();
        } catch (Exception ex) {
            System.err.println(" $ Error cleaning the DB: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            if (pm != null && !pm.isClosed()) {
                pm.close();
            }
        }
    }

    @Override
    public void storeActivity(Activity activity) {
        storeObject(activity);
    }

    @Override
    public Activity getActivity() {
        return getObject();
    }

    @Override
    public List<Activity> getActivities() {
        return getObjects(Activity.class);
    }

    @Override
    public List<Activity> getActivities(String condition) {
        return getObjects(Activity.class, condition);
    }

    @Override
    public void updateActivity(Activity activity) {
        updateObject(activity);
    }

    @Override
    public void deleteActivity(Activity activity) {
        deleteObject(activity);
    }

    @Override
    public void deleteActivities() {
        deleteAllObjects(Activity.class);
    }

    @Override
    public void storeAthlete(Athlete athlete) {
        storeObject(athlete);
    }

    @Override
    public Athlete getAthlete() {
        return getObject();
    }

    @Override
    public List<Athlete> getAthletes() {
        return getObjects(Athlete.class);
    }

    @Override
    public List<Athlete> getAthletes(String condition) {
        return getObjects(Athlete.class, condition);
    }

    @Override
    public void updateAthlete(Athlete athlete) {
        updateObject(athlete);
    }

    @Override
    public void deleteAthlete(Athlete athlete) {
        deleteObject(athlete);
    }

    @Override
    public void deleteAthletes() {
        deleteAllObjects(Athlete.class);
    }

    @Override
    public void storeChallenge(Challenge challenge) {
        storeObject(challenge);
    }

    @Override
    public Challenge getChallenge() {
        return getObject();
    }

    @Override
    public List<Challenge> getChallenges() {
        return getObjects(Challenge.class);
    }

    @Override
    public List<Challenge> getChallenges(String condition) {
        return getObjects(Challenge.class, condition);
    }

    @Override
    public void updateChallenge(Challenge challenge) {
        updateObject(challenge);
    }

    @Override
    public void deleteChallenge(Challenge challenge) {
        deleteObject(challenge);
    }

    @Override
    public void deleteChallenges() {
        deleteAllObjects(Challenge.class);
    }

}
