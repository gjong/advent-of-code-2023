package com.github.gjong.advent2023.common.geometry;

import java.util.Set;

public record Point(int x, int y) {

    public Point translate(Point translation) {
        return Point.of(x + translation.x, y + translation.y);
    }

    public Point translate(int x, int y) {
        return Point.of(this.x + x, this.y + y);
    }

    public boolean touches(Point other) {
        return (x >= (other.x - 1) && x <= (other.x + 1))
                && (y >= (other.y - 1) && y <= (other.y + 1));
    }

    public static Point zero = new Point(0, 0);

    public static Point of(int x, int y) {
        return new Point(x, y);
    }

    /**
     * Build the list of neighbours, this includes all points except the diagonal points that touch
     * this point.
     *
     * @return
     */
    public Set<Point> neighbours() {
        return Set.of(
                this.translate(0, 1),
                this.translate(0, - 1),
                this.translate(-1, 0),
                this.translate(1, 0));
    }

    public Point left() {
        return this.translate(-1, 0);
    }

    public Point right() {
        return this.translate(1, 0);
    }

    public Point up() {
        return this.translate(0, - 1);
    }

    public Point down() {
        return this.translate(0, 1);
    }
}
