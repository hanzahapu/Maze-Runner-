import java.util.Objects;

class Location {
    int x, y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Checks if two locations are equal
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location location = (Location) obj;
        return x == location.x && y == location.y;
    }

    // Generates a hash code
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}