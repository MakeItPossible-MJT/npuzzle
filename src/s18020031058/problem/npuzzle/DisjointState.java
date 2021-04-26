package s18020031058.problem.npuzzle;

import core.problem.Action;
import core.problem.State;

import java.util.ArrayList;

public class DisjointState extends State {
    private static int size = 0;
    private int[] board;
    private int pathCost = 0;
    private int hash = 0;
    private static int[][] zobrist = PuzzleProblem.getZobrist();

    public DisjointState(int size, int[] board) {
        DisjointState.zobrist = PuzzleProblem.getZobrist();
        DisjointState.size = size;
        this.board = board.clone();
        pathCost = 0;
        for (int i = 1; i <= size * size; ++i) {
            if (board[i] != 0) {
                hash ^= zobrist[i][board[i]];
            }
        }
    }

    public int[] getBoard() {
        return board;
    }

    /**
     * 复制构造函数
     *
     * @param parent 父结点
     */
    public DisjointState(DisjointState parent) {
        this.board = new int[size * size + 1];
        board = parent.board.clone();
        pathCost = parent.pathCost + 1;
        hash = parent.hash;
    }

    /**
     * 对于位于pos的点，试探所有可移动到的位置，得到所有可行子状态。
     *
     * @param pos 要移动的点的位置
     * @return 所有可行子状态
     */
    public ArrayList<DisjointState> move(int pos) {
        ArrayList<DisjointState> list = new ArrayList<>();
        if (board[pos] == 0) return list;


        int[] offsetsX = {-1, 1, 0, 0};
        int[] offsetsY = {0, 0, -1, 1};

        int posX = (pos - 1) / size;
        int posY = pos - posX * size;

        for (int i = 0; i < 4; ++i) {
            // 要移动到的位置,前面有x个size,位移是y
            int x = posX + offsetsX[i];
            int y = posY + offsetsY[i];
            int toPos = x * size + y; // 要移动到的点的位置
//            System.out.println("pos = " + pos + " toPos = " + toPos);
            if (!isLegal(x, y, toPos)) continue;

            DisjointState child = new DisjointState(this);

            // 移动
            int tmp = child.board[toPos];
            child.board[toPos] = child.board[pos];
            child.board[pos] = tmp;
            // hash值
            child.hash ^= zobrist[pos][child.board[toPos]];
            child.hash ^= zobrist[toPos][child.board[toPos]];

            list.add(child);
        }
        return list;
    }

    /**
     * 位置是否合法
     *
     * @param toPos 点在的位置
     * @return 合法返回true
     */
    private boolean isLegal(int x, int y, int toPos) {
        boolean ok = (x >= 0 && x < size && y >= 1 && y <= size);
        if (ok) {
            return (board[toPos] == 0);
        } else
            return false;
    }

    public int getPathCost() {
        return pathCost;
    }

    public int getHash() {
        return hash;
    }

    public int hashCode() {
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj instanceof DisjointState) {

            DisjointState another = (DisjointState) obj;
            int[] board2 = another.board;
            for (int i = 1; i <= size * size; ++i) {
                if (board[i] != board2[i]) return false;
            }
            return true;
        }
        return false;
    }

    public void draw() {
        for (int i = 1; i <= size * size; ++i) {
            if (size == 4)
                System.out.printf("%-2d", board[i]);
            else System.out.print(board[i]);
            if (i % size == 0) System.out.println();
            else System.out.print(' ');
        }
    }

    /**
     * 当前状态采用action而进入的下一个状态
     *
     * @param action 当前状态下，一个可行的action
     * @return 后继状态
     */
    public State next(Action action) {
        return null;
    }

    /**
     * 当前状态下所有可能的Action，但不一定都可行
     *
     * @return 所有可能的Action的可迭代集合
     */
    public Iterable<? extends Action> actions() {
        return null;
    }

    public static void main(String[] args) {
        PuzzleProblem.init(6);
        int[] board = new int[]{0, 0, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


        DisjointState initState = new DisjointState(4, board);
        DisjointState curState = initState;
        ArrayList<DisjointState> list;
        //for (int i = 1; i <= 16; ++i) {
        list = curState.move(4);
        // if (list.isEmpty()) continue;

        for (DisjointState child : list) {
            System.out.println("hash = " + child.hash);
            child.draw();
        }
        System.out.println("yes");
        // }
    }
}
