package be.miras.programs.frederik.model;

/**
 * @author Frederik Vanden Bussche
 * 
 *         view model van een adres
 *
 */
public class Adres implements Ivergelijk {
	private int id;
	private String straat;
	private int nummer;
	private String bus;
	private int postcode;
	private String plaats;
	private Integer persoonId;
	private String staticmap;
	private String googlemap;

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
	 * @return the straat String
	 */
	public String getStraat() {
		return straat;
	}

	/**
	 * @param straat
	 *            the straat to set String
	 */
	public void setStraat(String straat) {
		this.straat = straat;
	}

	/**
	 * @return the nummer int
	 */
	public int getNummer() {
		return nummer;
	}

	/**
	 * @param nummer
	 *            the nummer to set int
	 */
	public void setNummer(int nummer) {
		this.nummer = nummer;
	}

	/**
	 * @return the bus String
	 */
	public String getBus() {
		return bus;
	}

	/**
	 * @param bus
	 *            the bus to set String
	 */
	public void setBus(String bus) {
		this.bus = bus;
	}

	/**
	 * @return the postcode int
	 */
	public int getPostcode() {
		return postcode;
	}

	/**
	 * @param postcode
	 *            the postcode to set int
	 */
	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}

	/**
	 * @return the plaats String
	 */
	public String getPlaats() {
		return plaats;
	}

	/**
	 * @param plaats
	 *            the plaats to set String
	 */
	public void setPlaats(String plaats) {
		this.plaats = plaats;
	}

	/**
	 * @return the persoonId Integer
	 */
	public Integer getPersoonId() {
		return persoonId;
	}

	/**
	 * @param persoonId
	 *            the persoonId to set Integer
	 */
	public void setPersoonId(Integer persoonId) {
		this.persoonId = persoonId;
	}

	/**
	 * @return the staticmap String
	 */
	public String getStaticmap() {
		return staticmap;
	}

	/**
	 * @param staticmap
	 *            the staticmap to set String
	 */
	public void setStaticmap(String staticmap) {
		this.staticmap = staticmap;
	}

	/**
	 * @return the googlemap String
	 */
	public String getGooglemap() {
		return googlemap;
	}

	/**
	 * @param googlemap
	 *            the googlemap to set String
	 */
	public void setGooglemap(String googlemap) {
		this.googlemap = googlemap;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		Adres a1 = (Adres) o;
		Adres a2 = (Adres) p;

		if (a1.getStraat().equals(a2.getStraat()) && a1.getNummer() == a2.getNummer() && a1.getBus().equals(a2.getBus())
				&& a1.getPostcode() == a2.getPostcode() && a1.getPlaats().equals(a2.getPlaats())
				&& a1.getPersoonId() == a2.getPersoonId()) {
			isVerschillend = false;
		}
		return isVerschillend;
	}

	@Override
	public String toString() {
		if (this.getBus() != null && !this.getBus().isEmpty()) {
			return this.getStraat() + " " + this.getNummer() + " " + this.getBus() + " " + this.getPostcode() + " "
					+ this.getPlaats();
		} else {
			return this.getStraat() + " " + this.getNummer() + " " + this.getPostcode() + " " + this.getPlaats();
		}
	}

	
}
