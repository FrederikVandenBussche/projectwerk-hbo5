package be.miras.programs.frederik.dbo;

import java.util.ArrayList;

import be.miras.programs.frederik.model.Adres;

/**
 * @author Frederik Vanden Bussche
 * 
 *         hibernate object van de tabel klant
 */
public abstract class DbKlant {
	private int id;
	private ArrayList<Adres> adreslijst;

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

	/**
	 * @return String de aanspreeknaam voor de klant
	 * 
	 */
	public String geefAanspreekNaam() {
		String aanspreeknaam = null;

		if (this.getClass().getSimpleName().equals("DbParticulier")) {
			String naam = ((DbParticulier) this).getNaam();
			String voornaam = ((DbParticulier) this).getVoornaam();
			aanspreeknaam = voornaam.concat(" ").concat(naam);

		} else if (this.getClass().getSimpleName().equals("DbBedrijf")) {
			aanspreeknaam = ((DbBedrijf) this).getBedrijfnaam();

		} else {
			aanspreeknaam = " ";
		}

		return aanspreeknaam;
	}

	
}
