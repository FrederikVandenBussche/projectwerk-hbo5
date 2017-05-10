package be.miras.programs.frederik.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Frederik Vanden Bussche
 * 
 * 
 *         view model van Personeel
 */

public class Personeel implements Ivergelijk {
	private int persoonId;
	private int werknemerId;
	private int gebruikerId;
	private String voornaam;
	private String naam;
	private Date geboortedatum;
	private Date aanwervingsdatum;
	private double loon;
	private String email;
	private ArrayList<Adres> adreslijst;

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
	 * @return the werknemerId int
	 */
	public int getWerknemerId() {
		return werknemerId;
	}

	/**
	 * @param werknemerId
	 *            the werknemerId to set int
	 */
	public void setWerknemerId(int werknemerId) {
		this.werknemerId = werknemerId;
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
	 * @return the aanwervingsdatum Date
	 */
	public Date getAanwervingsdatum() {
		return aanwervingsdatum;
	}

	/**
	 * @param aanwervingsdatum
	 *            the aanwervingsdatum to set Date
	 */
	public void setAanwervingsdatum(Date aanwervingsdatum) {
		this.aanwervingsdatum = aanwervingsdatum;
	}

	/**
	 * @return the loon double
	 */
	public double getLoon() {
		return loon;
	}

	/**
	 * @param loon
	 *            the loon to set double
	 */ 
	public void setLoon(double loon) {
		this.loon = loon;
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
	 * @return the adreslijst ArrayList<Adres>
	 */
	public ArrayList<Adres> getAdreslijst() {
		return adreslijst;
	}

	/**
	 * @param adreslijst
	 *            the adreslijst to set ArrayList<Adres>
	 */
	public void setAdreslijst(ArrayList<Adres> adreslijst) {
		this.adreslijst = adreslijst;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		Personeel p1 = (Personeel) o;
		Personeel p2 = (Personeel) p;
		if (p1.getVoornaam().equals(p2.getVoornaam()) && p1.getNaam().equals(p2.getNaam())
				&& p1.getGeboortedatum().equals(p2.getGeboortedatum())
				&& p1.getAanwervingsdatum().equals(p2.getAanwervingsdatum()) && p1.getLoon() == p2.getLoon()
				&& p1.getEmail().equals(p2.getEmail())) {
			isVerschillend = false;
		}
		return isVerschillend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Personeel [persoonId=" + persoonId + ", werknemerId=" + werknemerId + ", voornaam=" + voornaam
				+ ", naam=" + naam + ", geboortedatum=" + geboortedatum + ", aanwervingsdatum=" + aanwervingsdatum
				+ ", loon=" + loon + ", email=" + email + "]";
	}

	
}
