package org.override.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSummary {
    String id;
    String name;
    String gender;
    String placeOfBirth;
    String dateOfBirth;
    String classes;
    String subject;

}
