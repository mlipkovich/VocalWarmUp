package com.berniac.vocalwarmup.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Reader;
import java.lang.reflect.Type;

/**
 * Created by Mikhail Lipkovich on 11/30/2017.
 */
public class DrawHierarchyJsonParser {

    private static final String TYPE_ELEMENT = "type";
    private static final Gson GSON;
    static {
        GSON = new GsonBuilder()
                .registerTypeAdapter(HierarchyItem.class, new JsonDeserializer<HierarchyItem>() {
                    @Override
                    public HierarchyItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        JsonObject member = (JsonObject) json;
                        JsonElement typeString = member.get(TYPE_ELEMENT);
                        HierarchyItem.ItemType type =
                                HierarchyItem.ItemType.valueOf(typeString.getAsString().toUpperCase());
                        Type actualType;
                        switch (type) {
                            case CATEGORY:
                                actualType = CategoryItem.class;
                                break;
                            case DRAW:
                                actualType = DrawItem.class;
                                break;
                            case PRESET:
                                actualType = Preset.class;
                                break;
                            default:
                                throw new IllegalStateException("Unsupported item type " + type);
                        }

                        return context.deserialize(json, actualType);
                    }
                })
                .create();
    }

    public static HierarchyItem parseItem(String json) {
        return GSON.fromJson(json, HierarchyItem.class);
    }

    public static HierarchyItem[] parseItemArray(String json) {
        return GSON.fromJson(json, HierarchyItem[].class);
    }

    public static HierarchyItem[] parseItemArray(Reader json) {
        return GSON.fromJson(json, HierarchyItem[].class);
    }

    public static Preset[] parsePresetArray(Reader json) {
        return GSON.fromJson(json, Preset[].class);
    }
}
