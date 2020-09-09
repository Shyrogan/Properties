package fr.shyrogan.properties.serialization.gson;

import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import fr.shyrogan.properties.Property;
import fr.shyrogan.properties.Group;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupSerializer implements JsonSerializer<Group>, JsonDeserializer<Group> {

    private final Map<String, Class<?>> TYPE_CACHE = new HashMap<>();

    @Override
    public JsonElement serialize(Group src, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("name", src.getName());
        if(src.getChild().size() > 0) {
            object.add("child", context.serialize(src.getChild().values()));
        }
        if(src instanceof Property<?>) {
            Property<?> prop = (Property<?>)src;
            object.add("value", context.serialize(prop.getValue()));
            object.addProperty("type", prop.getValue().getClass().getName());
        }
        return object;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Group deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();
        String             name = object.get("name").getAsString();
        ImmutableMap.Builder<String, Group> child = new ImmutableMap.Builder<>();
        if(object.has("child")) {
            JsonElement childE = object.getAsJsonArray("child");
            ArrayList<Group> array = context.deserialize(childE, new TypeToken<List<Group>>() {}.getType());
            int size = array.size();
            for(int i = 0; i < size; i++) {
                Group group = array.get(i);
                child.put(group.getName(), group);
            }
        }
        if(object.has("value")) {
            JsonElement valueE = object.get("value");
            String stringType = object.get("type").getAsString();
            Class<?> propertyType;
            if(TYPE_CACHE.containsKey(stringType)) {
                propertyType = TYPE_CACHE.get(stringType);
            } else {
                try {
                    propertyType = Class.forName(stringType);
                    TYPE_CACHE.put(stringType, propertyType);
                } catch (ClassNotFoundException exc) {
                    throw new RuntimeException("Cannot deserialize property \"" + name + "\" because class " + stringType + " cannot be found");
                }
            }
            return new Property(
                    name, context.deserialize(valueE, TypeToken.get(propertyType).getType()),
                    child.build(),
                    ImmutableClassToInstanceMap.builder().build(), Group.DEFAULT_CONDITION
            );
        } else {
            return new Group(name, child.build(), Group.DEFAULT_CONDITION);
        }
    }
}
