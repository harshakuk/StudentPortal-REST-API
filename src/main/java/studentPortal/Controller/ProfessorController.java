package studentPortal.Controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import studentPortal.Entity.Professor;
import studentPortal.Exception.CourseNotFoundException;
import studentPortal.ModelAssembler.ProfessorModelAssembler;
import studentPortal.Repository.ProfessorRepository;

@RestController
public class ProfessorController {
	
	private final ProfessorRepository repository;
	private ProfessorModelAssembler professorAssembler;

	ProfessorController(ProfessorRepository repository,ProfessorModelAssembler professorAssembler) {
	    this.repository = repository;
	    this.professorAssembler = professorAssembler;
	  }


	  // Aggregate root
	  // tag::get-aggregate-root[]
	  @GetMapping("/professors")
	  public CollectionModel<EntityModel<Professor>> all() {
		List<EntityModel<Professor>> professors = repository.findAll().stream() //
				      .map(professorAssembler::toModel) //
				      .collect(Collectors.toList());

		return CollectionModel.of(professors, linkTo(methodOn(ProfessorController.class).all()).withSelfRel());
	  }
	  // end::get-aggregate-root[]

	  @PostMapping("/professors")
	  EntityModel<Professor> newProfessor(@RequestBody Professor newProfessor) {
		  Professor returnProfessor = repository.save(newProfessor);
		  return professorAssembler.toModel(returnProfessor);
	  }

	  // Single item
	  
	  @GetMapping("/professors/{id}")
	  public EntityModel<Professor> one(@PathVariable Long id) {
	    
		  Professor returnProfessor = repository.findById(id)
	      .orElseThrow(() -> new CourseNotFoundException(id));
		  return professorAssembler.toModel(returnProfessor);
	  }

	  @PutMapping("/professors/{id}")
	  EntityModel<Professor> replaceProfessor(@RequestBody Professor newProfessor, @PathVariable Long id) {
	    
		  Professor returnProfessor = repository.findById(id)
	      .map(professor -> {
	    	professor.setName(newProfessor.getName());
	    	professor.setDesignation(newProfessor.getDesignation());
	        return repository.save(professor);
	      })
	      .orElseGet(() -> {
	    	  newProfessor.setId(id);
	        return repository.save(newProfessor);
	      });
		  return professorAssembler.toModel(returnProfessor);
	  }

	  @DeleteMapping("/professors/{id}")
	  void deleteProfessor(@PathVariable Long id) {
	    repository.deleteById(id);
	  }

}
