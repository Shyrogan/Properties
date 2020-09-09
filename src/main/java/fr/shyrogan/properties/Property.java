package fr.shyrogan.properties;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import fr.shyrogan.properties.constraint.Constraint;

import java.util.List;
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

    private       T                  value;
    private final ClassToInstanceMap<Constraint<T>> constraints;

    public Property(
            String name, T defaultValue, Map<String, Group> child,
            ClassToInstanceMap<Constraint<T>> constraints, Supplier<Boolean> displayCondition
    ) {
        super(name, child, displayCondition);
        this.value = defaultValue;
        this.constraints = constraints;
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
     * @return The list of constraints applied to this property
     */
    public ClassToInstanceMap<Constraint<T>> getConstraints() {
        return constraints;
    }

    /**
     * @param constraintClass Constraint's class
     * @return Whether this property has a constraint with specified type
     */
    public boolean has(Class<? extends Constraint<T>> constraintClass) {
        return constraints.containsKey(constraintClass);
    }

    /**
     * @param constraintClass Constraint's class
     * @return The constraint instance based on its class
     */
    public Constraint<T> get(Class<? extends Constraint<T>> constraintClass) {
        return constraints.get(constraintClass);
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
        private       String                                             name;
        private       T                                                  defaultValue;
        private final ImmutableMap.Builder<String, Group>                child            = ImmutableMap.builder();
        private final ImmutableClassToInstanceMap.Builder<Constraint<T>> constraints      = ImmutableClassToInstanceMap.builder();
        private       Supplier<Boolean>                                  displayCondition = DEFAULT_CONDITION;

        Builder() { }

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> defaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder<T> with(Constraint<T> constraint) {
            constraints.put((Class<Constraint<T>>)constraint.getClass(), constraint);
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
                    constraints.build(),
                    checkNotNull(displayCondition)
            );
        }
    }

}
