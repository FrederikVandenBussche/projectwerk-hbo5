package be.miras.programs.frederik.model;

/**
 * @author Frederik Vanden Bussche
 * 
 *         view model van inloggegevens
 *
 */
public class Inloggegevens implements Ivergelijk {
	
	private int id;
	private String gebruikersnaam;
	private String paswoord;
	
	public Inloggegevens(){
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
	 * @return the gebruikersnaam String
	 */
	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

	/**
	 * @param gebruikersnaam
	 *            the gebruikersnaam to set String
	 */
	public void setGebruikersnaam(String gebruikersnaam) {
		this.gebruikersnaam = gebruikersnaam;
	}

	/**
	 * @return the paswoord String
	 */
	public String getPaswoord() {
		return paswoord;
	}

	/**
	 * @param paswoord
	 *            the paswoord to set String
	 */
	public void setPaswoord(String paswoord) {
		this.paswoord = paswoord;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		Inloggegevens i1 = (Inloggegevens) o;
		Inloggegevens i2 = (Inloggegevens) p;

		if (i1.getGebruikersnaam().equals(i2.getGebruikersnaam()) && i1.getPaswoord().equals(i2.getPaswoord())) {
			isVerschillend = false;
		}
		return isVerschillend;
	}

	
}
