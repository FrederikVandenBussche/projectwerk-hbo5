package be.miras.programs.frederik.export;

import java.util.Date;
import java.util.List;

import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.Verplaatsing;

public class Factuur {
	private Date aanmaakDatum;
	private Date vervalDatum;
	private String klantNaam;
	private Adres adres;
	private List<Opdracht> opdrachtLijst;
	private boolean isBtwAanrekenen;
	private Adres bedrijfsAdres;
	private String bedrijfsEmail;
	private List<Verplaatsing> verplaatsingLijst; 

	public Factuur() {
		super();
	}

	public Date getAanmaakDatum() {
		return aanmaakDatum;
	}

	public void setAanmaakDatum(Date aanmaakDatum) {
		this.aanmaakDatum = aanmaakDatum;
	}

	public Date getVervalDatum() {
		return vervalDatum;
	}

	public void setVervalDatum(Date vervalDatum) {
		this.vervalDatum = vervalDatum;
	}

	public String getKlantNaam() {
		return klantNaam;
	}

	public void setKlantNaam(String klantNaam) {
		this.klantNaam = klantNaam;
	}

	public Adres getAdres() {
		return adres;
	}

	public void setAdres(Adres adres) {
		this.adres = adres;
	}

	public List<Opdracht> getOpdrachtLijst() {
		return opdrachtLijst;
	}

	public void setOpdrachtLijst(List<Opdracht> opdrachtLijst) {
		this.opdrachtLijst = opdrachtLijst;
	}

	public boolean isBtwAanrekenen() {
		return isBtwAanrekenen;
	}

	public void setBtwAanrekenen(boolean isBtwAanrekenen) {
		this.isBtwAanrekenen = isBtwAanrekenen;
	}

	public Adres getBedrijfsAdres() {
		return bedrijfsAdres;
	}

	public void setBedrijfsAdres(Adres bedrijfsAdres) {
		this.bedrijfsAdres = bedrijfsAdres;
	}

	public String getBedrijfsEmail() {
		return bedrijfsEmail;
	}

	public void setBedrijfsEmail(String bedrijfsEmail) {
		this.bedrijfsEmail = bedrijfsEmail;
	}

	public List<Verplaatsing> getVerplaatsingLijst() {
		return verplaatsingLijst;
	}

	public void setVerplaatsingLijst(List<Verplaatsing> verplaatsingLijst) {
		this.verplaatsingLijst = verplaatsingLijst;
	}

	
	
	

}
