package s18020031058.solver.heuristic;


import java.util.*;


import core.problem.Problem;
import core.solver.*;
import core.solver.heuristic.Predictor;


/**
 * 不能被继承的类，final类
 */
public final class IdaSearch implements Searcher {
    private Node goal;
    private long num = 0;
    private long cnt = 0;
    private final Predictor predictor;
    private Problem problem;
    /**
     *
     * @param predictor 估计器
     */
    public IdaSearch(Predictor predictor) {
        this.predictor = predictor;
    }

//    static final int[] tilePositions = {-1, 0, 0, 1, 2, 1, 2, 0, 1, 3, 4, 2, 3, 5, 4, 5};
//    static final int[] tileSubsets = {-1, 1, 0, 0, 0, 1, 1, 2, 2, 1, 1, 2, 2, 1, 2, 2};
    // 得到启发值
//    public static int getH(int []tmp) {
//        int index0 = 0, index1 = 0, index2 = 0;
//        for (int pos = 16; pos >= 1; --pos) {
//            final int tile = tmp[pos];
//            if (tile != 0) {
//                final int subsetNumber = tileSubsets[tile];
//                switch (subsetNumber) {
//                    case 2:
//                        index2 |= (pos - 1) << (tilePositions[tile] << 2);
//                        break;
//                    case 1:
//                        index1 |= (pos - 1) << (tilePositions[tile] << 2);
//                        break;
//                    default:
//                        index0 |= (pos - 1) << (tilePositions[tile] << 2);
//                        break;
//                }
//            }
//        }
//        return PuzzleConfiguration.costTable_15_puzzle_0[index0] +
//                PuzzleConfiguration.costTable_15_puzzle_1[index1] +
//                PuzzleConfiguration.costTable_15_puzzle_2[index2];
//    }

    /**
     * ida*
     *
     * @param problem 问题
     * @return 解决路径
     */
    @Override
    public Deque<Node> search(Problem problem) {
        //如果可直接判断问题是否可解，无解时直接返回解路径为null
        if (!problem.solvable()) {
            return null;
        }
        this.problem = problem;
        //搜索树的根节点
        Node root = problem.root(predictor);
        num = 0;
        cnt = 0;
        goal = null;
        int val;
//        int[] board = ((PuzzleState)root.getState()).getBoard();
        int eval = root.evaluation();
//        System.out.println("eval = " + eval);
        int i = 1;
        for (int maxDepth = eval; maxDepth > 0; maxDepth = val) {
            val = dfs(root, maxDepth, 0);
            // System.out.println("hashSet.size = " + hashSet.size());
//            System.out.println("maxDepth = " + maxDepth + " cnt = " + cnt);
            num = Math.max(cnt, num);
            if (goal != null) break;

            cnt = 0;

        }
        return generatePath(goal);
    }

    public int dfs(Node node, int maxDepth, int depth) {

        int minDepth = 0x7fffffff;
        //minDepth = 0;
        //int h = node.evaluation();
        //else System.out.println("hello in dfs");
        int h = node.getHeuristic();

        if (h + depth > maxDepth) {
            minDepth = Math.min(h + depth, minDepth);
            return minDepth;
        }
        cnt++;
        if (h == 0) {
            goal = node;
            return goal.evaluation();
        }

        for (Node child : problem.childNodes(node, predictor)) {
            if (goal != null) return minDepth;
            if (!child.equals(node.getParent())) {
                int cost = dfs(child, maxDepth, depth + 1);
                minDepth = Math.min(cost, minDepth);
            }
        }
        return minDepth;
    }


    public long expandedNode() {
        return num;
    }

}