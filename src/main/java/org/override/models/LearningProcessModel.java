package org.override.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.override.core.models.HyperBody;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningProcessModel implements HyperBody {
    public Double learningProcessPercent;
    public String process;
    public List<CreditModel> credits;

    @Override
    public String toJson() {
        return gson.toJson(this);
    }

    public static LearningProcessModel fromJson(String json){
        return gson.fromJson(json, LearningProcessModel.class);
    }
}
