package studentPortal.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import studentPortal.Course;
import studentPortal.CourseRepository;
import studentPortal.Professor;
import studentPortal.ProfessorRepository;
import studentPortal.Student;
import studentPortal.StudentRepository;

@RestController
public class StudentController {
	
	private final CourseRepository courseRepository;
	private final StudentRepository studentRepository;

	StudentController(CourseRepository courseRepository, StudentRepository studentRepository) {
	    this.courseRepository = courseRepository;
	    this.studentRepository = studentRepository;
    }
	
	 // Aggregate root
	  // tag::get-aggregate-root[]
	  @GetMapping("/students")
	  List<Student> all() {
	    return studentRepository.findAll();
	  }
	  // end::get-aggregate-root[]

	  @PostMapping("/students")
	  Student newStudent(@RequestBody Student newStudent) {
	    return studentRepository.save(newStudent);
	  }

	  // Single item
	  
	  @GetMapping("/students/{id}")
	  Student one(@PathVariable Long id) {
	    
	    return studentRepository.findById(id)
	      .orElseThrow(() -> new ResourseNotFoundException("student not found"));
	  }

	  @PutMapping("/students/{id}")
	  Student replaceStudent(@RequestBody Student newStudent, @PathVariable Long id) {
	    
	    return studentRepository.findById(id)
	      .map(student -> {
	        student.setFirstName(student.getFirstName());
	        student.setLastName(student.getLastName());
	        return studentRepository.save(student);
	      })
	      .orElseGet(() -> {
	    	  newStudent.setId(id);
	        return studentRepository.save(newStudent);
	      });
	  }

	  @DeleteMapping("/students/{id}")
	  void deleteStudent(@PathVariable Long id) {
		studentRepository.deleteById(id);
	  }
	  
	  @GetMapping("/courses/{courseId}/students")
	    public List <Student> getStudentsByCourse(@PathVariable(value = "courseId") Long courseId) {
	        return studentRepository.findByCourseId(courseId);
	  }
	  
	  @PostMapping("/courses/{courseId}/students")
	    public Student registerStudent(@PathVariable(value = "courseId") Long courseId,
	        @RequestBody Student student) throws ResourseNotFoundException {
	        return courseRepository.findById(courseId).map(course -> {
	            student.setCourse(course);
	            return studentRepository.save(student);
	        }).orElseThrow(() -> new ResourseNotFoundException("Course not found exception"));
	    }
	  
	  @PutMapping("/courses/{courseId}/student/{studentId}")
	    public Student updateStudent(@PathVariable(value = "courseId") Long courseId,
	        @PathVariable(value = "studentId") Long studentId, @RequestBody Student studentRequest)
	    throws ResourseNotFoundException {
	        if (!courseRepository.existsById(courseId)) {
	            throw new ResourseNotFoundException("Course " + courseId + " not found");
	        }

	        return studentRepository.findById(studentId).map(student -> {
	        	student.setFirstName(studentRequest.getFirstName());
	        	student.setLastName(studentRequest.getLastName());
	            return studentRepository.save(student);
	        }).orElseThrow(() -> new ResourseNotFoundException("Student " + studentId + " not found"));
	    }
	  
	  @DeleteMapping("/courses/{courseId}/student/{studentId}")
	    public ResponseEntity < ? > deleteStudent(@PathVariable(value = "courseId") Long courseId,
	        @PathVariable(value = "studentId") Long studentId) throws ResourseNotFoundException {
	        return studentRepository.findByIdAndCourseId(studentId, courseId).map(student -> {
	        	studentRepository.delete(student);
	            return ResponseEntity.ok().build();
	        }).orElseThrow(() -> new ResourseNotFoundException(
	            "Student not found with id " + studentId + " and courseId " + courseId));
	    }
	  


}
