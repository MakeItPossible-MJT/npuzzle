package s18020031058.problem.npuzzle;

import algs4.util.In;
import core.problem.Action;
import core.problem.State;
import core.runner.HeuristicType;
import core.solver.heuristic.Predictor;
import s18020031058.solver.heuristic.PuzzleConfiguration;

import java.util.ArrayList;
import java.util.EnumMap;

public class PuzzleState extends State {

    private int size = 0;

    private static final Direction[] directions;
    static {
        directions = new Direction[]{Direction.E, Direction.W, Direction.S, Direction.N};
    }

    private int[] board;
    private int hash = 0;
    // 用1维数组代替二维数组,spaceX代表的是前面有几个size,spaceY代表的是当前size的位移
    int spaceX, spaceY;
    int[][] zobrist = PuzzleProblem.getZobrist();

    private int[] index = new int[3];
    static final int[] tilePositions = {-1, 0, 0, 1, 2, 1, 2, 0, 1, 3, 4, 2, 3, 5, 4, 5};
    static final int[] tileSubsets = {-1, 1, 0, 0, 0, 1, 1, 2, 2, 1, 1, 2, 2, 1, 2, 2};
    /**
     * 空格采取action移动
     * @param action
     */
    public void move(Action action) {
        Direction dir = ((PuzzleAction)action).getDirection();
        int[] offsets = Direction.offset(dir);
        // 要移动到的位置,前面有x个size,位移是y
        int x = spaceX + offsets[0];
        int y = spaceY + offsets[1];
        int space = spaceX * size + spaceY; // 空格位置
        int pos = x * size + y; // 要移动到的点的位置

        // 计算hash值
        // hash ^= zobrist[space][0];
        hash ^= zobrist[pos][board[pos]];
        hash ^= zobrist[space][board[pos]];
        // hash ^= zobrist[pos][0];

        // 计算index
        int tile = board[pos];
        // 计算pos上的tile在哪个组
        int id = tileSubsets[tile];
        // 先异或之前的值，这样取消了之前或的值
        // 位数
        index[id] ^= ((pos - 1) << (tilePositions[tile] << 2));
        //再或上新的值
        index[id] |= ((space - 1) << (tilePositions[tile] << 2));

        // 移动后,空格在pos,要移动到的点在space
        int tmp = board[space];
        board[space] = board[pos];
        board[pos] = tmp;

        // 新的空格的位置
        spaceX = x;
        spaceY = y;
    }

    /**
     * 返回disjoint启发值
     * @param st 状态
     * @return disjoint启发值
     */
    public int disjoint(PuzzleState st) {
        return PuzzleConfiguration.costTable_15_puzzle_0[index[0]] +
                PuzzleConfiguration.costTable_15_puzzle_1[index[1]] +
                PuzzleConfiguration.costTable_15_puzzle_2[index[2]];
    }


    /**
     *
     * @return hash值
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * 通过hash值判断两个PuzzleState对象的棋盘是否一样
     * @param obj PuzzleState对象
     * @return 相等返回true
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj instanceof PuzzleState) {

            PuzzleState another = (PuzzleState) obj;
            int[] aBoard = another.board;
            for (int i = 1; i <= size * size; ++i) {
                if (board[i] != aBoard[i])
                    return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 构造函数，处理输入等
     */
    public PuzzleState(int size) {
        this.size = size;
        board = new int[size * size + 1];
    }

    /**
     * 处理棋盘的输入
     * @param io 输入
     */
    public void read(In io) {
        for (int i = 1; i <= size * size; ++i) {
                board[i] = io.readInt();
                // 计算hash值

                if (board[i] == 0) {
                    spaceX = (i - 1) / size;
                    spaceY = i - spaceX * size;
                }
                else
                    hash ^= zobrist[i][board[i]];
        }
        // 计算开始状态的Disjoint启发值
        for (int pos = 16; pos >= 1; --pos) {
            final int tile = board[pos];
            if (tile != 0) {
                final int subsetNumber = tileSubsets[tile];
                index[subsetNumber] |=
                        (pos - 1) << (tilePositions[tile] << 2);
            }
        }
    }

    /**
     * 复制构造函数
     * @param state state
     */
    public PuzzleState(PuzzleState state) {
        this.size = state.size;
        this.zobrist = state.zobrist;
        this.board = new int[size * size + 1];
        this.board = state.board.clone();

        spaceX = state.spaceX;
        spaceY = state.spaceY;

        this.hash = state.hash;
        // 复制构造函数处理index
        this.index = new int[3];
        this.index = state.index.clone();
    }


    /**
     * 打印棋盘
     */
    @Override
    public void draw() {
        for (int i = 1; i <= size * size; ++i) {
            if (size == 4)
                System.out.printf("%-2d", board[i]);
            else System.out.print(board[i]);
            if (i % size == 0) System.out.println();
            else System.out.print(' ');
        }
    }

    public void setBoard(int[] board) {
        this.board = board;
    }

    /**
     * 当前状态采用action而进入的下一个状态
     *
     * @param action 当前要采用的操作
     * @return 采用操作后的state
     */
    @Override
    public State next(Action action) {
        PuzzleState state = new PuzzleState(this);
        state.move(action);
        return state;
    }

    /**
     * 当前状态下可以采用的所有Action
     *
     * @return 返回当前左右可采用Action的列表
     */
    @Override
    public ArrayList<Action> actions() {
        ArrayList<Action> list = new ArrayList<>();

        for (Direction d : directions) {
            if (isLegal(d)) list.add(new PuzzleAction(d));
        }
        return list;
    }

    /**
     * 判断采取actions后空格的位置是否在棋盘内
     * @param d 方向
     * @return {true}, 在棋盘内
     */
    boolean isLegal(Direction d) {
        int[] offsets = Direction.offset(d);
        int x = spaceX + offsets[0];
        int y = spaceY + offsets[1];

        boolean ok1 = (x >= 0 && x < size && y >= 1 && y <= size);

        return ok1;
    }


    public int getSize() {
        return size;
    }

    public int[] getBoard() {
        return board;
    }

    //枚举映射，存放不同类型的启发函数
    private static final EnumMap<HeuristicType, Predictor> predictors = new EnumMap<>(HeuristicType.class);
    static{
        predictors.put(HeuristicType.MISPLACED,
                (state, goal) -> ((PuzzleState)state).misplaced((PuzzleState)state));
        predictors.put(HeuristicType.MANHATTAN,
                (state, goal) -> ((PuzzleState)state).manhattan((PuzzleState)state));
        predictors.put(HeuristicType.DISJOINT_PATTERN,
                (state, goal) -> ((PuzzleState)state).disjoint((PuzzleState)state));
    }
    public static Predictor predictor(HeuristicType type){
        return predictors.get(type);
    }




    public int manhattan(PuzzleState st) { return 0; }

    public int misplaced(PuzzleState st) { return 0; }

    public int getSpaceX() {
        return spaceX;
    }
    public int getSpaceY() {
        return spaceY;
    }

    public static void main(String[] args) {
        In in = new In();
        PuzzleProblem.init(6);
        PuzzleConfiguration.init();
//        Scanner scanner = new Scanner(System.in);
        PuzzleState state = new PuzzleState(4);
        state.read(in);
        state.draw();
        System.out.println("disjoint = " + state.disjoint(state));
        System.out.println();
        for (Action action : state.actions()) {
            PuzzleState child = (PuzzleState) state.next(action);

            child.draw();
            System.out.println("disjoint = " + child.disjoint(child));

        }

    }
}
