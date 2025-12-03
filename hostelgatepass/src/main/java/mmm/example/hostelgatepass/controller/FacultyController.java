package mmm.example.hostelgatepass.controller;

import mmm.example.hostelgatepass.model.GatePassRequest;
import mmm.example.hostelgatepass.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculty")
@CrossOrigin
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @GetMapping("/requests")
    public List<GatePassRequest> getAllRequests() {
        return facultyService.getAllRequests();
    }

    @PostMapping("/approve/{id}")
    public GatePassRequest approve(@PathVariable Long id, @RequestParam String remarks) {
        return facultyService.approveRequest(id, remarks);
    }

    @PostMapping("/reject/{id}")
    public GatePassRequest reject(@PathVariable Long id, @RequestParam String remarks) {
        return facultyService.rejectRequest(id, remarks);
    }
}
