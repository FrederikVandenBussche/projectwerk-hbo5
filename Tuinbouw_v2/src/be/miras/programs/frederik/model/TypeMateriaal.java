package be.miras.programs.frederik.model;

import java.util.List;

public class TypeMateriaal {
	private int id;
	private String naam;
	private List<Materiaal> materiaalLijst;

	public TypeMateriaal() {
	}

	/**
	 * @return the id int
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id int
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the naam String
	 */
	public String getNaam() {
		return naam;
	}

	/**
	 * @param naam String
	 *            the naam to set
	 */
	public void setNaam(String naam) {
		this.naam = naam;
	}

	/**
	 * @return the materiaalLijst List<Materiaal>
	 */
	public List<Materiaal> getMateriaalLijst() {
		return materiaalLijst;
	}

	/**
	 * @param materiaalLijst List<Materiaal>
	 *            the materiaalLijst to set
	 */
	public void setMateriaalLijst(List<Materiaal> materiaalLijst) {
		this.materiaalLijst = materiaalLijst;
	}

	
}
