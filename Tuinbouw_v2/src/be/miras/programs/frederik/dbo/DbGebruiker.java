package be.miras.programs.frederik.dbo;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel gebruiker
 */
public class DbGebruiker {
	private int id;
	private String email;
	private String wachtwoord;
	private String gebruikersnaam;
	private int bevoegdheidId;
	private int persoonId;

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
	 * @return the email String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set String
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the wachtwoord String
	 */
	public String getWachtwoord() {
		return wachtwoord;
	}

	/**
	 * @param wachtwoord
	 *            the wachtwoord to set String
	 */
	public void setWachtwoord(String wachtwoord) {
		this.wachtwoord = wachtwoord;
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
	 * @return the bevoegdheidId int
	 */
	public int getBevoegdheidId() {
		return bevoegdheidId;
	}

	/**
	 * @param bevoegdheidId
	 *            the bevoegdheidId to set int
	 */
	public void setBevoegdheidId(int bevoegdheidId) {
		this.bevoegdheidId = bevoegdheidId;
	}

	/**
	 * @return the persoonId int
	 */
	public int getPersoonId() {
		return persoonId;
	}

	/**
	 * @param persoonId
	 *            the persoonId to set int
	 */
	public void setPersoonId(int persoonId) {
		this.persoonId = persoonId;
	}


}
