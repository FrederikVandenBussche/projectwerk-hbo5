package be.miras.programs.frederik.model;

public class BedrijfKlant extends Klant implements Ivergelijk {
	
	private String naam;
	private String contactpersoon;
	
	public String getNaam() {
		return naam;
	}
	public void setNaam(String naam) {
		this.naam = naam;
	}
	public String getContactpersoon() {
		return contactpersoon;
	}
	public void setContactpersoon(String contactpersoon) {
		this.contactpersoon = contactpersoon;
	}
	
	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		BedrijfKlant k1 = (BedrijfKlant) o;
		BedrijfKlant k2 = (BedrijfKlant) p;
		if(k1.getPctKorting() == k2.getPctKorting()
				&& k1.getTel() == k2.getTel()
				&& k1.getTel2() == k2.getTel2()
				&& k1.getGsm() == k2.getGsm()
				&& k1.getGsm2() == k2.getGsm2()
				&& k1.getEmail().equals(k2.getEmail())
				&& k1.getBtwNummer().equals(k2.getBtwNummer())
				&& k1.isBtwAanrekenen() == k2.isBtwAanrekenen()
				&& k1.getNaam().equals(k2.getNaam())
				&& k1.getContactpersoon().equals(k2.getContactpersoon())
				){
			isVerschillend = false;
		}
		return isVerschillend;
	}
	
}
