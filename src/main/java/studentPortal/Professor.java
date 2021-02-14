package studentPortal;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "professor")
public class Professor {
	  @Column(name = "professor_id")
	  private @Id @GeneratedValue Long id;
	  @Column(name = "name")
	  private String name;
	  @Column(name = "designation")
	  private String designation;
	  @Column(name = "professor")
	  @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
	  @JsonIgnoreProperties(allowSetters = true, value = { "professor" })
	  private List<Course> courses;


	  Professor() {}

	  Professor(String name, String designation) {

	    this.name = name;
	    this.designation = designation;
	  }

	  public Long getId() {
	    return this.id;
	  }

	  public String getName() {
	    return this.name;
	  }

	  public String getDesignation() {
	    return this.designation;
	  }

	  public void setId(Long id) {
	    this.id = id;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

	  public void setDesignation(String designation) {
	    this.designation = designation;
	  }
	  
	  public List<Course> getCourses() {
		return courses;
	  }

	  public void setCourses(List<Course> courses) {
		this.courses = courses;
	  }

	  @Override
	  public boolean equals(Object o) {

	    if (this == o)
	      return true;
	    if (!(o instanceof Professor))
	      return false;
	    Professor professor = (Professor) o;
	    return Objects.equals(this.id, professor.id);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(this.id, this.name, this.designation);
	  }

	  @Override
	  public String toString() {
	    return "Professor{" + "id=" + this.id + ", name='" + this.name + '\'' + ", designation='" + this.designation + '\'' + '}';
	  }
}
