package net.marcv81.spacedroid.sprites;

/**
 * Instances of this class age.
 */
public class Ager {

    /**
     * Age in milliseconds.
     */
    private long age = 0;

    public long getAge() {
        return age;
    }

    public void update(long timeSlice) {
        age += timeSlice;
    }
}
