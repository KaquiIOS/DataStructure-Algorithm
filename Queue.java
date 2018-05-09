package SortingAlgorithm;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;


public class AvailableList<T> {

    // Member of LinkedList<T extends Comparable<T>>
    private Node<T> avHeader;
    private Node<T> avTail;

    public AvailableList() {
        avHeader = avTail = null;
    }

    /*
     * Name        : getAvailableNode
     * Parameter   : data : T, next : Node<T>
     * return      : Node<T>
     * Description : if there are available nodes, return front node after applying parameter. 재활용이 가능한 노드가 있을 때, 노드의 값을 매개벼수의 값으로 변경한 후 반환
     */
    public Node<T> getAvailableNode(T data, Node<T> next) {

        if(!isExistAvailableNode()) return null;

        Node<T> recycleNode = avHeader;

        avHeader = avHeader.next;

        recycleNode.next = next;
        recycleNode.data = data;

        return recycleNode;
    }

    /*
     * Name        : addRemoveNode
     * Parameter   : removedNode : Node<T>
     * return      : null
     * Description : add removed node to available list
     */
    public void addRemovedNode(Node<T> removedNode) {

        if(removedNode == null) return;

        removedNode.next = null;

        if(avHeader == null) avHeader = avTail = removedNode;

        else {
            removedNode.next = avHeader;
            avHeader = removedNode;
        }
    }

    /*
     * Name        : addRemovedNodes
     * Parameter   : removedNodes : Node<T>, tail : Node<T>
     * return      : null
     * Description : 리스트 자체를 삭제할 때 available list 의 tail에 붙여넣는다.
     */
    public void addRemovedNodes(Node<T> headerNode, Node<T> tail) {

        if(avHeader == null) {
            avHeader = headerNode;
            avTail = tail;
        } else {
            avTail.next = headerNode;
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

/*
* Class       : LinkedList<T extends Comparable<T>>
* Description : 기본적인 링크드 리스트이다. Available List 를 사용하여 노드 재활용.
* Member      : avHeader : Node<T> - Available List header for adding or popping removedNode
*               avTail   : Node<T> - Available List tail for removeAll
*               header   : Node<T> - list header
*               tail     : Note<T> - list tail
*               size     : int     - List Size
*/

public class LinkedList <T extends Comparable<T>> implements ListInterface<T> {

    private Node<T> header;
    private Node<T> tail;
    private int size;

    private AvailableList<T> avList;

    // Constructor
    public LinkedList() {
        avList = new AvailableList<>();
        header = tail = null;
        size = -1;
    }

    public LinkedList(T data) {
        avList = new AvailableList<>();
        header = tail = new Node<>(data);
        size = 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty(){
        return header == null;
    }


    private void makeList(T data, Node<T> recycleNode) {
        header = tail = (recycleNode == null) ? new Node<>(data) : recycleNode;
        size = 1;
    }

    @Override
    public void push_back(T data) {

        Node<T> recycleNode = avList.getAvailableNode(data, null);

        recycleNode = (recycleNode == null) ? new Node<>(data) : recycleNode;

        if(isEmpty()) {
            makeList(data, recycleNode);
            return;
        }
        else {
            tail.next = recycleNode;
            tail = tail.next;
        }

        size++;
    }

    @Override
    public void push_front(T data) {

        Node<T> recycleNode = avList.getAvailableNode(data, header);

        if(isEmpty())
            makeList(data, recycleNode);
        else
            header = (recycleNode == null) ? new Node<>(data, header) : recycleNode;

        size++;
    }

    @Override
    public void insert(int idx, T data) {

        if(isEmpty() || size <= idx) {
            push_back(data);
        } else {
            Node<T> preNode = header;
            Node<T> curNode = preNode;

            for(int i = 0; i < idx; ++i, preNode = curNode, curNode = curNode.next);

            Node<T> recycleNode = avList.getAvailableNode(data, curNode);

            preNode.next = (recycleNode == null) ? new Node<>(data, curNode) : recycleNode;
        }

        size++;
    }

    @Override
    public void remove_front() {

        if(isEmpty()) return;

        Node<T> removedNode = header;
        header = header.next;

        avList.addRemovedNode(removedNode);

        size--;
    }

    @Override
    public void remove_tail() {

        if(isEmpty()) return;

        Node<T> removedNode = tail;

        Node<T> preNode = header;
        Node<T> curNode = preNode;

        for(; curNode != tail; preNode = curNode, curNode = curNode.next);

        tail = preNode;
        tail.next = null;

        avList.addRemovedNode(removedNode);

        size--;
    }

    @Override
    public void removeByIndex(int idx) {

        if(isEmpty() || size <= idx) return;
        else if(size == idx - 1) {
            remove_tail();
            return;
        }

        Node<T> preNode = header;
        Node<T> curNode = preNode;

        for(int i = 0; i < idx; ++i, preNode = curNode, curNode = curNode.next);

        Node<T> removedNode = curNode;
        preNode.next = removedNode.next;

        avList.addRemovedNode(removedNode);

        size--;
    }

    @Override
    public void removeByData(T data) {

        if(isEmpty()) return;

        Node<T> preNode = header;
        Node<T> curNode = preNode;

        for(; curNode.data != data && curNode != null ; preNode = curNode, curNode = curNode.next);

        if(curNode == null) return;

        Node<T> removedNode = curNode;
        preNode.next = curNode.next;

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
    public int search(T data) {

        if(isEmpty()) return -1;

        int idx = 0;

        Node<T> preNode = header;
        Node<T> curNode = preNode;

        for(; curNode.data != data && curNode != null ; idx++, preNode = curNode, curNode = curNode.next);

        if(curNode == null) return -1;
        else return idx;
    }

    @Override
    public void sort(Class<T> clazz) {

        //T[] arr = (T[])(new Object[size]);
        T[] arr = (T[])Array.newInstance(clazz, size);

        Node<T> node = header;

        for(int i = 0; node != null; node = node.next, ++i)
            arr[i] = node.data;

        Arrays.sort(arr);

        node = header;

        for(int i = 0; node != null; node = node.next, ++i)
            node.data = arr[i];
    }

    @Override
    public void printList() throws IOException {

        if(isEmpty()) return;

        Node<T> startNode = header;

        OutputStream out = new BufferedOutputStream(System.out);

        for(; startNode != null; startNode = startNode.next)
            out.write((startNode.data.toString() + "\n").getBytes());

        out.flush();
    }

    @Override
    public T tail() {
        return tail.data;
    }

    @Override
    public T front() {
        return header.data;
    }
}

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
    public int search(T data);


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

    public T tail();

    public T front();

}

/*
 * Class       : Node<T extends Comparable<T>>
 * Description : Node for LinkedList.
 * Member      : data : T        - data
 *             : next : Node<T>  - next node pointer
 */

public class Node<T> {

    public T data;
    public Node<T> next;

    // Constructor
    public Node(T _data) {
        data = _data;
        next = null;
    }

    public Node(T _data, Node<T> _next) {
        data = _data;
        next = _next;
    }
}

public class QueueEmptyException extends Exception {

    public QueueEmptyException() {
        super("QueueEmptyException");
    }

    public QueueEmptyException(String msg) {
        super(msg);
    }
}

package SortingAlgorithm;

public class Queue<T extends Comparable<T>> {

    LinkedList<T> queue;
    private int size;

    public Queue() {
        queue = new LinkedList<>();
        size = -1;
    }

    public boolean empty() {
        return size == -1;
    }

    public int size() {
        return size;
    }

    public boolean add(T data) {

        if(data == null) return false;

        queue.push_back(data);
        size++;

        return true;
    }

    public T element() throws QueueEmptyException {

        if(empty()) throw new QueueEmptyException();

        return queue.front();
    }

    public T peek() {
        return (empty()) ? null : queue.front();
    }

    public T poll() {

        if(empty()) return null;

        T returnVal = queue.front();

        queue.remove_front();

        size--;

        return returnVal;
    }


    public T remove() throws QueueEmptyException{

        if(empty()) throw new QueueEmptyException();

        T returnVal = queue.front();

        queue.remove_front();

        size--;

        return returnVal;
    }
}
