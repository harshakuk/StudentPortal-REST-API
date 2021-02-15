package studentPortal.ModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import studentPortal.Controller.StudentController;
import studentPortal.Entity.Student;

@Component
public class StudentModelAssembler implements RepresentationModelAssembler<Student, EntityModel<Student>> {

  @Override
  public EntityModel<Student> toModel(Student student) {

    return EntityModel.of(student, //
        linkTo(methodOn(StudentController.class).one(student.getId())).withSelfRel(),
        linkTo(methodOn(StudentController.class).all()).withRel("students"));
  }
}
