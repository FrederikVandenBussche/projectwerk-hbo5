package be.miras.programs.frederik.controller;

/**
 * @author Frederik Vanden Bussche
 * 
 *         inputvalidatie om te verifi�ren of de waarden die ingegeven worden in
 *         de formulieren correct zijn
 *
 */
public interface IinputValidatie {
	
	
	/**
	 * @param teValideren
	 *            String[] de te valideren String's
	 * @return String de error message (isEmpty indien er de ingegeven waarden
	 *         geverifi�erd en goedgekeurd zijn.
	 * 
	 */
	String inputValidatie(String[] teValideren);
	
	
}
