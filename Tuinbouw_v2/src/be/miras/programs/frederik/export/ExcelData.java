package be.miras.programs.frederik.export;

import java.util.List;

import be.miras.programs.frederik.model.Opdracht;

/**
 * @author Frederik Vanden Bussche
 * 
 *         benodigde date om in de te genereren excel file te plaatsen
 *
 */
public class ExcelData {

	private String klantNaam;
	private List<Opdracht> opdrachtLijst;

	public ExcelData() {
		super();
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

	
}
