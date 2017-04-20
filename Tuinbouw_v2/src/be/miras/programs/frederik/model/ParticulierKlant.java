package be.miras.programs.frederik.model;

public class ParticulierKlant extends Klant implements Ivergelijk {
	private String voornaam;
	private String familienaam;
	
	
	public String getVoornaam() {
		return voornaam;
	}
	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}
	public String getFamilienaam() {
		return familienaam;
	}
	public void setFamilienaam(String familienaam) {
		this.familienaam = familienaam;
	}
	
	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		ParticulierKlant k1 = (ParticulierKlant) o;
		ParticulierKlant k2 = (ParticulierKlant) p;
		if(k1.getPctKorting() == k2.getPctKorting()
				&& k1.getTel() == k2.getTel()
				&& k1.getTel2() == k2.getTel2()
				&& k1.getGsm() == k2.getGsm()
				&& k1.getGsm2() == k2.getGsm2()
				&& k1.getEmail().equals(k2.getEmail())
				&& k1.getBtwNummer().equals(k2.getBtwNummer())
				&& k1.isBtwAanrekenen() == k2.isBtwAanrekenen()
				&& k1.getVoornaam().equals(k2.getVoornaam())
				&& k1.getFamilienaam().equals(k2.getFamilienaam())
				){
			isVerschillend = false;
		}
		return isVerschillend;
	}
	

}
