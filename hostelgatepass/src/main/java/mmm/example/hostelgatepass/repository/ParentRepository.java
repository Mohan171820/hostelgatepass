package mmm.example.hostelgatepass.repository;

import mmm.example.hostelgatepass.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    Optional<Parent> findByEmail(String email);

    Optional<Parent> findByParentCode(String parentCode);

    boolean existsByParentCode(String parentCode);

    Optional<Parent> findByEmailAndPassword(String email, String password);
}
