package mmm.example.hostelgatepass.controller;

import mmm.example.hostelgatepass.model.Admin;
import mmm.example.hostelgatepass.model.GatePassRequest;
import mmm.example.hostelgatepass.service.AdminService;
import mmm.example.hostelgatepass.service.GatePassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private GatePassService gatePassService;

    @Autowired
    private AdminService adminService; // ✅ inject AdminService

    @GetMapping("/requests")
    public List<GatePassRequest> getRequests() {
        return gatePassService.getAllRequests();
    }

    @PostMapping("/approve/{id}")
    public GatePassRequest approve(@PathVariable Long id, @RequestParam String remarks) {
        return gatePassService.approveRequest(id, remarks);
    }

    @PostMapping("/reject/{id}")
    public GatePassRequest reject(@PathVariable Long id, @RequestParam String remarks) {
        return gatePassService.rejectRequest(id, remarks);
    }

    // ✅ Proper login method using injected service
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        Admin valid = adminService.validateLogin(admin.getUsername(), admin.getPassword());
        if (valid != null) {
            return ResponseEntity.ok(valid);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
