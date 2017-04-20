package be.miras.programs.frederik.model;

public class Adres implements Ivergelijk{
	private int id;
	private String straat;
	private int nummer;
	private String bus;
	private int postcode;
	private String plaats;
	private Integer persoonId;
	private String staticmap;
	private String googlemap;

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getStraat() {
		return straat;
	}


	public void setStraat(String straat) {
		this.straat = straat;
	}


	public int getNummer() {
		return nummer;
	}


	public void setNummer(int nummer) {
		this.nummer = nummer;
	}


	public String getBus() {
		return bus;
	}


	public void setBus(String bus) {
		this.bus = bus;
	}


	public int getPostcode() {
		return postcode;
	}


	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}


	public String getPlaats() {
		return plaats;
	}


	public void setPlaats(String plaats) {
		this.plaats = plaats;
	}


	public Integer getPersoonId() {
		return persoonId;
	}


	public void setPersoonId(Integer persoonId) {
		this.persoonId = persoonId;
	}

	

	public String getStaticmap() {
		return staticmap;
	}


	public void setStaticmap(String staticmap) {
		this.staticmap = staticmap;
	}


	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		Adres a1 = (Adres) o;
		Adres a2 = (Adres) p;
		
		if (a1.getStraat().equals(a2.getStraat())
				&& a1.getNummer() == a2.getNummer()
				&& a1.getBus().equals(a2.getBus())
				&& a1.getPostcode() == a2.getPostcode()
				&& a1.getPlaats().equals(a2.getPlaats())
				&& a1.getPersoonId() == a2.getPersoonId()
				) {
			isVerschillend = false;
		}
		return isVerschillend;
	}


	@Override
	public String toString() {
		if (this.getBus() != null && !this.getBus().isEmpty()){
			return this.getStraat() + " " + this.getNummer() + " " + this.getBus() + " "
					+ this.getPostcode() + " " + this.getPlaats();
		} else {
			return this.getStraat() + " " + this.getNummer() + " "
					+ this.getPostcode() + " " + this.getPlaats();
		}
		
	}


	public String getGooglemap() {
		return googlemap;
	}


	public void setGooglemap(String googlemap) {
		this.googlemap = googlemap;
	}
	

	


}
