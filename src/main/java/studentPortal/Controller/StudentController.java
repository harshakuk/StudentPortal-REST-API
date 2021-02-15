package studentPortal.Controller;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import studentPortal.Entity.Student;
import studentPortal.Exception.ResourseNotFoundException;
import studentPortal.ModelAssembler.StudentModelAssembler;
import studentPortal.Repository.CourseRepository;
import studentPortal.Repository.StudentRepository;

@RestController
public class StudentController {
	
	private final CourseRepository courseRepository;
	private final StudentRepository studentRepository;
	private final StudentModelAssembler studentModelAssembler;

	StudentController(CourseRepository courseRepository, StudentRepository studentRepository, StudentModelAssembler studentModelAssembler) {
	  this.courseRepository = courseRepository;
	  this.studentRepository = studentRepository;
	  this.studentModelAssembler = studentModelAssembler;
    }
	
	 // Aggregate root
	  // tag::get-aggregate-root[]
	  @GetMapping("/students")
	  public CollectionModel<EntityModel<Student>> all() {
	   List<EntityModel<Student>> students = studentRepository.findAll().stream() //
			      .map(studentModelAssembler::toModel) //
			      .collect(Collectors.toList());

	    return CollectionModel.of(students, linkTo(methodOn(StudentController.class).all()).withSelfRel());
	  }
	  // end::get-aggregate-root[]

	  @PostMapping("/students")
	  EntityModel<Student> newStudent(@RequestBody Student newStudent) {
		Student returnStudent = studentRepository.save(newStudent);
		return studentModelAssembler.toModel(returnStudent);
	  }

	  // Single item
	  
	  @GetMapping("/students/{id}")
	  public EntityModel<Student> one(@PathVariable Long id) {
	    
		  Student returnStudent = studentRepository.findById(id)
	      .orElseThrow(() -> new ResourseNotFoundException("student not found"));
		  return studentModelAssembler.toModel(returnStudent);
	  }

	  @PutMapping("/students/{id}")
	  EntityModel<Student> replaceStudent(@RequestBody Student newStudent, @PathVariable Long id) {
	    
		  Student returnStudent = studentRepository.findById(id)
	      .map(student -> {
	        student.setFirstName(student.getFirstName());
	        student.setLastName(student.getLastName());
	        return studentRepository.save(student);
	      })
	      .orElseGet(() -> {
	    	  newStudent.setId(id);
	        return studentRepository.save(newStudent);
	      });
		  return studentModelAssembler.toModel(returnStudent);
	  }

	  @DeleteMapping("/students/{id}")
	  void deleteStudent(@PathVariable Long id) {
		studentRepository.deleteById(id);
	  }
	  
	  @GetMapping("/courses/{courseId}/students")
	    public CollectionModel<EntityModel<Student>> getStudentsByCourse(@PathVariable(value = "courseId") Long courseId) {
		  List<EntityModel<Student>> students = studentRepository.findByCourseId(courseId).stream() //
			      .map(studentModelAssembler::toModel) //
			      .collect(Collectors.toList());
		  return CollectionModel.of(students, linkTo(methodOn(StudentController.class).all()).withSelfRel());
	  }
	  
	  @PostMapping("/courses/{courseId}/students")
	    public EntityModel<Student> registerStudent(@PathVariable(value = "courseId") Long courseId,
	        @RequestBody Student student) throws ResourseNotFoundException {
		  Student returnStudent = courseRepository.findById(courseId).map(course -> {
	            student.setCourse(course);
	            return studentRepository.save(student);
	        }).orElseThrow(() -> new ResourseNotFoundException("Course not found exception"));
		  return studentModelAssembler.toModel(returnStudent);
	    }
	  
	  @PutMapping("/courses/{courseId}/student/{studentId}")
	    public EntityModel<Student> updateStudent(@PathVariable(value = "courseId") Long courseId,
	        @PathVariable(value = "studentId") Long studentId, @RequestBody Student studentRequest)
	    throws ResourseNotFoundException {
	        if (!courseRepository.existsById(courseId)) {
	            throw new ResourseNotFoundException("Course " + courseId + " not found");
	        }

	        Student returnStudent = studentRepository.findById(studentId).map(student -> {
	        	student.setFirstName(studentRequest.getFirstName());
	        	student.setLastName(studentRequest.getLastName());
	            return studentRepository.save(student);
	        }).orElseThrow(() -> new ResourseNotFoundException("Student " + studentId + " not found"));
	        
	        return studentModelAssembler.toModel(returnStudent);
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
