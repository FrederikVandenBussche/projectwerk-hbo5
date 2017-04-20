package be.miras.programs.frederik.model;

import java.util.Date;

public class Planning {
	private int id;
	private String werknemer;
	private int werknemerId;
	private Date beginuur;
	private Date einduur;
	private int isAanwezig;
	private double aantalUren;
	private double aantalKm;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWerknemer() {
		return werknemer;
	}
	public void setWerknemer(String werknemer) {
		this.werknemer = werknemer;
	}
	
	public int getWerknemerId() {
		return werknemerId;
	}
	
	public void setWerknemerId(int werknemerId) {
		this.werknemerId = werknemerId;
	}
	
	public Date getBeginuur() {
		return beginuur;
	}
	public void setBeginuur(Date beginuur) {
		this.beginuur = beginuur;
	}
	public Date getEinduur() {
		return einduur;
	}
	public void setEinduur(Date einduur) {
		this.einduur = einduur;
	}
	public int getIsAanwezig() {
		return isAanwezig;
	}
	public void setIsAanwezig(int isAanwezig) {
		this.isAanwezig = isAanwezig;
	}
	public double getAantalUren() {
		return aantalUren;
	}
	public void setAantalUren() {
		if (this.einduur == null || this.beginuur == null){
			this.aantalUren = Double.MIN_VALUE;
		} else {
			double aantalMilliseconden = this.einduur.getTime() - this.beginuur.getTime();
		    double uren = ((aantalMilliseconden / 1000)/60)/60;
		    int afgerond2decimalen = (int) (uren * 100);
		    this.aantalUren = (double)afgerond2decimalen / 100;
		}
	}
	
	public void setAantalUren(double aantalUren){
		this.aantalUren = aantalUren;
	}
	
	public double getAantalKm() {
		return aantalKm;
	}
	
	public void setAantalKm(double aantalKm) {
		this.aantalKm = aantalKm;
	}

	
	
	

}
