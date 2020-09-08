package fr.shyrogan.properties;

import com.google.common.collect.ImmutableMap;
import fr.shyrogan.properties.group.Group;

import java.util.Map;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Wraps an object so it can be saved and modified by the user.
 *
 * @param <T> Type
 */
public class Property<T> extends Group {

    /**
     * @param <T> Property's type
     * @return Builder
     */
    public static <T> Builder<T> newProperty() {
        return new Builder<>();
    }

    private T value;

    public Property(String name, T defaultValue, Map<String, Group> child, Supplier<Boolean> displayCondition) {
        super(name, child, displayCondition);
        this.value = defaultValue;
    }

    /**
     * @return The value wrapped by this {@link Property}
     */
    public T getValue() {
        return value;
    }

    /**
     * Modifies the value to the specified one
     *
     * @param value New value
     */
    public Property<T> setValue(T value) {
        this.value = value;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return "Property{name=" + getName() + ", value=" + value + '}';
    }

    /**
     * A builder used to create a {@link Property} instance
     * @param <T>
     */
    public static class Builder<T> {
        private String                                          name;
        private T                                               defaultValue;
        private final ImmutableMap.Builder<String, Group>       child            = ImmutableMap.builder();
        private Supplier<Boolean>                               displayCondition = DEFAULT_CONDITION;

        protected Builder() {}

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder<T> eat(Property<?> property) {
            child.put(property.getName(), property);
            return this;
        }

        public Builder<T> displayIf(Supplier<Boolean> displayCondition) {
            this.displayCondition = displayCondition;
            return this;
        }

        public Property<T> build() {
            return new Property<>(
                    checkNotNull(name),
                    checkNotNull(defaultValue),
                    child.build(),
                    checkNotNull(displayCondition)
            );
        }
    }

}
