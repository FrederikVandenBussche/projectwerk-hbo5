package be.miras.programs.frederik.dao.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import be.miras.programs.frederik.dao.DbMateriaalDao;
import be.miras.programs.frederik.dao.DbOpdrachtMateriaalDao;
import be.miras.programs.frederik.dao.DbTypeMateriaalDao;
import be.miras.programs.frederik.dao.ICRUD;
import be.miras.programs.frederik.dbo.DbMateriaal;
import be.miras.programs.frederik.dbo.DbOpdrachtMateriaal;
import be.miras.programs.frederik.dbo.DbTypeMateriaal;
import be.miras.programs.frederik.model.Materiaal;
import be.miras.programs.frederik.model.TypeMateriaal;

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
		
		int typeMateriaalId = materiaal.getSoortId();
		
		if (typeMateriaalId < 0) {
			// TypeMateriaal bestaat nog niet
			DbTypeMateriaal dbTypeMateriaal = new DbTypeMateriaal();
			dbTypeMateriaal.setNaam(materiaal.getNaam());
			typeMateriaalId = dbTypeMateriaalDao.voegToe(dbTypeMateriaal);
			
		}

		dbMateriaal.setNaam(materiaal.getNaam());
		dbMateriaal.setEenheid(materiaal.getEenheidsmaat());
		dbMateriaal.setEenheidsprijs(materiaal.getEenheidsprijs());
		dbMateriaal.setTypeMateriaalId(typeMateriaalId);

		int materiaalId = dbMateriaalDao.voegToe(dbMateriaal);

		return materiaalId;
	}

	@Override
	public Object lees(int id) {
		return null;
	}

	@Override
	public List<Object> leesAlle() {
		List<TypeMateriaal> typeMateriaalLijst = new ArrayList<TypeMateriaal>();
		List<DbMateriaal> dbMateriaalLijst = new ArrayList<DbMateriaal>();
		List<DbTypeMateriaal> dbTypeMateriaalLijst = new ArrayList<DbTypeMateriaal>();

		dbMateriaalLijst = (ArrayList<DbMateriaal>) (Object) dbMateriaalDao.leesAlle();
		dbTypeMateriaalLijst = (ArrayList<DbTypeMateriaal>) (Object) dbTypeMateriaalDao.leesAlle();

		Iterator<DbTypeMateriaal> dbTypeMateriaalIt = dbTypeMateriaalLijst.iterator();
		while (dbTypeMateriaalIt.hasNext()) {
			DbTypeMateriaal dbTypeMateriaal = dbTypeMateriaalIt.next();
			
			TypeMateriaal typeMateriaal = new TypeMateriaal();
			typeMateriaal.setId(dbTypeMateriaal.getId());
			typeMateriaal.setNaam(dbTypeMateriaal.getNaam());
			List<Materiaal> materiaalLijst = new ArrayList<Materiaal>();

			Iterator<DbMateriaal> dbMateriaalIt = dbMateriaalLijst.iterator();
			while (dbMateriaalIt.hasNext()) {
				DbMateriaal dbMateriaal = dbMateriaalIt.next();
				if (dbMateriaal.getTypeMateriaalId() == dbTypeMateriaal.getId()) {
					
					Materiaal materiaal = new Materiaal();
					materiaal.setId(dbMateriaal.getId());
					materiaal.setNaam(dbMateriaal.getNaam());
					materiaal.setEenheidsmaat(dbMateriaal.getEenheid());
					materiaal.setEenheidsprijs(dbMateriaal.getEenheidsprijs());
					materiaal.setSoortId(dbTypeMateriaal.getId());
					materiaal.setSoort(dbTypeMateriaal.getNaam());
					
					materiaalLijst.add(materiaal);
					dbMateriaalIt.remove();
				}
			}
			
			typeMateriaal.setMateriaalLijst(materiaalLijst);
			typeMateriaalLijst.add(typeMateriaal);
		}
		
		List<Object> objectLijst = new ArrayList<Object>(typeMateriaalLijst);
		return objectLijst;
	}

	@Override
	public void wijzig(Object o) {
		Materiaal materiaal = (Materiaal) o;
		DbMateriaal dbMateriaal = new DbMateriaal();

		int typeMateriaalId = materiaal.getSoortId();
		int oudTypeMateriaalId = dbMateriaalDao.geefTypeMateriaalId(materiaal.getId());
		
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
			dbTypeMateriaalDao.verwijder(oudTypeMateriaalId);
		}
	}

	@Override
	public void verwijder(int id) {
		int typeMateriaalId = dbMateriaalDao.geefTypeMateriaalId(id);
		dbMateriaalDao.verwijder(id);
		
		if (!dbMateriaalDao.isTypeMateriaalKomtVoor(typeMateriaalId)){
			dbTypeMateriaalDao.verwijder(typeMateriaalId);
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

	/**
	 * @param materiaal Materiaal
	 * @return int
	 * 
	 * return de id van dit Materiaal
	 * Als deze record niet bestaat, return Integer.min_value
	 */
	public int haalId(Materiaal materiaal) {
		int id = Integer.MIN_VALUE;
		
		DbMateriaalDao dbMateriaalDao = new DbMateriaalDao();
		DbMateriaal dbMateriaal = new DbMateriaal();
		
		dbMateriaal.setNaam(materiaal.getNaam());
		dbMateriaal.setEenheid(materiaal.getEenheidsmaat());
		dbMateriaal.setTypeMateriaalId(materiaal.getSoortId());
		
		id = dbMateriaalDao.haalId(dbMateriaal);
		
		return id;
	}

	/**
	 * @param typeMateriaalId int
	 * 
	 * Verwijder het typeMateriaal
	 * en alle materialen met geparameteriseerde typeMateriaalId.
	 */
	public void verwijderTypeMateriaal(int typeMateriaalId) {
		DbMateriaalDao dbMateriaalDao = new DbMateriaalDao();
		DbTypeMateriaalDao dbTypeMateriaalDao = new DbTypeMateriaalDao();
		
		dbMateriaalDao.verwijderWaarTypeMateriaalId(typeMateriaalId);
		dbTypeMateriaalDao.verwijder(typeMateriaalId);
		
	}

	
}
