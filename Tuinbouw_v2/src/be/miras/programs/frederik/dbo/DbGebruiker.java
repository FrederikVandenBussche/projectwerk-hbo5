package be.miras.programs.frederik.dbo;

public class DbGebruiker {
	private int id;
	private String email;
	private String wachtwoord;
	private String gebruikersnaam;
	private int bevoegdheidId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWachtwoord() {
		return wachtwoord;
	}

	public void setWachtwoord(String wachtwoord) {
		this.wachtwoord = wachtwoord;
	}

	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

	public void setGebruikersnaam(String gebruikersnaam) {
		this.gebruikersnaam = gebruikersnaam;
	}

	public int getBevoegdheidId() {
		return bevoegdheidId;
	}

	public void setBevoegdheidId(int bevoegdheidId) {
		this.bevoegdheidId = bevoegdheidId;
	}

}
