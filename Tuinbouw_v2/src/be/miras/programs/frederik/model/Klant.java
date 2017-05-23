package be.miras.programs.frederik.model;

import java.util.ArrayList;

/**
 * @author Frederik Vanden Bussche
 * 
 *         view model van een klant
 *
 */
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
	
	public Klant(){
	}

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
	 * @return the pctKorting int
	 */
	public int getPctKorting() {
		return pctKorting;
	}

	/**
	 * @param pctKorting
	 *            the pctKorting to set int
	 */
	public void setPctKorting(int pctKorting) {
		this.pctKorting = pctKorting;
	}

	/**
	 * @return the tel int
	 */
	public int getTel() {
		return tel;
	}

	/**
	 * @param tel
	 *            the tel to set int
	 */
	public void setTel(int tel) {
		this.tel = tel;
	}

	/**
	 * @return the tel2 int
	 */
	public int getTel2() {
		return tel2;
	}

	/**
	 * @param tel2
	 *            the tel2 to set int
	 */
	public void setTel2(int tel2) {
		this.tel2 = tel2;
	}

	/**
	 * @return the gsm int
	 */
	public int getGsm() {
		return gsm;
	}

	/**
	 * @param gsm
	 *            the gsm to set int
	 */
	public void setGsm(int gsm) {
		this.gsm = gsm;
	}

	/**
	 * @return the gsm2 int
	 */
	public int getGsm2() {
		return gsm2;
	}

	/**
	 * @param gsm2
	 *            the gsm2 to set int
	 */
	public void setGsm2(int gsm2) {
		this.gsm2 = gsm2;
	}

	/**
	 * @return the email email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set String
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the btwNummer String
	 */
	public String getBtwNummer() {
		return btwNummer;
	}

	/**
	 * @param btwNummer
	 *            the btwNummer to set String
	 */
	public void setBtwNummer(String btwNummer) {
		this.btwNummer = btwNummer;
	}

	/**
	 * @return the isBtwAanrekenen boolean
	 */
	public boolean isBtwAanrekenen() {
		return isBtwAanrekenen;
	}

	/**
	 * @param isBtwAanrekenen
	 *            the isBtwAanrekenen to set boolean
	 */
	public void setBtwAanrekenen(boolean isBtwAanrekenen) {
		this.isBtwAanrekenen = isBtwAanrekenen;
	}

	/**
	 * @return the adreslijst ArrayList<Adres>
	 */
	public ArrayList<Adres> getAdreslijst() {
		return adreslijst;
	}

	/**
	 * @param adreslijst
	 *            the adreslijst to set ArrayList<Adres>
	 */
	public void setAdreslijst(ArrayList<Adres> adreslijst) {
		this.adreslijst = adreslijst;
	}

	@Override
	public boolean isVerschillend(Object o, Object p) {
		boolean isVerschillend = true;
		Klant k1 = (Klant) o;
		Klant k2 = (Klant) p;
		if (k1.getPctKorting() == k2.getPctKorting() && k1.getTel() == k2.getTel() && k1.getTel2() == k2.getTel2()
				&& k1.getGsm() == k2.getGsm() && k1.getGsm2() == k2.getGsm2() && k1.getEmail().equals(k2.getEmail())
				&& k1.getBtwNummer().equals(k2.getBtwNummer()) && k1.isBtwAanrekenen() == k2.isBtwAanrekenen()) {
			isVerschillend = false;
		}
		return isVerschillend;
	}

	
}
