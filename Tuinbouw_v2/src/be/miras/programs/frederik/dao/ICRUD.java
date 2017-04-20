package be.miras.programs.frederik.dao;

import java.util.List;

public interface ICRUD {
	public boolean voegToe(Object o);
	public Object lees(int id);
	public List<Object> leesAlle();
	public boolean wijzig(Object o);
	public boolean verwijder(int id);
}
