package be.miras.programs.frederik.dao.adapter;

import java.util.List;

import be.miras.programs.frederik.dao.ICRUD;


public class OpdrachtDaoAdapter implements ICRUD {

	@Override
	public boolean voegToe(Object o) {
		
		return false;
	}

	@Override
	public Object lees(int id) {

		return null;
	}

	@Override
	public List<Object> leesAlle() {
		
		return null;
	}

	@Override
	public boolean wijzig(Object o) {
		
		return false;
	}

	@Override
	public boolean verwijder(int id) {
		
		return false;
	}

}
