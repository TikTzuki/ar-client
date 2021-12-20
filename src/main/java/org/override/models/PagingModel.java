package org.override.models;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.override.core.models.HyperBody;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Log4j2
public class PagingModel<T> implements HyperBody {
    @NonNull
    List<T> items;
    Integer pageNum;
    Integer pageSize;
    Integer totalItem;

    @Override
    public String toJson() {
        return gson.toJson(this);
    }

    public static <T> PagingModel<T> fromGson(String json, Class<T> tClass) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        List<T> items = new ArrayList<>();
        jsonObject.get("items").getAsJsonArray().forEach(
                i -> items.add(gson.fromJson(i, tClass))
        );
        return new PagingModel<>(
                items,
                jsonObject.get("pageNum") != null ? jsonObject.get("pageNum").getAsInt() : null,
                jsonObject.get("pageSize") != null ? jsonObject.get("pageSize").getAsInt() : null,
                jsonObject.get("totalItem") != null ? jsonObject.get("totalItem").getAsInt() : null
        );
    }
}
