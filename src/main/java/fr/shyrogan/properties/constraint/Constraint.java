package fr.shyrogan.properties.constraint;

import fr.shyrogan.properties.Property;

public interface Constraint<T> {

    /**
     * Applies this constraint when the value of the passed property is changed.
     *
     * @param property Property
     * @param future Future value
     * @return The future value
     */
    T apply(Property<T> property, T future);

}
