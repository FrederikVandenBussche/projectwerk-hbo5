package be.miras.programs.frederik.model;

public class Materiaal implements Ivergelijk{
	private int id;
	private String naam;
	private String eenheidsmaat;
	private double eenheidsprijs;
	private String soort;
	private double hoeveelheid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNaam() {
		return naam;
	}
	public void setNaam(String naam) {
		this.naam = naam;
	}
	public String getEenheidsmaat() {
		return eenheidsmaat;
	}
	public void setEenheidsmaat(String eenheidsmaat) {
		this.eenheidsmaat = eenheidsmaat;
	}
	public double getEenheidsprijs() {
		return eenheidsprijs;
	}
	public void setEenheidsprijs(double eenheidsprijs) {
		this.eenheidsprijs = eenheidsprijs;
	}
	public String getSoort() {
		return soort;
	}
	public void setSoort(String soort) {
		this.soort = soort;
	}
	
	public double getHoeveelheid() {
		return hoeveelheid;
	}
	public void setHoeveelheid(double hoeveelheid) {
		this.hoeveelheid = hoeveelheid;
	}
	
	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		
		Materiaal m1 = (Materiaal) o;
		Materiaal m2 = (Materiaal) p;
		
		if (m1.getNaam().equals(m2.getNaam())
				&& m1.getEenheidsmaat().equals(m2.getEenheidsmaat())
				&& m1.getEenheidsprijs() == m2.getEenheidsprijs()
				&& m1.getSoort().equals(m2.getSoort())){
			
			isVerschillend = false;
		}
		
		return isVerschillend;
	}

	
}
