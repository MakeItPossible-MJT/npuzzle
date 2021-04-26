package s18020031058.solver.heuristic;

import java.io.*;

//模式数据库生成器

/**
 * Examples:
 * When generating a complete pattern database (i.e. no dummy tiles):
 * java PatternDatabaseGenerator 8 1,2,3,4,5,6,7,8,0 8-puzzle.db
 * <p>
 * When generating a disjoint additive pattern database (i.e. dummy tiles 'x'):
 * java PatternDatabaseGenerator 15 0,2,3,4,x,x,x,x,x,x,x,x,x,x,x,0     15-puzzle-663-0.db
 * java PatternDatabaseGenerator 15 1,x,x,x,5,6,x,x,9,10,x,x,13,x,x,0   15-puzzle-663-1.db
 * java PatternDatabaseGenerator 15 x,x,x,x,x,x,7,8,x,x,11,12,x,14,15,0 15-puzzle-663-2.db
 */
public final class PuzzleConfiguration {

    public static final byte[] costTable_15_puzzle_0 = new byte[4096 + 1],
            costTable_15_puzzle_1 = new byte[16777216 + 1],
            costTable_15_puzzle_2 = new byte[16777216 + 1];

//    static {
//        loadStreamCostTable("database1.txt", costTable_15_puzzle_0);
//        loadStreamCostTable("database2.txt", costTable_15_puzzle_1);
//        loadStreamCostTable("database3.txt", costTable_15_puzzle_2);
//    }

    public static void init() {
        loadStreamCostTable("database1.txt", costTable_15_puzzle_0);
        loadStreamCostTable("database2.txt", costTable_15_puzzle_1);
        loadStreamCostTable("database3.txt", costTable_15_puzzle_2);

        // writeInDisk();
    }

    public static void writeInDisk() {
        System.out.println("writeInDist in PuzzleConfiguration");
        String path = "/home/mjt/Documents/大三下/人工智能/Project1--NPuzzle/海面宝宝组-Npuzzle项目源码第12阶段-马俊腾-18020031058";
        String filename = "分区2.txt";
        try {
            FileWriter out = new FileWriter(new File(path + filename));
            for (int i = 0; i < costTable_15_puzzle_2.length; ++i) {
                //System.out.println("in writeInDisk in PuzzleConfiguration");
                out.write(Integer.toHexString(i) + " ");

                out.write(Integer.toString(costTable_15_puzzle_2[i]) + "\n");
            }
            out.close();
        } catch (IOException e) {
            System.out.println("error");
        }

    }

    private PuzzleConfiguration() {
    }

    private static void loadStreamCostTable(final String filename,
                                            final byte[] costTable) {
        InputStream is = PuzzleConfiguration.class.getResourceAsStream(filename);
        DataInputStream dis = null;
        try {
            if (is == null) {
                is = new FileInputStream(filename);
            }
            dis = new DataInputStream(new BufferedInputStream(is));
            int i = 0;
            while (true) {
                costTable[i++] = dis.readByte();
            }
        } catch (final EOFException eofe) {

        } catch (final FileNotFoundException fnfe) {
            System.err.println("Error: Cannot find file " + filename + ".");
            System.exit(1);
        } catch (final IOException ioe) {
            System.err.println("Error: Cannot read from file " + filename + ".");
            System.exit(1);
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (final IOException ioe) {
            }
        }
    }

    public static void main(String[] args) {
        init();

    }
}