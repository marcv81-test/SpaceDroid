package net.marcv81.spacedroid.sprites;

/**
 * Instances of this class age and eventually expire. Additional functions help
 * handle the decline toward expiry.
 */
public final class Expirer implements Expirable {

    /**
     * Age in milliseconds.
     */
    private long age = 0;

    /**
     * Age of decline.
     */
    private long declineAge = 0;

    /**
     * Age of expiry.
     */
    private long expiryAge = 0;

    /**
     * Constructor with unknown decline and expiry ages.
     */
    public Expirer() {
    }

    /**
     * Constructor with immediate decline and known expiry age.
     */
    public Expirer(long expiryAge) {
        this.expiryAge = expiryAge;
    }

    /**
     * Constructor with known decline and expiry ages.
     */
    public Expirer(long declineAge, long expiryAge) {
        this.declineAge = declineAge;
        this.expiryAge = expiryAge;
    }

    public long getAge() {
        return age;
    }

    /**
     * Triggers immediate decline. Does nothing if the expiry is already scheduled, even if
     * not declining yet.
     *
     * @param duration Decline duration in milliseconds.
     */
    public void decline(long duration) {

        // Ignore when expiry is already scheduled
        if (expiryAge == 0) {
            this.declineAge = age;
            this.expiryAge = age + duration;
        }
    }

    public boolean isDeclining() {
        return (expiryAge > 0) && (age >= declineAge) && (age < expiryAge);
    }

    public boolean isExpired() {
        return (expiryAge > 0) && (age >= expiryAge);
    }

    /**
     * @return Decline ratio going progressively from 0f to 1f.
     */
    public float getDeclineRatio() {
        if (isExpired()) {
            return 1f;
        } else if (isDeclining()) {
            float x = age - declineAge;
            float y = expiryAge - declineAge;
            return x / y;
        } else {
            return 0f;
        }
    }

    /**
     * Updates the age.
     *
     * @param timeSlice Time slice duration in milliseconds.
     */
    public void update(long timeSlice) {
        age += timeSlice;
    }
}
