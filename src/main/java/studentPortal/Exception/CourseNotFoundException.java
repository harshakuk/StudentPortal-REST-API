package studentPortal.Exception;

public class CourseNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1009368092558042121L;

	public CourseNotFoundException(Long id) {
	    super("Could not find course " + id);
	}
}
