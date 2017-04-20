package be.miras.programs.frederik.dbo;

import java.util.ArrayList;

import be.miras.programs.frederik.model.Adres;

public abstract class DbKlant {
	private int id;
	private ArrayList<Adres> adreslijst;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Adres> getAdreslijst() {
		return adreslijst;
	}

	public void setAdreslijst(ArrayList<Adres> adreslijst) {
		this.adreslijst = adreslijst;
	}
	
	public String geefAanspreekNaam(){
		String aanspreeknaam = null;
		
		if(this.getClass().getSimpleName().equals("DbParticulier")){
			String naam = ((DbParticulier) this).getNaam();
			String voornaam = ((DbParticulier) this).getVoornaam();
			aanspreeknaam = voornaam.concat(" ").concat(naam);
			
		} else if (this.getClass().getSimpleName().equals("DbBedrijf")){
			aanspreeknaam = ((DbBedrijf) this).getBedrijfnaam();
			
		} else {
			aanspreeknaam = " ";
			
		}
		
		return aanspreeknaam;
	}

}
