package studentPortal.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import studentPortal.Entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
	
	List<Student> findByCourseId(Long courseId);
	Optional<Student> findByIdAndCourseId(Long id, Long courseId);
  
}
