package mmm.example.hostelgatepass.repository;

import mmm.example.hostelgatepass.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
    Student findByIdAndPassword(String id, String password);
}