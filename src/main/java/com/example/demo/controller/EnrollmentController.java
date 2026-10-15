package com.example.demo.controller;

import com.example.demo.model.Enrollment;
import com.example.demo.repository.EnrollmentRepository;
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
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentController(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping("/enrollments")
    public List<Enrollment> getAll() {
        return enrollmentRepository.findAll();
    }

    @GetMapping("/enrollments/{id}")
    public ResponseEntity<Enrollment> getById(@PathVariable Long id) {
        return enrollmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/enrollments")
    public ResponseEntity<Enrollment> create(@Valid @RequestBody Enrollment enrollment) {
        Enrollment saved = enrollmentRepository.save(enrollment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/enrollments/{id}")
    public ResponseEntity<Enrollment> update(@PathVariable Long id, @Valid @RequestBody Enrollment enrollment) {
        return enrollmentRepository.findById(id)
                .map(existing -> {
                    enrollment.setId(id);
                    return ResponseEntity.ok(enrollmentRepository.save(enrollment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/enrollments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!enrollmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        enrollmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
