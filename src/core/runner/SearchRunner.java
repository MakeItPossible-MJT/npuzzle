package core.runner;

import algs4.util.Stopwatch;
import core.problem.Problem;
import core.solver.Node;
import algs4.util.In;
import core.solver.heuristic.BestFirstSearch;
import s18020031058.problem.npuzzle.FeederMa;
import s18020031058.solver.heuristic.IdaSearch;

import java.util.ArrayList;
import java.util.Deque;

public class SearchRunner {

    public static void main(String[] args) {

        //从文件中读入问题的实例，寻路问题
        In problemInput = new In("resources/pathfinding.txt");

        //生成一个具体的EngineFeeder：FeederXu，引擎饲养员徐老师:)
        EngineFeeder feeder = new FeederMa();

        //feeder从文件获取所有问题实例
        ArrayList<Problem> problems = feeder.getProblems(problemInput);

        //从Feeder获取所使用的搜索引擎 AStar

        IdaSearch astar = (IdaSearch) feeder.getScoreSearcher();
//        BestFirstSearch astar = (BestFirstSearch) feeder.getScoreSearcher();
        double time = 0;
        for (Problem problem : problems){
            Stopwatch timer = new Stopwatch();
            //使用AStar引擎求解问题
            Deque<Node> path = astar.search(problem);
            time = timer.elapsedTime();
            System.out.printf("time = %.3fs\n", time);

            //解的可视化
            // problem.showSolution(path);
            //仅打印路径
            // problem.printPath(path);
            System.out.println("扩展结点数 = " + astar.expandedNode());
            System.out.println();
        }

        System.out.println("==============================================================");
    }
}