package s18020031058.solver.heuristic;

import core.problem.State;
import core.solver.Node;
import core.solver.heuristic.AbstractFrontier;
import s18020031058.problem.npuzzle.DisjointState;

import java.util.*;

/**
 * Student01开发的”超级棒“的Frontier数据结构
 */
public class Frontier extends AbstractFrontier {
    /**
     * My secret wonderful data structures! :)
     *
     */
    private final PriorityQueue<Node> pq;
    private final HashMap<State, Node> hashMap;
    /**
     *
     * @param comparator
     */
    public Frontier(Comparator<Node> comparator) {
        super(comparator);
        pq = new PriorityQueue<>(comparator);
        hashMap = new HashMap<>();
    }

    @Override
    public boolean isEmpty() {
        return pq.isEmpty();
    }

    /**
     * 获取 Frontier 中，状态为 s 的节点
     *
     * @param s 状态
     * @return 存在：  相应的状态为 s 的节点；
     * 不存在：null
     */
    @Override
    protected Node getNode(State s) {
        return hashMap.get(s);
    }

    /**
     * Only return the max Node
     * @return the max Node
     */
    @Override
    public Node peek() {
        if (pq.isEmpty()) return null;
        return pq.peek();
    }

    /**
     * 用节点 e 替换掉具有相同状态的旧节点 oldNode
     *
     * @param oldNode
     * @param e
     */
    @Override
    public void replace(Node oldNode, Node e) {
        hashMap.put(oldNode.getState(), e);
        pq.add(e);
    }

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    @Override
    public Iterator<Node> iterator() {
        return pq.iterator();
    }

    @Override
    public int size() {
        return pq.size();
    }

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to {@link #add}, which can fail to insert an element only
     * by throwing an exception.
     *
     * @param node the element to add
     * @return {@code true} if the element was added to this queue, else
     * {@code false}
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this queue
     * @throws NullPointerException     if the specified element is null and
     *                                  this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *                                  prevents it from being added to this queue
     */
    @Override
    public boolean offer(Node node) {
        pq.add(node);
        hashMap.put(node.getState(), node);
        return true;
    }

    @Override
    public boolean add(Node node) {
        return offer(node);
    }

    /**
     * Get the max Node and delete it from the frontier
     * @return the max Node
     */
    @Override
    public Node poll() {
        if (pq.isEmpty()) return null;
        // node有的为null，但是offer是在两个里都添加的。
        // 说明pq里有重复state的Node，
        // 所以如果hashMap里没有，
        // 证明具有相同state，但是启发值更优的已经被探索，
        // 所以直接略掉
        Node node = pq.poll();
        while (!hashMap.containsKey(node.getState())) {
            node = pq.poll();
        }
        return node;
    }

    @Override
    public boolean contains(Object obj) {
        if (obj == this) return true;

        else if (obj instanceof Node) {
            // 判断hashMap里是否有这个节点的hash值
            Node node = (Node) obj;
            return hashMap.get(node.getState()) != null;
        }
        return false;
    }

    @Override
    public void clear() {
        pq.clear();
        hashMap.clear();
    }
}

