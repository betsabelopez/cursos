package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Course;
import com.example.demo.model.CourseLevel;
import com.example.demo.model.Instructor;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.InstructorRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final InstructorRepository instructorRepository;

    public CourseService(CourseRepository courseRepository,
                        CategoryRepository categoryRepository,
                        InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.instructorRepository = instructorRepository;
    }

    @PostConstruct
    public void init() {
        if (courseRepository.count() > 0) {
            return;
        }

        Category backend = categoryRepository.save(new Category(null, "Backend", "Cursos de lógica, APIs y servicios"));
        Category architecture = categoryRepository.save(new Category(null, "Arquitectura", "Diseño y evolución de sistemas"));

        Instructor noe = instructorRepository.save(new Instructor(null, "Noe", "noe@cursos.com", "Especialista en Spring Boot"));
        Instructor fernanda = instructorRepository.save(new Instructor(null, "Fernanda", "fernanda@cursos.com", "Experta en Java y mentoring"));
        Instructor carlos = instructorRepository.save(new Instructor(null, "Carlos", "carlos@cursos.com", "Arquitecto de microservicios"));

        courseRepository.save(new Course(null, "Spring Boot Fundamentos", "Aprende a crear APIs REST con Spring Boot.", backend, CourseLevel.BEGINNER, noe, 8));
        courseRepository.save(new Course(null, "Java Avanzado", "Profundiza en colecciones, streams y programación funcional.", backend, CourseLevel.INTERMEDIATE, fernanda, 10));
        courseRepository.save(new Course(null, "Microservicios con Spring Cloud", "Diseña aplicaciones distribuidas y escalables.", architecture, CourseLevel.ADVANCED, carlos, 12));
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> getById(Long id) {
        return courseRepository.findById(id);
    }

    public Course save(Course course) {
        if (course.getCategory() != null && course.getCategory().getId() == null) {
            String categoryName = course.getCategory().getName();
            Category category = categoryRepository.findAll().stream()
                    .filter(item -> item.getName().equalsIgnoreCase(categoryName))
                    .findFirst()
                    .orElseGet(() -> categoryRepository.save(course.getCategory()));
            course.setCategory(category);
        }

        if (course.getInstructor() != null && course.getInstructor().getId() == null) {
            String instructorName = course.getInstructor().getName();
            Instructor instructor = instructorRepository.findAll().stream()
                    .filter(item -> item.getName().equalsIgnoreCase(instructorName))
                    .findFirst()
                    .orElseGet(() -> instructorRepository.save(course.getInstructor()));
            course.setInstructor(instructor);
        }

        return courseRepository.save(course);
    }

    public Optional<Course> update(Long id, Course updatedCourse) {
        return courseRepository.findById(id)
                .map(existing -> {
                    updatedCourse.setId(id);
                    return save(updatedCourse);
                });
    }

    public boolean deleteById(Long id) {
        if (!courseRepository.existsById(id)) {
            return false;
        }
        courseRepository.deleteById(id);
        return true;
    }
}
