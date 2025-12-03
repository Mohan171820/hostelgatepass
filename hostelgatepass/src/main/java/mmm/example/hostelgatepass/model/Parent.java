package mmm.example.hostelgatepass.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String contactNumber;

    @Column(unique = true)
    private String parentCode;

    // Link parent to a specific student
    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;


}
