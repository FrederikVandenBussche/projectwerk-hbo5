package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.miras.programs.frederik.dao.DbMateriaalDao;
import be.miras.programs.frederik.dao.DbOpdrachtMateriaalDao;
import be.miras.programs.frederik.dao.DbTypeMateriaalDao;
import be.miras.programs.frederik.dao.ICRUD;
import be.miras.programs.frederik.dbo.DbMateriaal;
import be.miras.programs.frederik.dbo.DbOpdrachtMateriaal;
import be.miras.programs.frederik.dbo.DbTypeMateriaal;
import be.miras.programs.frederik.model.Materiaal;

/**
 * @author Frederik Vanden Bussche
 * 
 * Adapter die het model Materiaal
 * koppelt aan de databankobjecten : DbMateriaal, DbTypeMateriaal
 *
 */
public class MateriaalDaoAdapter implements ICRUD {
	
	private DbMateriaalDao dbMateriaalDao;
	private DbTypeMateriaalDao dbTypeMateriaalDao;
	

	public MateriaalDaoAdapter() {
		super();
		this.dbMateriaalDao = new DbMateriaalDao();
		this.dbTypeMateriaalDao = new DbTypeMateriaalDao();
	}

	@Override
	public int voegToe(Object o) {
		Materiaal materiaal = (Materiaal) o;

		DbMateriaal dbMateriaal = new DbMateriaal();

		String naamType = materiaal.getSoort();
		int id = dbTypeMateriaalDao.lees(naamType);

		if (id < 0) {
			// TypeMateriaal bestaat nog niet
			DbTypeMateriaal dbTypeMateriaal = new DbTypeMateriaal();
			dbTypeMateriaal.setNaam(naamType);
			dbTypeMateriaalDao.voegToe(dbTypeMateriaal);
			id = dbTypeMateriaalDao.lees(naamType);
		}

		dbMateriaal.setNaam(materiaal.getNaam());
		dbMateriaal.setEenheid(materiaal.getEenheidsmaat());
		dbMateriaal.setEenheidsprijs(materiaal.getEenheidsprijs());
		dbMateriaal.setTypeMateriaalId(id);

		dbMateriaalDao.voegToe(dbMateriaal);

		return Integer.MIN_VALUE;
	}

	@Override
	public Object lees(int id) {
		return null;
	}

	@Override
	public List<Object> leesAlle() {
		List<Materiaal> materiaalLijst = new ArrayList<Materiaal>();
		List<DbMateriaal> dbMateriaalLijst = new ArrayList<DbMateriaal>();
		List<DbTypeMateriaal> dbTypeMateriaalLijst = new ArrayList<DbTypeMateriaal>();

		DbMateriaal dbMateriaal = null;

		dbMateriaalLijst = (ArrayList<DbMateriaal>) (Object) dbMateriaalDao.leesAlle();
		dbTypeMateriaalLijst = (ArrayList<DbTypeMateriaal>) (Object) dbTypeMateriaalDao.leesAlle();

		Iterator<DbMateriaal> it = dbMateriaalLijst.iterator();
		while (it.hasNext()) {
			DbTypeMateriaal dbTypeMateriaal = null;
			Materiaal materiaal = new Materiaal();

			dbMateriaal = it.next();
			String type = null;

			Iterator<DbTypeMateriaal> typeIt = dbTypeMateriaalLijst.iterator();
			while (typeIt.hasNext()) {
				dbTypeMateriaal = typeIt.next();
				if (dbTypeMateriaal.getId() == dbMateriaal.getTypeMateriaalId()) {
					type = dbTypeMateriaal.getNaam();
				}
			}

			materiaal.setId(dbMateriaal.getId());
			materiaal.setNaam(dbMateriaal.getNaam());
			materiaal.setEenheidsmaat(dbMateriaal.getEenheid());
			materiaal.setEenheidsprijs(dbMateriaal.getEenheidsprijs());
			materiaal.setSoort(type);
			materiaalLijst.add(materiaal);
		}

		List<Object> objectLijst = new ArrayList<Object>(materiaalLijst);
		return objectLijst;
	}

	@Override
	public void wijzig(Object o) {
		Materiaal materiaal = (Materiaal) o;
		DbMateriaal dbMateriaal = new DbMateriaal();

		int typeMateriaalId = dbTypeMateriaalDao.lees(materiaal.getSoort());
		int oudTypeMateriaalId = dbMateriaalDao.geefTypeMateriaalId(materiaal.getId());
		System.out.println("zoek id van : " + materiaal.getSoort() + ": " + typeMateriaalId);
		if (typeMateriaalId < 0) {
			DbTypeMateriaal dtm = new DbTypeMateriaal();

			dtm.setNaam(materiaal.getSoort());

			typeMateriaalId = dbTypeMateriaalDao.voegToe(dtm);
		} 
		
		dbMateriaal.setTypeMateriaalId(typeMateriaalId);
		
		dbMateriaal.setId(materiaal.getId());
		dbMateriaal.setNaam(materiaal.getNaam());
		dbMateriaal.setEenheid(materiaal.getEenheidsmaat());
		dbMateriaal.setEenheidsprijs(materiaal.getEenheidsprijs());

		dbMateriaalDao.wijzig(dbMateriaal);
		
		if (!dbMateriaalDao.isTypeMateriaalKomtVoor(oudTypeMateriaalId)){
			System.out.println("het komt niet meer voor");
			dbTypeMateriaalDao.verwijder(oudTypeMateriaalId);
		}
	}

	@Override
	public void verwijder(int id) {
		int typeMateriaalId = dbMateriaalDao.geefTypeMateriaalId(id);
		dbMateriaalDao.verwijder(id);

		if (!dbMateriaalDao.isTypeMateriaalKomtVoor(typeMateriaalId)) {
			DbTypeMateriaalDao dtmd = new DbTypeMateriaalDao();
			dtmd.verwijder(typeMateriaalId);
		}
	}

	/**
	 * @param opdrachtId int
	 * @return List<Materiaal>
	 * 
	 * returnt List<Materiaal> met een bepaalde opdrachtId
	 */
	public List<Materiaal> leesOpdrachtMateriaal(int opdrachtId) {

		List<Materiaal> lijst = new ArrayList<Materiaal>();

		DbOpdrachtMateriaalDao dbOpdrachtMateriaalDao = new DbOpdrachtMateriaalDao();
		List<DbOpdrachtMateriaal> dbOpdrachtMateriaalLijst = new ArrayList<DbOpdrachtMateriaal>();

		dbOpdrachtMateriaalLijst = dbOpdrachtMateriaalDao.leesWaarOpdrachtId(opdrachtId);

		// itereer over de DbOpdrachtMateriaalLijst waardat de OpdrachtId =
		// opdrachtId
		Iterator<DbOpdrachtMateriaal> it = dbOpdrachtMateriaalLijst.iterator();
		while (it.hasNext()) {
			DbOpdrachtMateriaal om = it.next();
					
			DbMateriaal dbMateriaal = new DbMateriaal();
			Materiaal materiaal = new Materiaal();

			dbMateriaal = (DbMateriaal) dbMateriaalDao.lees(om.getMateriaalId());

			materiaal.setId(dbMateriaal.getId());
			materiaal.setNaam(dbMateriaal.getNaam());
			materiaal.setEenheidsmaat(dbMateriaal.getEenheid());
			materiaal.setEenheidsprijs(dbMateriaal.getEenheidsprijs());

			int typeMateriaalId = dbMateriaal.getTypeMateriaalId();
			DbTypeMateriaal dbTypeMateriaal = (DbTypeMateriaal) dbTypeMateriaalDao.lees(typeMateriaalId);
			materiaal.setSoort(dbTypeMateriaal.getNaam());
			materiaal.setHoeveelheid(om.getVerbruik());

			lijst.add(materiaal);
		}
		return lijst;
	}

	
}
