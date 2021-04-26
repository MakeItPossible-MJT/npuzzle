package s18020031058.problem.npuzzle;

import java.util.EnumMap;

public enum Direction {
    E('→'),
    W('←'),
    S('↓'),
    N('↑');
    Direction(char symbol) {this.symbol = symbol;}
    private final char symbol;
    public char symbol() {return symbol;}

    private static final EnumMap<Direction, int[]> DIRECTION_OFFSET = new EnumMap<>(Direction.class);
    static {
        DIRECTION_OFFSET.put(E, new int[]{0, 1});
        DIRECTION_OFFSET.put(W, new int[]{0, -1});
        DIRECTION_OFFSET.put(S, new int[]{1, 0});
        DIRECTION_OFFSET.put(N, new int[]{-1, 0});
    }

    public static int[] offset(Direction dir) {
        return DIRECTION_OFFSET.get(dir);
    }
}
