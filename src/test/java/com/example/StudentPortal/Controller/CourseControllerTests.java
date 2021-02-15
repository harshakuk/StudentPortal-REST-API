package com.example.StudentPortal.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import studentPortal.Controller.CourseController;
import studentPortal.Entity.Course;
import studentPortal.Repository.CourseRepository;

@WebMvcTest(controllers = CourseController.class)
@ActiveProfiles("test")
public class CourseControllerTests {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private CourseRepository repository;


    @Autowired
    private ObjectMapper objectMapper;
	private List<Course> courseList;
	
	@Before
	public void setUp() {
		this.courseList = new ArrayList<>();
        this.courseList.add(new Course("test 1", "",""));
        this.courseList.add(new Course("test 2", "",""));
        this.courseList.add(new Course("test 3", "",""));
	}
	
	@Test
	public void getCourseList() throws Exception {
		given(repository.findAll()).willReturn(courseList);

        this.mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(courseList.size())));
	}

}
