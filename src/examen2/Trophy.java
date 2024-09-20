package examen2;

public enum Trophy {
    PLATINO(5), ORO(3), PLATA(2), BRONCE(1);
    public int points;

    Trophy(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
