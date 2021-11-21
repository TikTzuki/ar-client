package org.override.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.override.core.models.HyperBody;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TermScoreItem implements HyperBody {
    String subjectId;
    String subjectName;
    Integer creditsCount;
    Double examPercent;
    Double finalExamPercent;
    Double examScore;
    Double finalExamScore;
    Double termScoreFirst;
    Double termScoreSecond;
    String gpaFrist;
    String gpaSecond;
    Double gpa;
    String result;

    @Override
    public String toJson() {
        return gson.toJson(this);
    }

}
