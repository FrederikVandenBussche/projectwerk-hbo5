package be.miras.programs.frederik.export;

import java.util.Date;
import java.util.List;

import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Opdracht;
import be.miras.programs.frederik.model.Verplaatsing;

/**
 * @author Frederik Vanden Bussche
 * 
 *         De benodigde factuurdata om een factuur in pdf vorm op te stellen
 *
 */
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

	/**
	 * @return the aanmaakDatum Date
	 */
	public Date getAanmaakDatum() {
		return aanmaakDatum;
	}

	/**
	 * @param aanmaakDatum
	 *            the aanmaakDatum to set Date
	 */
	public void setAanmaakDatum(Date aanmaakDatum) {
		this.aanmaakDatum = aanmaakDatum;
	}

	/**
	 * @return the vervalDatum Date
	 */
	public Date getVervalDatum() {
		return vervalDatum;
	}

	/**
	 * @param vervalDatum
	 *            the vervalDatum to set Date
	 */
	public void setVervalDatum(Date vervalDatum) {
		this.vervalDatum = vervalDatum;
	}

	/**
	 * @return the klantNaam String
	 */
	public String getKlantNaam() {
		return klantNaam;
	}

	/**
	 * @param klantNaam
	 *            the klantNaam to set String
	 */
	public void setKlantNaam(String klantNaam) {
		this.klantNaam = klantNaam;
	}

	/**
	 * @return the adres Adres
	 */
	public Adres getAdres() {
		return adres;
	}

	/**
	 * @param adres
	 *            the adres to set Adres
	 */
	public void setAdres(Adres adres) {
		this.adres = adres;
	}

	/**
	 * @return the opdrachtLijst List<Opdracht>
	 */
	public List<Opdracht> getOpdrachtLijst() {
		return opdrachtLijst;
	}

	/**
	 * @param opdrachtLijst
	 *            the opdrachtLijst to set List<Opdracht>
	 */
	public void setOpdrachtLijst(List<Opdracht> opdrachtLijst) {
		this.opdrachtLijst = opdrachtLijst;
	}

	/**
	 * @return the isBtwAanrekenen boolean
	 */
	public boolean isBtwAanrekenen() {
		return isBtwAanrekenen;
	}

	/**
	 * @param isBtwAanrekenen
	 *            the isBtwAanrekenen to set boolean
	 */
	public void setBtwAanrekenen(boolean isBtwAanrekenen) {
		this.isBtwAanrekenen = isBtwAanrekenen;
	}

	/**
	 * @return the bedrijfsAdres Adres
	 */
	public Adres getBedrijfsAdres() {
		return bedrijfsAdres;
	}

	/**
	 * @param bedrijfsAdres
	 *            the bedrijfsAdres to set Adres
	 */
	public void setBedrijfsAdres(Adres bedrijfsAdres) {
		this.bedrijfsAdres = bedrijfsAdres;
	}

	/**
	 * @return the bedrijfsEmail String
	 */
	public String getBedrijfsEmail() {
		return bedrijfsEmail;
	}

	/**
	 * @param bedrijfsEmail
	 *            the bedrijfsEmail to set String
	 */
	public void setBedrijfsEmail(String bedrijfsEmail) {
		this.bedrijfsEmail = bedrijfsEmail;
	}

	/**
	 * @return the verplaatsingLijst List<Verplaatsing>
	 */
	public List<Verplaatsing> getVerplaatsingLijst() {
		return verplaatsingLijst;
	}

	/**
	 * @param verplaatsingLijst
	 *            the verplaatsingLijst to set List<Verplaatsing>
	 */
	public void setVerplaatsingLijst(List<Verplaatsing> verplaatsingLijst) {
		this.verplaatsingLijst = verplaatsingLijst;
	}

	
}
