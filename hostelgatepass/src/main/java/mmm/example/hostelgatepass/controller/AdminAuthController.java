package mmm.example.hostelgatepass.controller;

import mmm.example.hostelgatepass.model.Admin;
import mmm.example.hostelgatepass.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/admin")
@CrossOrigin
public class AdminAuthController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Admin login(@RequestBody Admin a) {
        Admin admin = adminService.validateLogin(a.getUsername(), a.getPassword());
        if (admin == null) {
            throw new RuntimeException("Invalid username or password");
        }
        admin.setPassword(null); // Hide password in response
        return admin;
    }
}
