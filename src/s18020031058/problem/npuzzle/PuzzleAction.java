package s18020031058.problem.npuzzle;

import core.problem.Action;


public class PuzzleAction extends Action {
    private final Direction direction;
    public PuzzleAction(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void draw() {
        System.out.println(toString());
    }

    @Override
    public int stepCost() {
        return 1;
    }

    @Override
    public String toString() {
        return direction.symbol() + "";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof PuzzleAction) {
            PuzzleAction another = (PuzzleAction) obj;
            //两个Node对象的状态相同，则认为是相同的
            return this.direction.equals(another.direction);
        }
        return false;
    }
}
