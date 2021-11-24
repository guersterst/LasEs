package de.lases.global.transport;

import java.util.List;

/**
 * Represents a field of science.
 */
public class ScienceField implements Cloneable {

    private int id;

    private String name;

    public int getId() {
        return id;
    }

    /**
     * Set the id of the science field.
     *
     * @param id The id of the science field.
     */
    public void setId(int id) {
        this.id = id;
    }

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
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
