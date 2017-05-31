package be.miras.programs.frederik.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Frederik Vanden Bussche
 * 
 *         view model van een werkgever
 *
 */
public class Werkgever implements Ivergelijk {
	
	private int id;
	private String naam;
	private String voornaam;
	private Date geboortedatum;
	private String email;
	private String wachtwoord;
	private String gebruikersnaam;
	private int bevoegdheidID;
	private int persoonId;
	private int gebruikerId;
	private ArrayList<Adres> adreslijst;
	
	
	public Werkgever(){
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

	/**
	 * @return the geboortedatum Date
	 */
	public Date getGeboortedatum() {
		return geboortedatum;
	}

	/**
	 * @param geboortedatum
	 *            the geboortedatum to set Date
	 */
	public void setGeboortedatum(Date geboortedatum) {
		this.geboortedatum = geboortedatum;
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
	 * @return the bevoegdheidID int
	 */
	public int getBevoegdheidID() {
		return bevoegdheidID;
	}

	/**
	 * @param bevoegdheidID
	 *            the bevoegdheidID to set int
	 */
	public void setBevoegdheidID(int bevoegdheidID) {
		this.bevoegdheidID = bevoegdheidID;
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

	/**
	 * @return the gebruikerId int
	 */
	public int getGebruikerId() {
		return gebruikerId;
	}

	/**
	 * @param gebruikerId
	 *            the gebruikerId to set int
	 */
	public void setGebruikerId(int gebruikerId) {
		this.gebruikerId = gebruikerId;
	}

	/**
	 * @return the adreslijst ArrayList<Adres>
	 */
	public ArrayList<Adres> getAdreslijst() {
		return adreslijst;
	}

	/**
	 * @param adreslijst
	 *            the adreslijst to set ArrayLijst<Adres>
	 */
	public void setAdreslijst(ArrayList<Adres> adreslijst) {
		this.adreslijst = adreslijst;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		Werkgever w1 = (Werkgever) o;
		Werkgever w2 = (Werkgever) p;

		if (w1.getNaam().equals(w2.getNaam()) && w1.getVoornaam().equals(w2.getVoornaam())
				&& w1.getGeboortedatum().equals(w2.getGeboortedatum()) && w1.getEmail().equals(w2.getEmail())
				&& w1.getGebruikersnaam().equals(w2.getGebruikersnaam())) {
			isVerschillend = false;
		}
		return isVerschillend;
	}


}
