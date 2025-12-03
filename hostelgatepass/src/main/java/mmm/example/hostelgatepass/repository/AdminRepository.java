package mmm.example.hostelgatepass.repository;

import mmm.example.hostelgatepass.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    // Optional custom finder for flexibility
    Admin findByUsernameAndPassword(String username, String password);
}
