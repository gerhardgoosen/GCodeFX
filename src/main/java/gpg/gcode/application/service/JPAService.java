package gpg.gcode.application.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import gpg.gcode.application.model.GCodeFXPojo;

public class JPAService {

	private EntityManagerFactory emFactory;
	private EntityManager entityManager;

	public EntityManager initilizeEntityManager() {
		emFactory = Persistence.createEntityManagerFactory("GCodeFX_DB");
		setEntityManager(emFactory.createEntityManager());

		return getEntityManager();
	}

	public EntityManager getEntityManager() {
		if (entityManager == null || emFactory == null) {
			initilizeEntityManager();
		}
		return entityManager;
	}

	public GCodeFXPojo saveModel(GCodeFXPojo data) {

		GCodeFXPojo retVal = data;
		getEntityManager().getTransaction().begin();
		if (data.getId() != null) {
			retVal = getEntityManager().merge(data);
		} else {
			getEntityManager().persist(data);
			retVal = data;
		}
		getEntityManager().getTransaction().commit();

		return retVal;

	}

	public GCodeFXPojo deleteModel(GCodeFXPojo data) {
		getEntityManager().getTransaction().begin();
		getEntityManager().remove(data);
		getEntityManager().getTransaction().commit();
		return null;
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();

		closeConnection();
	}

	public void closeConnection() {
		getEntityManager().close();
		emFactory.close();
	}

	private void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
