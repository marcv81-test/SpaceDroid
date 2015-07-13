package net.marcv81.spacedroid.physics;

/**
 * Instances of this class age, decline, and expire.
 */
public final class Decliner extends Ager implements Expirable {

    /**
     * Age of decline.
     */
    private long declineAge = 0;

    /**
     * Age of expiry.
     */
    private long expiryAge = 0;

    /**
     * Constructor with undetermined decline and expiry ages.
     */
    public Decliner() {
    }

    /**
     * Constructor with immediate decline and set expiry age.
     */
    public Decliner(long expiryAge) {
        this.expiryAge = expiryAge;
    }

    /**
     * Constructor with set decline and expiry ages.
     */
    public Decliner(long declineAge, long expiryAge) {
        this.declineAge = declineAge;
        this.expiryAge = expiryAge;
    }

    /**
     * Triggers immediate decline. Does nothing if the expiry is already scheduled, even if
     * not declining yet.
     *
     * @param duration Decline duration in milliseconds.
     */
    public void decline(long duration) {
        if (expiryAge == 0) {
            this.declineAge = this.getAge();
            this.expiryAge = this.getAge() + duration;
        }
    }

    public boolean isDeclining() {
        return (expiryAge > 0) && (this.getAge() >= declineAge) && (this.getAge() < expiryAge);
    }

    public boolean isExpired() {
        return (expiryAge > 0) && (this.getAge() >= expiryAge);
    }

    /**
     * @return Decline ratio going progressively from 0f to 1f.
     */
    public float getDeclineRatio() {
        if (isExpired()) {
            return 1f;
        } else if (isDeclining()) {
            float x = this.getAge() - declineAge;
            float y = expiryAge - declineAge;
            return x / y;
        } else {
            return 0f;
        }
    }
}
