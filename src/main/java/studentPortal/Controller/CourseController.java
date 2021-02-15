package studentPortal.Controller;
import studentPortal.Entity.Course;
import studentPortal.Exception.CourseNotFoundException;
import studentPortal.Exception.ProfessorNotFoundException;
import studentPortal.Exception.ResourseNotFoundException;
import studentPortal.ModelAssembler.CourseModelAssembler;
import studentPortal.Repository.CourseRepository;
import studentPortal.Repository.ProfessorRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {
	
	 private final CourseRepository repository;
	 private ProfessorRepository professorRepository;
	 private CourseModelAssembler courseAssembler;

	 CourseController(CourseRepository repository, ProfessorRepository professorRepository, CourseModelAssembler courseAssembler) {
	    this.repository = repository;
	    this.professorRepository = professorRepository;
	    this.courseAssembler = courseAssembler;
	  }


	  // Aggregate root
	  // tag::get-aggregate-root[]
	  @GetMapping("/courses")
	  public CollectionModel<EntityModel<Course>> all() {
		List<EntityModel<Course>> courses = repository.findAll().stream() //
			      .map(courseAssembler::toModel) //
			      .collect(Collectors.toList());

	    return CollectionModel.of(courses, linkTo(methodOn(CourseController.class).all()).withSelfRel());
	  }
	  // end::get-aggregate-root[]
	  
	  @GetMapping("/professors/{professorId}/courses")
	    public CollectionModel<EntityModel<Course>> getCoursesByProfessor(@PathVariable(value = "professorId") Long professorId) {
		  List<EntityModel<Course>> courses = repository.findByProfessorId(professorId).stream() //
			      .map(courseAssembler::toModel) //
			      .collect(Collectors.toList());
		  return CollectionModel.of(courses, linkTo(methodOn(CourseController.class).all()).withSelfRel());
	  }
	  
	  @PostMapping("/professors/{professorId}/courses")
	    public EntityModel<Course> createCourse(@PathVariable(value = "professorId") Long professorId,
	        @RequestBody Course course) throws ProfessorNotFoundException {
		  
		   Course returnCourse = professorRepository.findById(professorId).map(professor -> {
	            course.setProfessor(professor);
	            return repository.save(course);
	        }).orElseThrow(() -> new ProfessorNotFoundException(professorId));
		   
		   return courseAssembler.toModel(returnCourse);
	    }
	  
	  @PutMapping("/professors/{professorId}/courses/{courseId}")
	    public EntityModel<Course> updateCourse(@PathVariable(value = "professorId") Long professorId,
	        @PathVariable(value = "courseId") Long courseId, @RequestBody Course courseRequest)
	    throws CourseNotFoundException {
	        if (!professorRepository.existsById(professorId)) {
	            throw new ProfessorNotFoundException(professorId);
	        }
	        
	        Course returnCourse = repository.findById(courseId).map(course -> {
	            course.setName(courseRequest.getName());
	            return repository.save(course);
	        }).orElseThrow(() -> new CourseNotFoundException(courseId));

	        return courseAssembler.toModel(returnCourse);
	    }
	  
	  @DeleteMapping("/professors/{professorId}/courses/{courseId}")
	    public ResponseEntity < ? > deleteCourse(@PathVariable(value = "professorId") Long professorId,
	        @PathVariable(value = "courseId") Long courseId) throws ResourseNotFoundException {
	        return repository.findByIdAndProfessorId(courseId, professorId).map(course -> {
	        	repository.delete(course);
	            return ResponseEntity.ok().build();
	        }).orElseThrow(() -> new ResourseNotFoundException(
	            "Course not found with id " + courseId + " and instructorId " + professorId));
	    }
	  
	  

	  @PostMapping("/courses")
	  EntityModel<Course> newCourse(@RequestBody Course newCourse) {
		Course returnCourse = repository.save(newCourse);
		return courseAssembler.toModel(returnCourse);
	  }

	  // Single item
	  
	  @GetMapping("/courses/{id}")
	
	  public EntityModel<Course> one(@PathVariable Long id) {
	    
	    Course course = repository.findById(id)
	      .orElseThrow(() -> new CourseNotFoundException(id));
	    return courseAssembler.toModel(course);
	  }

	  @PutMapping("/courses/{id}")
	  EntityModel<Course> replaceCourse(@RequestBody Course newCourse, @PathVariable Long id) {
	    
	    Course returnCourse = repository.findById(id)
	      .map(course -> {
	        course.setName(newCourse.getName());
	        course.setStartDate(newCourse.getStartDate());
	        course.setEndDate(newCourse.getEndDate());
	        return repository.save(course);
	      })
	      .orElseGet(() -> {
	    	  newCourse.setId(id);
	        return repository.save(newCourse);
	      });
	    return courseAssembler.toModel(returnCourse);
	  }

	  @DeleteMapping("/courses/{id}")
	  void deleteCourse(@PathVariable Long id) {
	    repository.deleteById(id);
	  }

}
