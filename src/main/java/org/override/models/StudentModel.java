package org.override.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.override.core.models.HyperBody;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentModel implements HyperBody {
    public String studentId;

    public String name;

    public Double avgScore;

    public Integer course;

    public String subject;

    public String speciality;

    @Override
    public String toJson() {
        return gson.toJson(this);
    }
}
