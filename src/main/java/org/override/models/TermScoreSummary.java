package org.override.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TermScoreSummary {
    public Double avgTermScore;
    public Double avgGPATermScore;
    public Double avgScore;
    public Double avgGPAScore;
    public Integer creditsTermCount;
    public Integer creditsCount;
}
