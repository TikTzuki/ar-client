package org.override.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.override.core.models.HyperBody;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditModel implements HyperBody {
    public String subjectId;
    public String subjectName;
    public Integer creditsCount;

    @Override
    public String toJson() {
        return gson.toJson(this);
    }
}
