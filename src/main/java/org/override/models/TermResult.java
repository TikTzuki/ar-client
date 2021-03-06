package org.override.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.override.core.models.HyperBody;

import java.lang.reflect.Method;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TermResult implements HyperBody {
    public StudentSummary studentSummary;
    public List<TermResultItem> termResultItems;

    @Override
    public String toJson() {
        return gson.toJson(this);
    }

    public static TermResult fromJson(String json) {
        return gson.fromJson(json, TermResult.class);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TermResultItem {
        public Integer term;
        public String year;
        public List<TermScoreItem> termScoreItems;
        public TermScoreSummary termScoreSummary;

        public static String getName() {
            return "my name";
        }
    }

}
