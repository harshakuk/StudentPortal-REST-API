package studentPortal.ModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import studentPortal.Controller.ProfessorController;
import studentPortal.Entity.Professor;

@Component
public class ProfessorModelAssembler implements RepresentationModelAssembler<Professor, EntityModel<Professor>> {

  @Override
  public EntityModel<Professor> toModel(Professor professor) {

    return EntityModel.of(professor, //
        linkTo(methodOn(ProfessorController.class).one(professor.getId())).withSelfRel(),
        linkTo(methodOn(ProfessorController.class).all()).withRel("professors"));
  }
}
