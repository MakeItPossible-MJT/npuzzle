package s18020031058.solver.heuristic;

import s18020031058.problem.npuzzle.DisjointState;
import s18020031058.problem.npuzzle.PuzzleProblem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class DisjointPatternDB {
    private static byte[][] database;

    static {
        database = new byte[3][];
        database[0] = new byte[4096 + 1];
        database[1] = new byte[16777216 + 1];
        database[2] = new byte[16777216 + 1];

    }

    public static void bfs() {
        int size = 4;
        int cnt = 0;
        int[][] board = new int[3][];
        board[0] = new int[]{0, 0, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        board[1] = new int[]{0, 1, 0, 0, 0, 5, 6, 0, 0, 9, 10, 0, 0, 13, 0, 0, 0};
        board[2] = new int[]{0, 0, 0, 0, 0, 0, 0, 7, 8, 0, 0, 11, 12, 0, 14, 15, 0};

        Set<DisjointState> hashSet = new HashSet<>();
        Queue<DisjointState> q = new ArrayDeque<>();

        DisjointState curState;
        for (int i = 0; i < 3; ++i) {
            hashSet.clear();
            q.clear();
            cnt = 0;

            DisjointState initState = new DisjointState(size, board[i]);
            q.add(initState);

            while (!q.isEmpty()) {
                curState = q.poll();
                if (hashSet.contains(curState)) continue;
                hashSet.add(curState);
                // 写入到database数组
                int pos = getPos(curState.getBoard(), i);
                database[i][pos] = (byte) curState.getPathCost();

                ArrayList<DisjointState> list;
                for (int j = 1; j <= size * size; ++j) {
                    list = curState.move(j);
                    if (list.isEmpty()) continue;

                    for (DisjointState child : list) {
                        if (!hashSet.contains(child)) {
                            q.add(child);
                        }
                    }
                }
            }
        }
    }

    // tile在对应的组中的位置，从0开始，比如2在第0组的位置是0,3在第0组的位置是1
    static final int[] tilePositions
            = {-1, 0, 0, 1, 2, 1, 2, 0, 1, 3, 4, 2, 3, 5, 4, 5};

    public static int getPos(int[] board, int i) {
        int id = 0;
        for (int pos = 16; pos >= 1; --pos) {
            final int tile = board[pos];
            if (tile != 0) {
                id |= (pos - 1) << (tilePositions[tile] << 2);
            }
        }
        return id;
    }

    public static void writeInFile() {
        String path = "/home/mjt/Documents/大三下/人工智能/Project1--NPuzzle/海面宝宝组-Npuzzle项目源码第12阶段-马俊腾-18020031058";
        for (int i = 0; i < 3; ++i) {
            String filename = "database" + Integer.toString(i + 1) + ".txt";
            try {
                FileOutputStream out = new FileOutputStream(filename);
                out.write(database[i]);

            }
            catch (IOException e) {
                System.out.println("error");
            }
        }
    }

    public static void main(String[] args) {
        PuzzleProblem.init(6);
        DisjointPatternDB.bfs();
        DisjointPatternDB.writeInFile();
    }
}
