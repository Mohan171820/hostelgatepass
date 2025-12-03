
package mmm.example.hostelgatepass.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    private String id;

    private String name;
    private String email;
    private String roomNo;
    private String parentContact;
    private String password;
}