package org.override.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.override.core.models.HyperBody;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TermResult implements HyperBody {
    StudentSummary studentSummary;
    List<TermResultItem> termResultItems;

    @Override
    public String toJson() {
        return gson.toJson(this);
    }

    static class TermResultItem {
        Integer term;
        String year;
        List<TermScoreItem> termScoreItems;
        TermScoreSummary termScoreSummary;
    }
}
