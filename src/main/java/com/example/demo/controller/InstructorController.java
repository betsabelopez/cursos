package com.example.demo.controller;

import com.example.demo.model.Instructor;
import com.example.demo.repository.InstructorRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class InstructorController {

    private final InstructorRepository instructorRepository;

    public InstructorController(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @GetMapping("/instructors")
    public List<Instructor> getAll() {
        return instructorRepository.findAll();
    }

    @GetMapping("/instructors/{id}")
    public ResponseEntity<Instructor> getById(@PathVariable Long id) {
        return instructorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/instructors")
    public ResponseEntity<Instructor> create(@Valid @RequestBody Instructor instructor) {
        Instructor saved = instructorRepository.save(instructor);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/instructors/{id}")
    public ResponseEntity<Instructor> update(@PathVariable Long id, @Valid @RequestBody Instructor instructor) {
        return instructorRepository.findById(id)
                .map(existing -> {
                    instructor.setId(id);
                    return ResponseEntity.ok(instructorRepository.save(instructor));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/instructors/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!instructorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        instructorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
