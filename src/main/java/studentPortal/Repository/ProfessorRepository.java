package studentPortal.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import studentPortal.Entity.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

}
