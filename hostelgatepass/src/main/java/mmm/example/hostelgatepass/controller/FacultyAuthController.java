package mmm.example.hostelgatepass.controller;

import mmm.example.hostelgatepass.model.Faculty;
import mmm.example.hostelgatepass.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/faculty")
@CrossOrigin
public class FacultyAuthController {

    @Autowired
    private FacultyService facultyService;

    @PostMapping("/login")
    public Faculty login(@RequestBody Faculty f) {
        Faculty faculty = facultyService.validateLogin(f.getUsername(), f.getPassword());
        if (faculty == null) {
            throw new RuntimeException("Invalid username or password");
        }
        faculty.setPassword(null);
        return faculty;
    }
}
