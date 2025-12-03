package mmm.example.hostelgatepass.service;

import mmm.example.hostelgatepass.model.Admin;
import mmm.example.hostelgatepass.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository repo;

    public Admin validateLogin(String username, String password) {
        // Option 1: Use the built-in finder for direct matching
        Admin admin = repo.findByUsernameAndPassword(username, password);

        // Option 2 (alternative): Use findById if you prefer manual password check
        // Admin admin = repo.findById(username).orElse(null);
        // if (admin != null && admin.getPassword().equals(password)) {
        //     return admin;
        // }

        return admin; // Returns null if not found
    }
}
