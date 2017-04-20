package be.miras.programs.frederik.model;

import java.util.ArrayList;
import java.util.Date;

/*
 * POJO classe van een personeelslid.
 * Ik heb geen constructor omdat ik deze class gebruik met 
 * het hibernate framework
 */
/**
 * POJO class voor het object Personeel
 * 
 * @author Frederik
 *
 */
public class Personeel implements Ivergelijk {
	private int persoonId;
	private int werknemerId;
	private String voornaam;
	private String naam;
	private Date geboortedatum;
	private Date aanwervingsdatum;
	private double loon;
	private String email;
	private ArrayList<Adres> adreslijst;
	
	

	public int getPersoonId() {
		return persoonId;
	}
	
	public void setPersoonId(int persoonId) {
		this.persoonId = persoonId;
	}
	
	public int getWerknemerId() {
		return werknemerId;
	}
	
	public void setWerknemerId(int werknemerId) {
		this.werknemerId = werknemerId;
	}
	public String getVoornaam() {
		return voornaam;
	}
	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}
	public String getNaam() {
		return naam;
	}
	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<Adres> getAdreslijst() {
		return adreslijst;
	}

	public void setAdreslijst(ArrayList<Adres> adreslijst) {
		this.adreslijst = adreslijst;
	}

	public Date getGeboortedatum() {
		return geboortedatum;
	}

	public void setGeboortedatum(Date geboortedatum) {
		this.geboortedatum = geboortedatum;
	}

	public Date getAanwervingsdatum() {
		return aanwervingsdatum;
	}

	public void setAanwervingsdatum(Date aanwervingsdatum) {
		this.aanwervingsdatum = aanwervingsdatum;
	}

	public double getLoon() {
		return loon;
	}

	public void setLoon(double loon) {
		this.loon = loon;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		Personeel p1 = (Personeel) o;
		Personeel p2 = (Personeel) p;
		if(p1.getVoornaam().equals(p2.getVoornaam())
				&& p1.getNaam().equals(p2.getNaam())
				&& p1.getGeboortedatum().equals(p2.getGeboortedatum())
				&& p1.getAanwervingsdatum().equals(p2.getAanwervingsdatum())
				&& p1.getLoon() == p2.getLoon()
				&& p1.getEmail().equals(p2.getEmail())
				){
			isVerschillend = false;
		}
		return isVerschillend;
	}

	
	

}
