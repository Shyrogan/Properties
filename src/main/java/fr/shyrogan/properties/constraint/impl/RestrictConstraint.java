package fr.shyrogan.properties.constraint.impl;

import fr.shyrogan.properties.Property;
import fr.shyrogan.properties.constraint.Constraint;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public final class RestrictConstraint<T> implements Constraint<T> {

    /**
     * Returns a new restriction constraint that restricts the values to a few possibilities.
     *
     * @param possibilities Possibilities
     * @return Restriction constraint
     */
    public static <T> RestrictConstraint<T> restrict(T... possibilities) {
        return new RestrictConstraint<>(possibilities);
    }

    private final Set<T> restrictions;

    RestrictConstraint(T[] restrictions) {
        this.restrictions = new LinkedHashSet<>();
        this.restrictions.addAll(Arrays.asList(restrictions));
    }

    @Override
    public T apply(Property<T> property, T future) {
        // If the set does not contain the current value, we should probably add it ;)
        restrictions.add(property.getValue());

        if(!restrictions.contains(future))
            return property.getValue();
        return future;
    }

    /**
     * @return The set of value choices available
     */
    public Set<T> getRestrictions() {
        return restrictions;
    }

    /**
     * A dirty method to navigate through our values
     *
     * @param value Value
     * @return The next value of specified one
     */
    public T nextTo(T value) {
        if(!restrictions.contains(value))
            return value;

        Iterator<T> iterator = restrictions.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if(next == value) {
                return iterator.hasNext() ? iterator.next() : value;
            }
        }
        return value;
    }

    /**
     * A dirty method to navigate through our values
     *
     * @param value Value
     * @return The previous value of specified one
     */
    public T previousTo(T value) {
        if(!restrictions.contains(value))
            return value;

        Iterator<T> iterator = restrictions.iterator();
        T previous = value;
        while (iterator.hasNext()) {
            T next = iterator.next();
            if(next == value) {
                return previous;
            }
            previous = next;
        }
        return value;
    }

}
