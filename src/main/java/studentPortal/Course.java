package studentPortal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "course")
public class Course {
  
  @Column(name = "course_id")
  private @Id @GeneratedValue Long id;
  @Column(name = "name")
  private String name;
  @Column(name = "start_date")
  private String startDate;
  @Column(name = "end_date")
  private String endDate;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name="professor_id",referencedColumnName = "professor_id")
  @JsonIgnoreProperties(allowSetters = true, value = { "courses" })
  private Professor professor;
  @OneToMany(mappedBy="course",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnoreProperties(allowSetters = true, value = { "course" })
  private List<Student> students = new ArrayList<>();


  Course() {}

  Course(String name, String startDate, String endDate) {

    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getStartDate() {
    return this.startDate;
  }
  
  public String getEndDate() {
	return this.endDate;
  }
  
  public Professor getProfessor() {
    return this.professor;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }
  
  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }
  
  public void setProfessor(Professor professor) {
	this.professor = professor;
  }
  
  public List<Student> getStudents() {
	return students;
  }

  public void setStudents(List<Student> students) {
    this.students = students;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (!(o instanceof Course))
      return false;
    Course course = (Course) o;
    return Objects.equals(this.id, course.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.startDate, this.endDate, this.professor);
  }

  @Override
  public String toString() {
    return "Course{" + "id=" + this.id + ", name='" + this.name + '\'' + ", startDate='" + this.startDate + '\'' + ", endDate='" + this.endDate + '\'' + '}';
  }
}
