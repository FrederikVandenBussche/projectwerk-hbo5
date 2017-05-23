package be.miras.programs.frederik.model;

/**
 * @author Frederik Vanden Bussche
 * 
 *         view model van een materiaal
 *
 */
public class Materiaal implements Ivergelijk {
	
	private int id;
	private String naam;
	private String eenheidsmaat;
	private double eenheidsprijs;
	private String soort;
	private double hoeveelheid;
	
	public Materiaal(){
	}

	/**
	 * @return the id int
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set int
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
	 * @param naam
	 *            the naam to set String
	 */
	public void setNaam(String naam) {
		this.naam = naam;
	}

	/**
	 * @return the eenheidsmaat String
	 */
	public String getEenheidsmaat() {
		return eenheidsmaat;
	}

	/**
	 * @param eenheidsmaat
	 *            the eenheidsmaat to set String
	 */
	public void setEenheidsmaat(String eenheidsmaat) {
		this.eenheidsmaat = eenheidsmaat;
	}

	/**
	 * @return the eenheidsprijs double
	 */
	public double getEenheidsprijs() {
		return eenheidsprijs;
	}

	/**
	 * @param eenheidsprijs
	 *            the eenheidsprijs to set double
	 */
	public void setEenheidsprijs(double eenheidsprijs) {
		this.eenheidsprijs = eenheidsprijs;
	}

	/**
	 * @return the soort String
	 */
	public String getSoort() {
		return soort;
	}

	/**
	 * @param soort
	 *            the soort to set String
	 */
	public void setSoort(String soort) {
		this.soort = soort;
	}

	/**
	 * @return the hoeveelheid double
	 */
	public double getHoeveelheid() {
		return hoeveelheid;
	}

	/**
	 * @param hoeveelheid
	 *            the hoeveelheid to set double
	 */
	public void setHoeveelheid(double hoeveelheid) {
		this.hoeveelheid = hoeveelheid;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;

		Materiaal m1 = (Materiaal) o;
		Materiaal m2 = (Materiaal) p;

		if (m1.getNaam().equals(m2.getNaam()) && m1.getEenheidsmaat().equals(m2.getEenheidsmaat())
				&& m1.getEenheidsprijs() == m2.getEenheidsprijs() && m1.getSoort().equals(m2.getSoort())) {

			isVerschillend = false;
		}

		return isVerschillend;
	}

	
}
