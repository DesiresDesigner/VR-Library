/**
 * Created by vesel on 2016-03-26.
 */

class Point {
    /**
     * Create a point.
     * @param {number} x - The x value.
     * @param {number} y - The y value.
     * @param {number} z - The z value.
     */
    constructor(x, y, z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 
     * @param other {Point}
     * @returns {number}
     */
    distanceTo(other) {
        return Math.sqrt(this.squareDistanceTo(other));
    }

    /**
     *
     * @param other {Point}
     * @returns {number}
     */
    squareDistanceTo(other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }
}