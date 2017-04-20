package be.miras.programs.frederik.model;

import java.util.ArrayList;
import java.util.Date;

public class Werkgever implements Ivergelijk {
	private int id;
	private String naam;
	private String voornaam;
	private Date geboortedatum;
	private String email;
	private String wachtwoord;
	private String gebruikersnaam;
	private int bevoegdheidID;
	private ArrayList<Adres> adreslijst;

	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNaam() {
		return naam;
	}


	public void setNaam(String naam) {
		this.naam = naam;
	}


	public String getVoornaam() {
		return voornaam;
	}


	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}


	public Date getGeboortedatum() {
		return geboortedatum;
	}


	public void setGeboortedatum(Date geboortedatum) {
		this.geboortedatum = geboortedatum;
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

	

	public int getBevoegdheidID() {
		return bevoegdheidID;
	}


	public void setBevoegdheidID(int bevoegdheidID) {
		this.bevoegdheidID = bevoegdheidID;
	}


	public ArrayList<Adres> getAdreslijst() {
		return adreslijst;
	}


	public void setAdreslijst(ArrayList<Adres> adreslijst) {
		this.adreslijst = adreslijst;
	}


	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		Werkgever w1 = (Werkgever) o;
		Werkgever w2 = (Werkgever) p;
		if (w1.getNaam().equals(w2.getNaam()) 
				&& w1.getVoornaam().equals(w2.getVoornaam())
				&& w1.getGeboortedatum().equals(w2.getGeboortedatum())
				&& w1.getEmail().equals(w2.getEmail())
				&& w1.getGebruikersnaam().equals(w2.getGebruikersnaam())) {
			isVerschillend = false;
		}
		return isVerschillend;
	}

}
