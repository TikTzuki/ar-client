package org.override.models;

import lombok.*;
import org.override.core.models.HyperBody;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class UserModel implements HyperBody {
    public Integer id;

    @NonNull
    public String email;

    @NonNull
    public String name;

    @NonNull
    public String publicKey;

    @Override
    public String toJson() {
        return gson.toJson(this);
    }

    public static UserModel fromJson(String json) {
        return gson.fromJson(json, UserModel.class);
    }
}
