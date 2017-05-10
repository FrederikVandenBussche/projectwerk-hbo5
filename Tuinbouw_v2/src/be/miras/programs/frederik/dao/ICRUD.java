package be.miras.programs.frederik.dao;

import java.util.List;

/**
 * @author Frederik Vanden Bussche
 *
 *CRUD interface voor de data acces objecten
 */
public interface ICRUD {
	
	/**
	 * @param o Object
	 * @return int
	 * 
	 * Voeg een object toe aan de databank
	 * Return de id van dit object
	 */
	public int voegToe(Object o);
	
	/**
	 * @param id int
	 * @return Object
	 * 
	 * select * form databanktabel where id = parameter-id
	 */
	public Object lees(int id);
	
	/**
	 * @return List<Object>
	 * 
	 * select * from databanktabel
	 */
	public List<Object> leesAlle();
	
	/**
	 * @param o Object
	 * 
	 * Wijzig het object in de databanktabel
	 */
	public void wijzig(Object o);
	
	/**
	 * @param id int
	 * 
	 * delete from databanktabel where id = paramter-id
	 */
	public void verwijder(int id);
	
	
}
