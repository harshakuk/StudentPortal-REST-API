package studentPortal.Controller;

@SuppressWarnings("serial")
public class ProfessorNotFoundException extends RuntimeException {
	
	ProfessorNotFoundException(Long id) {
	    super("Could not find professor " + id);
	  }

}
