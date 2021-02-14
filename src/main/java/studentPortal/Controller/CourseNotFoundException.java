package studentPortal.Controller;

public class CourseNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1009368092558042121L;

	CourseNotFoundException(Long id) {
	    super("Could not find course " + id);
	  }
}
