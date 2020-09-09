package fr.shyrogan.properties.constraint.impl;

import fr.shyrogan.properties.Property;
import fr.shyrogan.properties.constraint.Constraint;

public final class BoundConstraint<T extends Number> implements Constraint<T> {

    /**
     * Returns a new bounding constraint for a Property that wraps a number allowing us
     * to set a minimum value and a maximum value.
     *
     * @param minimum Minimum
     * @param maximum Maximum
     * @return Constraint
     */
    public static <T extends Number> BoundConstraint<T> bound(T minimum, T maximum) {
        if(minimum.doubleValue() > maximum.doubleValue())
            return new BoundConstraint<>(maximum, minimum);
        else
            return new BoundConstraint<>(minimum, maximum);
    }

    private final T minimum;
    private final T maximum;

    BoundConstraint(T minimum, T maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public T apply(Property<T> property, T future) {
        if(future.doubleValue() > maximum.doubleValue())
            return maximum;
        else if(future.doubleValue() < minimum.doubleValue())
            return minimum;
        else
            return future;
    }
}
