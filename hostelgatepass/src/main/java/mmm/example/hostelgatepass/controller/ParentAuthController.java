package mmm.example.hostelgatepass.controller;

import mmm.example.hostelgatepass.model.Parent;
import mmm.example.hostelgatepass.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/parent")
@CrossOrigin
public class ParentAuthController {

    @Autowired
    private ParentService parentService;  // correct class reference

    @PostMapping("/login")
    public Parent login(@RequestBody Parent p) {
        Parent parent = parentService.validateLogin(p.getEmail(), p.getPassword());
        if (parent == null) {
            throw new RuntimeException("Invalid email or password");
        }
        parent.setPassword(null); // do not expose password
        return parent;
    }
}
