package SortingAlgorithm;

import java.lang.reflect.Array;

public class PriorityQueue<T extends Comparable<T>> {

    private Heap<T> pq;

    public PriorityQueue(Class<T> _clazz) {
        pq = new Heap<>(_clazz);
    }

    public void pop() {
        pq.pop();
    }

    public void push(T data) {
        pq.add(data);
    }

    public T top() {
        return pq.top();
    }
}



class Heap<T extends Comparable<T>> {

    private T[] heap;
    private int capacity;
    private int pos;
    private Class<T> clazz;


    public Heap(Class<T> _clazz) {

        clazz = _clazz;
        capacity = 10;
        pos = 0;

        heap = (T[]) Array.newInstance(clazz, capacity);
    }

    private void swap(int idx1, int idx2) {
        T tmp = heap[idx1];
        heap[idx1] = heap[idx2];
        heap[idx2] = tmp;
    }

    private boolean empty() {
        return pos == 0;
    }


    private void doubleTree() {

        T[] newHeap = (T[])Array.newInstance(clazz, capacity * 2);

        for(int i = 0; i < capacity; ++i)
            newHeap[i] = heap[i];

        capacity = capacity * 2;

        heap = newHeap;
    }

    public void pop() {
        getMin();
    }

    public T top() {
        return (empty()) ? null : heap[1];
    }

    public void add(T data) {

        if(pos == capacity - 1)
            doubleTree();

        heap[++pos] = data;

        int tPos = pos;
        int parPos = tPos/2;

        while(0 < tPos && 0 < parPos) {

            if(heap[tPos].compareTo(heap[parPos]) < 0)
                swap(tPos, parPos);
            else
                break;

            tPos = parPos;
            parPos = tPos/2;
        }
    }

    public T getMin() {

        if(empty()) return null;

        T returnVal = heap[1];

        swap(1, pos);

        pos--;

        int tPos = 1;

        while(tPos <= pos) {

            int left = tPos * 2;
            int right = tPos * 2 + 1;

            if(right <= pos) {

                int smaller = (heap[left].compareTo(heap[right]) < 0) ? left : right;

                if(heap[tPos].compareTo(heap[smaller]) > 0) {
                    swap(tPos, smaller);
                    tPos = smaller;
                } else {
                    break;
                }

            } else if(left <= pos) {

                if(heap[tPos].compareTo(heap[left]) > 0) {
                    swap(tPos, left);
                    tPos = left;
                } else {
                    break;
                }

            } else {
                break;
            }
        }

        return returnVal;
    }

    public void printTree() {

        if(empty()) return;

        // level 순회
        for(int i = 1; i <= pos; ++i)
            System.out.println(heap[i]);
    }
}
