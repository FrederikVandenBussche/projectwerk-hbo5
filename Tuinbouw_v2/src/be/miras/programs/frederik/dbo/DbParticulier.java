package be.miras.programs.frederik.dbo;

import be.miras.programs.frederik.model.Ivergelijk;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel particulier
 */
public class DbParticulier extends DbKlant implements Ivergelijk {

	private String naam;
	private String voornaam;

	/**
	 * @return the naam String
	 */
	public String getNaam() {
		return naam;
	}

	/**
	 * @param naam
	 *            the naam to set String
	 */
	public void setNaam(String naam) {
		this.naam = naam;
	}

	/**
	 * @return the voornaam String
	 */
	public String getVoornaam() {
		return voornaam;
	}

	/**
	 * @param voornaam
	 *            the voornaam to set String
	 */
	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		DbParticulier p1 = (DbParticulier) o;
		DbParticulier p2 = (DbParticulier) p;
		if (p1.getVoornaam().equals(p2.getVoornaam()) && p1.getNaam().equals(p2.getNaam())) {
			isVerschillend = false;
		}
		return isVerschillend;
	}

	
}
