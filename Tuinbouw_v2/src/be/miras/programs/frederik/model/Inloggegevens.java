package be.miras.programs.frederik.model;

public class Inloggegevens implements Ivergelijk {
	private int id;
	private String gebruikersnaam;
	private String paswoord;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

	public void setGebruikersnaam(String gebruikersnaam) {
		this.gebruikersnaam = gebruikersnaam;
	}

	public String getPaswoord() {
		return paswoord;
	}

	public void setPaswoord(String paswoord) {
		this.paswoord = paswoord;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		Inloggegevens i1 = (Inloggegevens) o;
		Inloggegevens i2 = (Inloggegevens) p;
		
		if (i1.getGebruikersnaam().equals(i2.getGebruikersnaam())
				&& i1.getPaswoord().equals(i2.getPaswoord())
				) {
			isVerschillend = false;
		}
		return isVerschillend;
	}

}
