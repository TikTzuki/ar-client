package org.override.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TermScoreSummary {
    Double avgTermScore;
    Double avgGPATermScore;
    Double avgScore;
    Double avgGPAScore;
    Integer creditsCount;
}
