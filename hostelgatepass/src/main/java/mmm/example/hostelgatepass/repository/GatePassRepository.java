package mmm.example.hostelgatepass.repository;

import mmm.example.hostelgatepass.model.GatePassRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatePassRepository extends JpaRepository<GatePassRequest, Long> {

    // Find requests by student ID
    List<GatePassRequest> findByStudent_Id(String studentId);

    // Find requests by overall status
    List<GatePassRequest> findByStatus(String status);

    // Find requests by student ID and faculty approval status
    List<GatePassRequest> findByStudent_IdAndFacultyStatus(String studentId, String facultyStatus);

    // Find requests by student ID and admin approval status
    List<GatePassRequest> findByStudent_IdAndAdminStatus(String studentId, String adminStatus);

    // Find requests for a parent by parent ID
    List<GatePassRequest> findByParent_Id(Long parentId);

    // Find requests for a parent by parent ID and parent status
    List<GatePassRequest> findByParent_IdAndParentStatus(Long parentId, String parentStatus);
}
