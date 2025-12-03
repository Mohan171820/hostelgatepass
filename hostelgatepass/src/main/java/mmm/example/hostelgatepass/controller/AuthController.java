package mmm.example.hostelgatepass.controller;

import mmm.example.hostelgatepass.model.Admin;
import mmm.example.hostelgatepass.model.Student;
import mmm.example.hostelgatepass.service.AdminService;
import mmm.example.hostelgatepass.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdminService adminService;

    // Student Register
    @PostMapping("/register")
    public Student register(@RequestBody Student s) {
        return studentService.registerStudent(s);
    }

    // Student Login
    @PostMapping("/login")
    public Student login(@RequestBody Student s) {
        return studentService.validateLogin(s.getId(), s.getPassword());
    }

}
