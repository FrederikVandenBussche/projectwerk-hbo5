package be.miras.programs.frederik.export;

import java.util.List;

import be.miras.programs.frederik.model.Opdracht;

public class ExcelData {

	private String klantNaam;
	private List<Opdracht> opdrachtLijst;

	public ExcelData() {
		super();
	}

	public String getKlantNaam() {
		return klantNaam;
	}

	public void setKlantNaam(String klantNaam) {
		this.klantNaam = klantNaam;
	}

	public List<Opdracht> getOpdrachtLijst() {
		return opdrachtLijst;
	}

	public void setOpdrachtLijst(List<Opdracht> opdrachtLijst) {
		this.opdrachtLijst = opdrachtLijst;
	}

}
