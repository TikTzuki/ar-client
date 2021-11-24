package org.override.utils;

import org.override.models.StudentSummary;
import org.override.models.TermResult;
import org.override.models.TermScoreItem;
import org.override.models.TermScoreSummary;

import java.util.List;

public class FakeData {

    public static TermResult getTermResult() {
        return new TermResult(
                new StudentSummary("123", "name", "gender", "123 Ng", "2020-10-30", "DTC", "CNTT"),
                List.of(
                        new TermResult.TermResultItem(
                                1,
                                "2021-2022",
                                List.of(new TermScoreItem("1", "môn 1", 1, 1D, 1D, 1D, 1D, 1D, 1D, "a", "b", 4D, "Đ")),
                                new TermScoreSummary(3D, 4D, 3D, 4D, 20, 120)
                        ),
                        new TermResult.TermResultItem(
                                2,
                                "2021-2022",
                                List.of(new TermScoreItem("1", "môn 1", 1, 1D, 1D, 1D, 1D, 1D, 1D, "a", "b", 4D, "Đ")),
                                new TermScoreSummary(2D, 1D, 2D, 2D, 20, 120)
                        )
                )
        );
    }

    public static void main(String[] args) {
    }
}