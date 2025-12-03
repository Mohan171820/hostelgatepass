package mmm.example.hostelgatepass.controller;

import mmm.example.hostelgatepass.model.GatePassRequest;
import mmm.example.hostelgatepass.model.Parent;
import mmm.example.hostelgatepass.repository.GatePassRepository;
import mmm.example.hostelgatepass.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/parent")
@CrossOrigin
public class ParentController {

    @Autowired
    private ParentRepository parentRepo;

    @Autowired
    private GatePassRepository gatePassRepo;

    // ====== Parent Login ======
    @PostMapping("/login")
    public Parent login(@RequestBody Parent parent) {
        Optional<Parent> optionalParent = parentRepo.findByEmailAndPassword(parent.getEmail(), parent.getPassword());
        Parent p = optionalParent.orElseThrow(() -> new RuntimeException("Invalid email or password"));
        p.setPassword(null); // do not send password back
        return p;
    }

    // ====== Fetch pending requests for this parent ======
    @GetMapping("/requests/{parentId}")
    public List<GatePassRequest> getPendingRequests(@PathVariable Long parentId) {
        return gatePassRepo.findByParent_IdAndParentStatus(parentId, "PENDING");
    }

    // ====== Approve a request ======
    @PutMapping("/approve/{requestId}")
    public GatePassRequest approveRequest(@PathVariable Long requestId,
                                          @RequestParam(required = false) String remarks) {

        GatePassRequest req = gatePassRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        req.setParentStatus("APPROVED");
        req.setParentRemarks(remarks);
        req.setStatus("PendingFaculty"); // now visible to faculty

        return gatePassRepo.save(req);
    }

    // ====== Reject a request ======
    @PutMapping("/reject/{requestId}")
    public GatePassRequest rejectRequest(@PathVariable Long requestId,
                                         @RequestParam(required = false) String remarks) {

        GatePassRequest req = gatePassRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        req.setParentStatus("REJECTED");
        req.setParentRemarks(remarks);
        req.setStatus("Rejected");

        return gatePassRepo.save(req);
    }


}
