package fr.shyrogan.properties.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.shyrogan.properties.Property;
import fr.shyrogan.properties.group.Group;
import fr.shyrogan.properties.serialization.gson.GroupSerializer;

public interface PropertySerializer {

    PropertySerializer INSTANCE = new Default();

    /**
     * @param holder Holder instance
     * @return A String form of the holder
     */
    String write(Group holder);

    /**
     * @param raw Saved text as raw
     */
    Group read(String raw);

    /**
     * The default serializer that doesn't use any library.
     */
    class Default implements PropertySerializer {
        private final Gson GSON = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(TypeToken.get(Group.class).getType(), new GroupSerializer())
                .registerTypeAdapter(TypeToken.get(Property.class).getType(), new GroupSerializer())
                .create();

        @Override
        public String write(Group g) {
            return GSON.toJson(g);
        }

        @Override
        public Group read(String raw) {
            return GSON.fromJson(raw, Group.class);
        }
    }

}
