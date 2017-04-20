package be.miras.programs.frederik.model;

import java.util.ArrayList;

public class Klant implements Ivergelijk {
	private int id;
	private int pctKorting;
	private int tel;
	private int tel2;
	private int gsm;
	private int gsm2;
	private String email;
	private String btwNummer;
	private boolean isBtwAanrekenen;
	private ArrayList<Adres> adreslijst;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPctKorting() {
		return pctKorting;
	}
	public void setPctKorting(int pctKorting) {
		this.pctKorting = pctKorting;
	}
	public int getTel() {
		return tel;
	}
	public void setTel(int tel) {
		this.tel = tel;
	}
	public int getTel2() {
		return tel2;
	}
	public void setTel2(int tel2) {
		this.tel2 = tel2;
	}
	public int getGsm() {
		return gsm;
	}
	public void setGsm(int gsm) {
		this.gsm = gsm;
	}
	public int getGsm2() {
		return gsm2;
	}
	public void setGsm2(int gsm2) {
		this.gsm2 = gsm2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBtwNummer() {
		return btwNummer;
	}
	public void setBtwNummer(String btwNummer) {
		this.btwNummer = btwNummer;
	}
	public boolean isBtwAanrekenen() {
		return isBtwAanrekenen;
	}
	public void setBtwAanrekenen(boolean isBtwAanrekenen) {
		this.isBtwAanrekenen = isBtwAanrekenen;
	}
	public ArrayList<Adres> getAdreslijst() {
		return adreslijst;
	}
	public void setAdreslijst(ArrayList<Adres> adreslijst) {
		this.adreslijst = adreslijst;
	}
	
	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		Klant k1 = (Klant) o;
		Klant k2 = (Klant) p;
		if(k1.getPctKorting() == k2.getPctKorting()
				&& k1.getTel() == k2.getTel()
				&& k1.getTel2() == k2.getTel2()
				&& k1.getGsm() == k2.getGsm()
				&& k1.getGsm2() == k2.getGsm2()
				&& k1.getEmail().equals(k2.getEmail())
				&& k1.getBtwNummer().equals(k2.getBtwNummer())
				&& k1.isBtwAanrekenen() == k2.isBtwAanrekenen()
				){
			isVerschillend = false;
		}
		return isVerschillend;
	}
	
	
	
}
