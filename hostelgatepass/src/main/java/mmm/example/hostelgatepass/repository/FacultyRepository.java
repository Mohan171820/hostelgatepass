package mmm.example.hostelgatepass.repository;

import mmm.example.hostelgatepass.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Faculty findByUsername(String username);
}
