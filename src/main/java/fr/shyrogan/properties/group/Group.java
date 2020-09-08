package fr.shyrogan.properties.group;

import com.google.common.collect.ImmutableMap;
import fr.shyrogan.properties.Property;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class Group {

    /**
     * The display condition used when {@link this#quick(String)} is called.
     */
    public static Supplier<Boolean>                  DEFAULT_CONDITION = () -> true;

    /**
     * @return A new holder created using the defaults suppliers contained in this
     * {@link Group} class.
     */
    public static Group quick(String name) {
        return new Group(name, Collections.emptyMap(), DEFAULT_CONDITION);
    }

    /**
     * @return A new {@link Group} builder to fully customize it.
     */
    public static Builder newGroup() {
        return new Builder();
    }

    private final     String             name;
    private final     Map<String, Group> child;
    private final     Supplier<Boolean>  displayCondition;

    public Group(String name, Map<String, Group> child, Supplier<Boolean> displayCondition) {
        this.name = name;
        this.child = child;
        this.displayCondition = displayCondition;
    }

    /**
     * @return The name of this holder
     */
    public String getName() {
        return name;
    }

    /**
     * @return The properties contained by this holder
     */
    public Map<String, Group> getChild() {
        return child;
    }

    /**
     * @param name Children's name
     * @return The potential children contained by this holder
     */
    public Optional<Group> getChild(String name) {
        return Optional.ofNullable(child.get(name));
    }

    /**
     * @return Whether this property can be displayed by invoking the {@link this#displayCondition}
     */
    public boolean canBeDisplayed() {
        return displayCondition.get();
    }

    /**
     * A builder used to create a group.
     */
    public static class Builder {
        private String                                    name;
        private final ImmutableMap.Builder<String, Group> child            = ImmutableMap.builder();
        private Supplier<Boolean>                         displayCondition = DEFAULT_CONDITION;

        protected Builder() {}

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder eat(Property<?> property) {
            child.put(property.getName(), property);
            return this;
        }

        public Builder displayIf(Supplier<Boolean> displayCondition) {
            this.displayCondition = displayCondition;
            return this;
        }

        public Group build() {
            return new Group(
                    requireNonNull(name),
                    child.build(),
                    requireNonNull(displayCondition)
            );
        }
    }

}
