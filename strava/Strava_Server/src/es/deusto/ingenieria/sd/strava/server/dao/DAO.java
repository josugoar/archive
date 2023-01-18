package es.deusto.ingenieria.sd.strava.server.dao;

import java.util.ArrayList;
import java.util.List;

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

    @SuppressWarnings("unchecked")
    private <T> T getObject(Class<T> cls, String condition) {
        PersistenceManager pm = pmf.getPersistenceManager();
        pm.getFetchPlan().setMaxFetchDepth(3);

        Transaction tx = pm.currentTransaction();
        T product = null;

        try {
            System.out.println("   * Querying a Product: " + condition);

            tx.begin();
            Query<?> query = pm.newQuery("SELECT FROM " + cls.getName() + " WHERE " + condition);
            query.setUnique(true);
            product = (T) query.execute();
            tx.commit();

        } catch (Exception ex) {
            System.out.println("   $ Error retreiving an extent: " + ex.getMessage());
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            pm.close();
        }

        return product;
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

    @Override
    public void storeActivity(Activity activity) {
        storeObject(activity);
    }

    @Override
    public Activity getActivity(String name, String email) {
        return getObject(Activity.class, "name == " + name + " AND email == " + email);
    }

    public boolean containsActivity(String name, String email) {
        return getActivity(name, email) != null;
    }

    @Override
    public List<Activity> getActivities(String condition) {
        return getObjects(Activity.class, condition);
    }

    @Override
    public void deleteActivity(Activity activity) {
        deleteObject(activity);
    }

    @Override
    public void storeAthlete(Athlete athlete) {
        storeObject(athlete);
    }

    @Override
    public Athlete getAthlete(String email) {
        return getObject(Athlete.class, "email == " + email);
    }

    public boolean containsAthlete(String email) {
        return getAthlete(email) != null;
    }

    @Override
    public List<Athlete> getAthletes(String condition) {
        return getObjects(Athlete.class, condition);
    }

    @Override
    public void deleteAthlete(Athlete athlete) {
        deleteObject(athlete);
    }

    @Override
    public void storeChallenge(Challenge challenge) {
        storeObject(challenge);
    }

    @Override
    public Challenge getChallenge(String name) {
        return getObject(Challenge.class, "name == " + name);
    }

    public boolean containsChallenge(String name) {
        return getChallenge(name) != null;
    }

    @Override
    public List<Challenge> getChallenges(String condition) {
        return getObjects(Challenge.class, condition);
    }

    @Override
    public void deleteChallenge(Challenge challenge) {
        deleteObject(challenge);
    }

}
