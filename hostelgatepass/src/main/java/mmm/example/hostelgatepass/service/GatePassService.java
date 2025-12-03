package mmm.example.hostelgatepass.service;

import mmm.example.hostelgatepass.model.*;
import mmm.example.hostelgatepass.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GatePassService {

    @Autowired
    private GatePassRepository gatePassRepo;

    @Autowired
    private StudentRepository studentRepo;

    // ✅ Student raises a gate pass request
    public GatePassRequest raiseRequest(String studentId, String reason, String type, LocalDateTime returnDate) {
        Student s = studentRepo.findById(studentId).orElse(null);
        if (s == null) return null;

        GatePassRequest req = new GatePassRequest();
        req.setStudent(s);
        req.setReason(reason);
        req.setType(type);
        req.setRequestDate(LocalDateTime.now());
        req.setExpectedReturnDate(returnDate);

        // Initialize statuses
        req.setParentStatus("PENDING");
        req.setFacultyStatus("PENDING");
        req.setAdminStatus("PENDING");
        req.setParentRemarks("");
        req.setFacultyRemarks("");
        req.setAdminRemarks("");

        req.setStatus("PENDING");

        return gatePassRepo.save(req);
    }

    // ✅ Fetch all requests (for admin/faculty view)
    public List<GatePassRequest> getAllRequests() {
        return gatePassRepo.findAll();
    }

    // ✅ Fetch by student
    public List<GatePassRequest> getRequestsByStudentId(String studentId) {
        return gatePassRepo.findByStudent_Id(studentId);
    }

    // ✅ Parent approval
    public GatePassRequest parentApprove(Long id, String remarks, boolean approved) {
        GatePassRequest req = gatePassRepo.findById(id).orElse(null);
        if (req != null) {
            req.setParentStatus(approved ? "APPROVED" : "REJECTED");
            req.setParentRemarks(remarks);
            updateOverallStatus(req);
            gatePassRepo.save(req);
        }
        return req;
    }

    // ✅ Faculty approval
    public GatePassRequest facultyApprove(Long id, String remarks, boolean approved) {
        GatePassRequest req = gatePassRepo.findById(id).orElse(null);
        if (req != null) {
            req.setFacultyStatus(approved ? "APPROVED" : "REJECTED");
            req.setFacultyRemarks(remarks);
            updateOverallStatus(req);
            gatePassRepo.save(req);
        }
        return req;
    }

    // ✅ Admin approval
    public GatePassRequest adminApprove(Long id, String remarks, boolean approved) {
        GatePassRequest req = gatePassRepo.findById(id).orElse(null);
        if (req != null) {
            req.setAdminStatus(approved ? "APPROVED" : "REJECTED");
            req.setAdminRemarks(remarks);
            updateOverallStatus(req);
            gatePassRepo.save(req);
        }
        return req;
    }

    // ✅ Helper method — updates overall gate pass status
    private void updateOverallStatus(GatePassRequest req) {
        if ("REJECTED".equalsIgnoreCase(req.getParentStatus()) ||
                "REJECTED".equalsIgnoreCase(req.getFacultyStatus()) ||
                "REJECTED".equalsIgnoreCase(req.getAdminStatus())) {
            req.setStatus("REJECTED");
        } else if ("APPROVED".equalsIgnoreCase(req.getParentStatus()) &&
                "APPROVED".equalsIgnoreCase(req.getFacultyStatus()) &&
                "APPROVED".equalsIgnoreCase(req.getAdminStatus())) {
            req.setStatus("APPROVED");
        } else {
            req.setStatus("PENDING");
        }
    }

    public GatePassRequest approveRequest(Long id, String remarks) {
        GatePassRequest req = gatePassRepo.findById(id).orElse(null);
        if (req != null) {
            req.setStatus("APPROVED");
            req.setAdminRemarks(remarks);
            gatePassRepo.save(req);
        }
        return req;
    }

    public GatePassRequest rejectRequest(Long id, String remarks) {
        GatePassRequest req = gatePassRepo.findById(id).orElse(null);
        if (req != null) {
            req.setStatus("REJECTED");
            req.setAdminRemarks(remarks);
            gatePassRepo.save(req);
        }
        return req;
    }
}
