package mmm.example.hostelgatepass.service;

import mmm.example.hostelgatepass.model.GatePassRequest;
import mmm.example.hostelgatepass.model.Parent;
import mmm.example.hostelgatepass.repository.GatePassRepository;
import mmm.example.hostelgatepass.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ParentService {

    @Autowired
    private ParentRepository parentRepo;

    @Autowired
    private GatePassRepository gatePassRepo;

    // ===================== LOGIN ====================
    public Parent validateLogin(String email, String password) {
        Optional<Parent> optionalParent = parentRepo.findByEmailAndPassword(email, password);
        return optionalParent.orElse(null); // returns null if login fails
    }

    // ==================== REGISTER ====================
    public Parent registerParent(Parent parent) {
        // Generate unique 4-digit parentCode
        String code;
        do {
            code = String.format("%04d", new Random().nextInt(10000)); // 0000-9999
        } while (parentRepo.existsByParentCode(code));
        parent.setParentCode(code);

        return parentRepo.save(parent);
    }

    // ==================== GET PARENT BY ID ====================
    public Parent getParentById(Long id) {
        return parentRepo.findById(id).orElse(null);
    }

    // ==================== GET PARENT BY CODE ====================
    public Parent getParentByCode(String code) {
        Optional<Parent> optionalParent = parentRepo.findByParentCode(code);
        return optionalParent.orElse(null);
    }

    // ==================== GET ALL REQUESTS ====================
    public List<GatePassRequest> getAllRequests() {
        return gatePassRepo.findAll();
    }

    // ==================== APPROVE REQUEST ====================
    public GatePassRequest approveRequest(Long requestId, String remarks) {
        Optional<GatePassRequest> optionalReq = gatePassRepo.findById(requestId);
        if (optionalReq.isPresent()) {
            GatePassRequest req = optionalReq.get();
            req.setParentStatus("APPROVED");
            req.setParentRemarks(remarks);
            return gatePassRepo.save(req);
        }
        return null;
    }

    // ==================== REJECT REQUEST ====================
    public GatePassRequest rejectRequest(Long requestId, String remarks) {
        Optional<GatePassRequest> optionalReq = gatePassRepo.findById(requestId);
        if (optionalReq.isPresent()) {
            GatePassRequest req = optionalReq.get();
            req.setParentStatus("REJECTED");
            req.setParentRemarks(remarks);
            return gatePassRepo.save(req);
        }
        return null;
    }

    // ==================== GET REQUESTS FOR PARENT ====================
    public List<GatePassRequest> getRequestsForParent(Long parentId) {
        return gatePassRepo.findByParent_Id(parentId);
    }
}
