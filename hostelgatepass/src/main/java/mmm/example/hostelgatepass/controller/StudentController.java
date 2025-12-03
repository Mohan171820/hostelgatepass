package mmm.example.hostelgatepass.controller;

import mmm.example.hostelgatepass.model.GatePassRequest;
import mmm.example.hostelgatepass.service.GatePassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/student")
@CrossOrigin
public class StudentController {

    @Autowired
    private GatePassService gatePassService;

    // Raise new gate pass request
    @PostMapping("/raise-request")
    public GatePassRequest raiseRequest(@RequestParam String studentId,
                                        @RequestParam String reason,
                                        @RequestParam String type,
                                        @RequestParam String returnDate) {
        return gatePassService.raiseRequest(
                studentId,
                reason,
                type,
                LocalDateTime.parse(returnDate + "T00:00:00")
        );
    }

    // ✅ Fetch all requests belonging to a specific student
    @GetMapping("/requests/{studentId}")
    public List<GatePassRequest> getStudentRequests(@PathVariable String studentId) {
        return gatePassService.getRequestsByStudentId(studentId);
    }
}
