package studentPortal.Exception;

@SuppressWarnings("serial")
public class ProfessorNotFoundException extends RuntimeException {
	
	public ProfessorNotFoundException(Long id) {
	    super("Could not find professor " + id);
	  }

}
