
/*
*
*  Binary Search Tree 만들어보기
*  삽입, 삭제, 검색 구현
*  
 */

 
import java.io.*;
import java.util.*;

 
class BST {

    /*
    *   TreeNode Class
    *   Member로 왼쪽, 오른쪽 자식 객체, 키값을 가지고 있다.
    */
    private class TreeNode implements Comparable<TreeNode> {

        public int val;
        public TreeNode left, right;

        public TreeNode(int val) {
            this.val = val;
            left = null;
            right = null;
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(TreeNode o) {
            return val - o.val;
        }
    }

    // 루트 노드
    private TreeNode rootNode;

    // 생성자
    public BST() { rootNode = null; }
    public BST(int val) { rootNode = new TreeNode(val);}

    // 빈트리면 true, 아니면 false 바환
    public boolean isEmpty() { return rootNode == null;}

    // 빈트리면 루트 생성. 아니면 재귀적으로 들어갈 위치를 찾은다음 삽입.
    public void insert(int val) {
        rootNode = isEmpty() ? new TreeNode(val) : insert(val, rootNode);
    }

    // 재귀적으로 들어갈 자리를 찾은 다음 삽입.
    private TreeNode insert(int val, TreeNode curNode) {

        if(curNode == null)
            return new TreeNode(val);

        if(curNode.val == val) return curNode;

        else if(curNode.val < val)
            curNode.right = insert(val, curNode.right);
        else if(curNode.val > val)
            curNode.left = insert(val, curNode.left);

        return curNode;
    }

    // 찾고자하는 값 검색. 존재하면 true, 없으면 false 반환
    public boolean search(int val) {
        return search(val, rootNode);
    }

    // 재귀적으로 탐색 수행.
    private boolean search(int val, TreeNode curNode) {

        if(curNode.val == val) return true;
        else if(curNode == null) return false;
        else return (curNode.val < val) ? search(val, curNode.right) : search(val, curNode.left);
    }

    // 삭제는 4가지 경우를 고려하여 작성
    // 1. 자식이 없는 리프 노드
    // 2. 오른쪽 자식만 있는 경우
    // 3. 왼쪽 자식만 있는 경우
    // 4. 자식이 둘이 있는 경우는
    public boolean delete(int val) {

        TreeNode targetNode = rootNode;
        TreeNode parNode = targetNode;
        // 부모의 왼쪽 자식이면 true, 아니면 false
        boolean isLeft = false;

        // 값이 트리에 존재하는지 확인
        while(targetNode.val != val) {
            parNode = targetNode;

            // 오른쪽으로
            if(targetNode.val < val) {
                targetNode = targetNode.right;
                isLeft = false;
            }

            // 오른쪽으로
            else {
                targetNode = targetNode.left;
                isLeft = true;
            }
        }

        // 값이 트리에 없다면 삭제 실패
        if(targetNode == null) return false;

        // 자식이 없는 노드를 삭제
        if(targetNode.left == null && targetNode.right == null) {

            // 삭제할 노드가가
           if(isLeft) parNode.left = null;
            else parNode.right = null;
            // 삭제할 노드는
            targetNode = null;
        }
        // 오른쪽 자식 하나있는 노드를 삭제
        else if(targetNode.left == null && targetNode.right != null) {

            if(isLeft) parNode.left = targetNode.right;
            else parNode.right= targetNode.right;
            targetNode = null;
        }
        // 왼쪽 자식 하나있는 노드를 삭제
        else if(targetNode.left != null && targetNode.right == null) {

            if(isLeft) parNode.left = targetNode.left;
            else parNode.right = targetNode.left;
            targetNode = null;
        }
        // 자식 둘있는 노드를 삭제
        else {

            // 오른쪽 서브트리의 가장 작은 노드의 부모노드
            TreeNode minNode = getMinNode(targetNode.right);
            // 오른쪽 서브트리의 가장 작은 노드
            TreeNode tmp = minNode.left;

            // 바로 오른쪽 자식이 가장 작은 오른쪽 원소인 경우
            if(tmp == null) {
                // 삭제될 노드의 왼쪽 자식과 교체될 노드의 왼쪽에 달기
                minNode.left = targetNode.left;
                // 부모의 왼쪽 자식인 경우
                if(isLeft) parNode.left = minNode;
                // 부모의 오른쪽 자식인 경우
                else parNode.right = minNode;
                // 삭제할 노드 처리
                targetNode = null;
            }

            else {
                // 가장 작은 노드가 오른쪽 자식을 가지는 경우 부모의 왼쪽에 달아주기
                if(tmp.right != null)
                    minNode.left = tmp.right;
                // 자식이 없으면 왼쪽 노드 연결 끊기
                else
                    minNode.left =  null;
                // 값 변경
                targetNode.val = tmp.val;
                // 가장 작은 노드 삭제
                tmp = null;
            }
        }
        // 삭제 성공
        return true;
    }

    // 오른쪽 서브트리에서 가장 작은 노드의 부모 노드 리턴
    private TreeNode getMinNode(TreeNode curNode) {

        TreeNode minNode = curNode;

        for(; minNode.left != null; curNode = minNode, minNode = minNode.left);

        return curNode;
    }

    // 빈 트리면 에러 문장 출력
    public void printTree() {
        if(rootNode == null) {
            System.out.println("Nothing");
            return;
        }
        printTree(rootNode);
    }

    // 중위순회로 돌리기
    private void printTree(TreeNode curNode) {
        if(curNode.right != null) printTree(curNode.right);
        System.out.println(curNode.val);
        if(curNode.left != null) printTree(curNode.left);
    }
}

public class Main {

    final static int MAX_SIZE = 11;

    public static void main(String args[]) throws Exception{

        FastScanner fs = new FastScanner();
        Random rd = new Random();
        int[] input = {73,27,91,5,37,77,4,10,62,75,30};

        BST t = new BST();

        for(int i = 0; i < MAX_SIZE; ++i)
            t.insert(input[i]);

        System.out.println("--------------------------");
        t.delete(75);
        t.printTree();

        System.out.println("--------------------------");
        t.delete(27);
        t.printTree();

        System.out.println("--------------------------");
        t.delete(5);
        t.printTree();

        System.out.println("--------------------------");
        t.delete(73);
        t.printTree();
    }
}


class FastScanner {

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private StringTokenizer st;

    public int nextInt() throws Exception {
        if(st == null || !st.hasMoreTokens()) st = new StringTokenizer(br.readLine());
        return Integer.parseInt(st.nextToken());
    }

    public long nextLong() throws Exception {
        if(st == null || !st.hasMoreTokens()) st = new StringTokenizer(br.readLine());
        return Long.parseLong(st.nextToken());
    }

    public String nextLine() throws Exception {
        if(st == null || !st.hasMoreTokens()) st = new StringTokenizer(br.readLine());
        return st.nextToken();
    }
}

