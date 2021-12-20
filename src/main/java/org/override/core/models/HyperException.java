package org.override.core.models;

import com.google.gson.JsonObject;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class HyperException implements HyperBody {
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";

    @NonNull
    public String code;
    public String loc;
    public String detail;

    @Override
    public String toJson() {
        return gson.toJson(this);
    }

    public static HyperException fromJson(String json) {
        return gson.fromJson(gson.fromJson(json, JsonObject.class), HyperException.class);
    }
}
