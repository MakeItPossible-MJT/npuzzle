package s18020031058.problem.npuzzle;

import core.problem.Action;
import core.problem.Problem;
import core.problem.State;
import core.solver.Node;

import java.util.Deque;
import java.util.Random;

public class PuzzleProblem extends Problem {
    public static int[][] zobrist;
    private static boolean hasInited = false;
    private int[] aux;
    int inverseNum = 0;
    public static void init(int size) {
        if (hasInited) return;
        zobrist = new int[(size + 1) * (size + 1)][(size + 1) * (size + 1)];
        Random r = new Random();
        for (int i = 1; i <= size * size; ++i) {
            for (int j = 0; j <= size * size; ++j) {
                    zobrist[i][j] = r.nextInt(0x7fffffff);
                }
        }
        hasInited = true;
    }

    public static int[][] getZobrist() {
        return zobrist;
    }

    public PuzzleProblem(State initialState, State goal, int size) {
        super(initialState, goal, size);
        aux = new int[size * size + 2];
    }

    /**
     * 当前问题是否有解
     * @return 有解，true; 无解，false
     *
     */

    public boolean solvable() {
        int[] board = ((PuzzleState)initialState).getBoard();
        int[] nums = new int[size * size + 1];
        int cnt = 0;
        int line = 0;
        for (int i = 1; i <= size * size; ++i) {
            if (board[i] != 0) nums[cnt++] = board[i];
            else line = i / size;
        }
        mergeSort(nums, 1, size * size - 1);
        if (size % 2 == 0) inverseNum += (size - line);
        return (inverseNum % 2 == 0);
    }

    public void mergeSort(int[] board, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        mergeSort(board, lo, mid);
        mergeSort(board, mid + 1, hi);
        merge(board, lo, mid, hi);
    }

    public void merge(int[] board, int lo, int mid, int hi) {
        for (int i = lo; i <= hi; ++i) aux[i] = board[i];
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; ++k) {
            if (j > hi) board[k] = aux[i++];
            else if (i > mid) board[k] = aux[j++];
            else if (aux[i] <= aux[j]) board[k] = aux[i++];
            else {
                board[k] = aux[j++];
                inverseNum += (mid - i) + 1;
            }
        }
    }


    /**
     *
     * @param state     当前状态
     * @param action    进入当前状态所采取的Action
     * @return          进入当前状态的代价
     */
    public int stepCost(State state, Action action) {
        return 1;
    }

    /**
     * 在状态state上的action是否可用？
     * @param state     当前状态
     * @param action    当前状态下所采用的动作
     * @return  true：可用；false：不可用
     */
    protected boolean applicable(State state, Action action) {
        // 我在actions函数中返回的是所有可行的action,所以这里一定成立,只需要返回true.
        return true;
    }

    /**
     * 解路径的可视化
     * @param path 解路径
     */
    public void showSolution(Deque<Node> path) {

    }

    /**
     * 打印当前问题实例
     */
    public void draw() {

    }

    /**
     * 打印解路径
     * @param path 解路径
     */
    public void printPath(Deque<Node> path) {
        Node node;
        PuzzleAction action;
        initialState.draw();
        while (!path.isEmpty()) {
            node = path.poll();
            action = (PuzzleAction) node.getAction();
            System.out.println(" ↓ ");
            System.out.println(" ↓ (0 " + action.getDirection() + ")");
            System.out.println(" ↓ ");
            node.getState().draw();
        }
        return;
    }

    /**
     * 判断某个状态state是否到达目标状态，多数情况下是判断跟目标状态是否相等。
     *
     * @param state 要判断的状态
     * @return  true：要判断的状态已经是目标状态；否则，false
     */
    @Override
    public boolean goal(State state){
        return (((PuzzleState)state).disjoint((PuzzleState) state)) == 0;
    }
}
