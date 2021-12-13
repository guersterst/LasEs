package de.lases.global.transport;

import java.util.List;

/**
 * Represents a field of expertise in science.
 */
public class ScienceField implements Cloneable {

    private String name;

    public String getName() {
        return name;
    }

    /**
     * Set the name of the science field.
     *
     * @param name The name of the science field.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Create a deep copy of the original object.
     *
     * @return A deep copy.
     */
    @Override
    public ScienceField clone() {
        try {
            ScienceField clone = (ScienceField) super.clone();

            // nothing to do here, since science field has only immutable fields.
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Check equality by comparing ids.
     *
     * @param object The object to compare to.
     * @return Is the provided object equal to this user?
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (object instanceof ScienceField field) {
            return this.name.equals(field.name);
        } else {
            return false;
        }
    }
}
