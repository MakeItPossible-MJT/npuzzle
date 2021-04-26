package s18020031058.problem.npuzzle;

import algs4.util.In;
import core.problem.Problem;
import core.problem.State;
import core.runner.EngineFeeder;
import core.runner.HeuristicType;
import core.solver.Node;
import core.solver.Searcher;
import core.solver.heuristic.AbstractFrontier;
import core.solver.heuristic.EvaluationType;
import core.solver.heuristic.Predictor;
import s18020031058.solver.heuristic.Frontier;
import s18020031058.solver.heuristic.IdaSearch;
import s18020031058.solver.heuristic.PuzzleConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 寻路问题的EngineFeeder
 */
public class FeederMa extends EngineFeeder {
    /**
     * 从文件输入流中读入NPuzzle问题的实例
     *
     * @param io 输入流
     * @return 文件中所有NPuzzle实例
     */
    @Override
    public ArrayList<Problem> getProblems(In io) {

        ArrayList<Problem> problems = new ArrayList<>();

        // 初始化zobrist数组
        PuzzleProblem.init(6);

        while (io.hasNextLine()) {
            int size = io.readInt();

            PuzzleState initialState = new PuzzleState(size);
            PuzzleState goalState = new PuzzleState(size);
            initialState.read(io);
            goalState.read(io);
            PuzzleProblem problem = new PuzzleProblem(initialState, goalState, size);
            problems.add(problem);
        }
        return problems;
    }

    @Override
    public ArrayList<Problem> getProblems(Scanner scanner) {
        return null;
    }

    /**
     * 生成采取某种估值机制的Frontier
     *
     * @param type 结点评估器的类型
     * @return 使用该评估机制的一个Frontier实例
     */
    @Override
    public AbstractFrontier getFrontier(EvaluationType type) {
        return new Frontier(Node.evaluator(type));
    }

    /**
     * 获得对状态进行估值的Predictor
     *
     * @param type 估值函数的类型
     * @return 估值函数
     */
    @Override
    public Predictor getPredictor(HeuristicType type) {
        return PuzzleState.predictor(type);
    }

    /**
     * 生成IdaStar搜索的一个实例
     */
    @Override
    public Searcher getIdaStar() {
        return getIdaStar(HeuristicType.MANHATTAN);
    }

    public Searcher getIdaStar(HeuristicType type) {

        //使用类型为type的启发函数
        return new IdaSearch(PuzzleState.predictor(type));
    }

    /**
     * 用来提交头歌评分的Searcher
     *
     * @return 搜索引擎
     */
    @Override
    public Searcher getScoreSearcher() {
        PuzzleConfiguration.init();
//        return getAStar(HeuristicType.MANHATTAN);

        return getIdaStar(HeuristicType.DISJOINT_PATTERN);
    }

}
