package SortingAlgorithm;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;

public interface ListInterface<T> {

    /*
     * Name        : size
     * Parameter   : void
     * return      : int
     * Description : return # of nodes
     */
    public int size();

    /*
     * Name        : isEmpty
     * Parameter   : void
     * return      : boolean
     * Description : check weather tree is empty or not
     */
    public boolean isEmpty();

    /*
     * Name        : push_back
     * Parameter   : data : T
     * return      : null
     * Description : 재활용 가능한 노드가 있으면 사용하고 없으면 새로운 노드를 만들어 리스트 맨 뒤에 연결
     */
    public void push_back(T data);

    /*
     * Name        : push_front
     * Parameter   : data : T
     * return      : null
     * Description : 재활용 가능한 노드가 있다면 사용하고, 없다면 새로운 노드를 만들어 맨 앞에 연결.
     */
    public void push_front(T data);


    /*
     * Name        : insert
     * Parameter   : idx : int, data : T
     * return      : null
     * Description : 지정된 위치에 data 삽입. 위치가 리스트의 크기보다 크거나 빈 리스트인경우 맨 뒤에 연결.
     */
    public void insert(int idx, T data);

    /*
     * Name        : remove_front
     * Parameter   : void
     * return      : null
     * Description : 맨 앞의 노드를 삭제(재활용 리스트에 연결). 빈 리스트면 삭제를 수행하지 않고 종료한다.
     */
    public void remove_front();

    /*
     * Name        : remove_tail
     * Parameter   : void
     * return      : null
     * Description : 맨 뒤의 노드를 삭제(재활용 리스트에 연결). 빈 리스트면 삭제를 수행하지 않고 종료한다.
     */
    public void remove_tail();

    /*
     * Name        : removeByIndex
     * Parameter   : idx : int
     * return      : null
     * Description : 입력된 인덱스 위치에 있는 노드를 삭제(재활용 리스트에 연결). 인덱스가 범위를 벗어나거나 빈 리스트면 삭제를 수행하지 않고 종료.
     */
    public void removeByIndex(int idx);


    /*
     * Name        : removeByData
     * Parameter   : data : T
     * return      : null
     * Description : 입력된 데이터를 탐색하면서 발견하면 해당 노드 삭제(재활용 리스트에 연결). 데이터가 없으면 종료.
     */
    public void removeByData(T data);


    /*
     * Name        : removeAll
     * Parameter   : void
     * return      : null
     * Description : 모든 노드를 삭제한다(재활용 리스트에 연결)
     */
    public void removeAll();


    /*
     * Name        : search
     * Parameter   : data : T
     * return      : null
     * Description : 입력된 데이터를 리스트에서 검색한다. 빈 리스트라면 검색을 수행하지 않고 종료한다.
     */
    public boolean search(T data);


    // Runtime 도중에 Generic Type인 T 를 찾을방법은..메인에서 클래스 타입을 넘겨주는 방법..
    // 다른 방법 찾아보기
    public void sort(Class<T> clazz);


    /*
     * Name        : printList
     * Parameter   : void
     * return      : null
     * Description : 리스트를 출력한다.
     */
    public void printList() throws IOException;


}


/*
 * Class       : Node<T extends Comparable<T>>
 * Description : Node for LinkedList.
 * Member      : data : T        - data
 *             : next : Node<T>  - next node pointer
 */

public class BidirectionalNode<T> {

    public T data;
    public BidirectionalNode<T> left;
    public BidirectionalNode<T> right;

    // Constructor
    public BidirectionalNode(T _data) {
        data = _data;
        left = null;
        right = null;
    }

    public BidirectionalNode(T _data, BidirectionalNode<T> _left, BidirectionalNode<T> _right) {
        data = _data;
        left = _left;
        right = _right;
    }
}


public class BidirectionalAvailableList<T extends Comparable<T>> {

    private BidirectionalNode<T> avHeader;
    private BidirectionalNode<T> avTail;

    public BidirectionalAvailableList() {
        avHeader = avTail = null;
    }

    /*
     * Name        : getAvailableNode
     * Parameter   : data : T, left : BidirectionalNode<T>, right : BidirectionalNode<T>
     * return      : BidirectionalNode<T>
     * Description : 재활용이 가능한 노드가 있을 때, 노드의 값을 매개변수의 값으로 변경한 후 반환
     */
    public BidirectionalNode<T> getAvailableNode(T data, BidirectionalNode<T> left, BidirectionalNode<T> right) {

        if(!isExistAvailableNode()) return null;

        BidirectionalNode<T> recycleNode = avHeader;

        avHeader = avHeader.right;

        recycleNode.left = left;
        recycleNode.right = right;
        recycleNode.data = data;

        return recycleNode;
    }

    /*
     * Name        : addRemoveNode
     * Parameter   : removedNode : BidirectionalNode<T>
     * return      : null
     * Description : 재활용할 노드를 추가
     */
    public void addRemovedNode(BidirectionalNode<T> removedNode) {

        if(removedNode == null) return;

        removedNode.left = null;
        removedNode.right = null;

        if(avHeader == null) avHeader = avTail = removedNode;

        else {
            removedNode.right = avHeader;
            avHeader = removedNode;
        }
    }

    /*
     * Name        : addRemovedNodes
     * Parameter   : header : BidirectionalNode<T>, tail : BidirectionalNode<T>
     * return      : null
     * Description : 리스트 자체를 삭제할 때 available list 의 tail에 붙여넣는다.
     */
    public void addRemovedNodes(BidirectionalNode<T> header, BidirectionalNode<T> tail) {

        if(avHeader == null) {
            avHeader = header;
            avTail = tail;
        } else {
            avTail.right = header;
            header.left = avTail;
            avTail = tail;
        }
    }

    /*
     * Name        : isExistAvailableNode
     * Parameter   : void
     * return      : boolean
     * Description : 재활용 가능한 노드가 있으면 true, 없으면 false 반환
     */
    private boolean isExistAvailableNode() {
        return avHeader != null;
    }
}



public class BidirectionalList<T extends Comparable<T>> implements ListInterface<T> {

    private BidirectionalNode<T> header;
    private BidirectionalNode<T> tail;
    private int size;

    private BidirectionalAvailableList<T> avList;


    public BidirectionalList() {
        header = tail = null;
        size = -1;
        avList = new BidirectionalAvailableList<>();
    }

    public BidirectionalList(BidirectionalNode<T> node) {
        header = tail = node;
        size = 1;
        avList = new BidirectionalAvailableList<>();
    }

    private void makeList(T data, BidirectionalNode<T> recycleNode) {
        header = tail = (recycleNode == null) ? new BidirectionalNode<>(data) : recycleNode;
        size = 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return header == null;
    }

    @Override
    public void push_back(T data) {

        BidirectionalNode<T> recycleNode = avList.getAvailableNode(data, tail, header);

        if(isEmpty()) {
            makeList(data, recycleNode);
            return;
        }

        recycleNode = (recycleNode == null) ? new BidirectionalNode<>(data, tail, header) : recycleNode;

        tail.right = recycleNode;
        header.left = recycleNode;
        tail = recycleNode;
        size++;
    }

    @Override
    public void push_front(T data) {

        BidirectionalNode<T> recycleNode = avList.getAvailableNode(data, tail, header);

        if(isEmpty()) {
            makeList(data, recycleNode);
            return;
        }

        recycleNode = (recycleNode == null) ? new BidirectionalNode<>(data, tail, header) : recycleNode;

        header.left = recycleNode;
        tail.right = recycleNode;
        header = recycleNode;
        size++;
    }

    @Override
    public void insert(int idx, T data) {

        if(isEmpty() || size <= idx) {
            push_back(data);
            return;
        } else if(idx == 0) {
            push_front(data);
            return;
        }

        BidirectionalNode<T> preNode = header;
        BidirectionalNode<T> curNode = preNode;

        // 중간 기준 왼쪽 - 오른쪽 타고가기
        if(idx <= size / 2)
            for(int i = 0; i < idx; ++i, preNode = curNode, curNode = curNode.right);

        // 중간 기준 오른쪽 - 왼쪽 타고가기
        else {
            for(int i = 0; i <= size - idx; ++i, preNode = curNode, curNode = curNode.left);

            BidirectionalNode<T> temp = preNode;
            preNode = curNode;
            curNode = temp;
        }

        BidirectionalNode<T> recycleNode = avList.getAvailableNode(data, preNode, curNode);

        recycleNode = (recycleNode == null) ? new BidirectionalNode<>(data, preNode, curNode) : recycleNode;

        preNode.right = recycleNode;
        curNode.left = recycleNode;

        size++;
    }

    @Override
    public void remove_front() {

        if(isEmpty()) return;
        if(size == 1) {
            removeAll();
            return;
        }

        BidirectionalNode<T> removedNode = header;

        header = header.right;
        header.left = tail;
        tail.right = header;

        avList.addRemovedNode(removedNode);

        size--;
    }

    @Override
    public void remove_tail() {

        if(isEmpty()) return;
        if(size == 1) {
            removeAll();
            return;
        }

        BidirectionalNode<T> removedNode = tail;

        tail = tail.left;
        tail.right = header;
        header.left = tail;

        avList.addRemovedNode(removedNode);

        size--;
    }

    @Override
    public void removeByIndex(int idx) {

        if(isEmpty() || size <= idx) return;
        if(idx == 0) {
            remove_front();
            return;
        } else if(idx == size - 1) {
            remove_tail();
            return;
        }

        BidirectionalNode<T> preNode = header;
        BidirectionalNode<T> curNode = preNode;

        // 중간 기준 왼쪽 - 오른쪽 타고가기
        if(idx <= size / 2)
            for(int i = 0; i < idx; ++i, preNode = curNode, curNode = curNode.right);

            // 중간 기준 오른쪽 - 왼쪽 타고가기
        else {
            for(int i = 0; i <= size - idx; ++i, preNode = curNode, curNode = curNode.left);

            BidirectionalNode<T> temp = preNode;
            preNode = curNode;
            curNode = temp;
        }

        // curNode 삭제
        BidirectionalNode<T> removedNode = curNode;
        preNode.right = curNode.right;
        curNode.right.left = preNode;

        avList.addRemovedNode(removedNode);

        size--;
    }

    @Override
    public void removeByData(T data) {

        if(isEmpty()) return;
        else if(tail.data == data) {
            remove_tail();
            return;
        }

        BidirectionalNode<T> preNode = header;
        BidirectionalNode<T> curNode = preNode;

        for(;  curNode != tail && curNode.data != data; preNode = curNode, curNode = curNode.right);

        if(curNode == tail) return;

        BidirectionalNode<T> removedNode = curNode;

        preNode.right = curNode.right;
        curNode.right.left = preNode;

        avList.addRemovedNode(removedNode);

        size--;
    }

    @Override
    public void removeAll() {
        avList.addRemovedNodes(header, tail);
        header = tail = null;
        size = -1;
    }

    @Override
    public boolean search(T data) {

        if(isEmpty()) return false;
        if(tail.data == data) return true;

        BidirectionalNode<T> preNode = header;
        BidirectionalNode<T> curNode = preNode;

        for(; curNode.data != data && curNode != tail; preNode = curNode, curNode = curNode.right);

        if(curNode == tail) return false;
        else return true;
    }

    @Override
    public void sort(Class<T> clazz) {
        T[] arr = (T[])Array.newInstance(clazz, size);

        BidirectionalNode<T> node = header;

        for(int i = 0; node != tail; node = node.right, ++i)
            arr[i] = node.data;

        arr[size - 1] = node.data;

        Arrays.sort(arr);

        node = header;

        for(int i = 0; node != tail; node = node.right, ++i)
            node.data = arr[i];

        tail.data = arr[size - 1];
    }

    @Override
    public void printList() throws IOException {

        if (isEmpty()) return;

        BidirectionalNode<T> startNode = header;

        OutputStream out = new BufferedOutputStream(System.out);

        for (; startNode != tail; startNode = startNode.right)
            out.write((startNode.data.toString() + "\n").getBytes());

        out.write((tail.data + "\n").getBytes());

        out.flush();

    }
}

