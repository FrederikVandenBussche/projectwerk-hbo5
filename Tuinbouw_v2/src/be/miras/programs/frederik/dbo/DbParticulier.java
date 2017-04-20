package be.miras.programs.frederik.dbo;

import be.miras.programs.frederik.model.Ivergelijk;

public class DbParticulier extends DbKlant implements Ivergelijk {
	
	private String naam;
	private String voornaam;

	

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

	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		DbParticulier p1 = (DbParticulier) o;
		DbParticulier p2 = (DbParticulier) p;
		if(p1.getVoornaam().equals(p2.getVoornaam())
				&& p1.getNaam().equals(p2.getNaam())){
			isVerschillend = false;
		}
		return isVerschillend;
	}

}
