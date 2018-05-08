package SortingAlgorithm;

import org.apache.commons.lang3.ArrayUtils;

class Sort {

    private static void swap(int idx1, int idx2, String[] input) {
        // 같은 인덱스면 return
        if(idx1 == idx2) return;
        // Swap
        String tmp = input[idx1];
        input[idx1] = input[idx2];
        input[idx2] = tmp;
    }

    private static void swap(int idx1, int idx2, int[] input) {
        // 같은 인덱스면 return
        if(idx1 == idx2) return;
        // Swap
        int tmp = input[idx1];
        input[idx1] = input[idx2];
        input[idx2] = tmp;
    }

    private static void reverse(int[] input) {

        int left = 0, right = input.length - 1;

        while(left < right)
            swap(left++, right--, input);
    }

    private static void reverse(String[] input) {

        int left = 0, right = input.length - 1;

        while(left < right)
            swap(left++, right--, input);
    }

    /*
    * Method      : bubbleSort
    * Parameter   : input : int[]
    * Description : Sort input array by using Bubble Sort
    * Note        : To decrease time, use isSorted : boolean Variable. after inner for statement, check something changed. if nothing changed, break for statement.
    */
    public final static int[] bubbleSort(int input[]) {

        int length = input.length - 1;

        for(int i = 0; i < length; ++i) {
            // 한 번이라도 바뀌지 않으면 정렬이 완료되었다고 판단.
            boolean isSorted = true;

            for(int j = 0; j < length; ++j) {
                if(input[j] < input[j+1]) {
                    isSorted = false;
                    swap(j, j+1, input);
                }
            }

            if(isSorted) break;
        }
        return input;
    }


    /*
     * Method      : selectionSort
     * Parameter   : input : int[]
     * Description : Sort input array by using Selection Sort
     */

    public final static int[] selectionSort(int input[]) {
        int length = input.length;

        for(int i = 0; i < length - 1; ++i) {

            int maxIndex = i;

            for(int j = i + 1; j < length; ++j)
                if (input[maxIndex] <= input[j])
                    maxIndex = j;

            swap(maxIndex, i, input);
        }

        return input;
    }


    /*
     * Method      : insertionSort
     * Parameter   : input : int[]
     * Description : Sort input array by using insertion Sort
     */

    public final static int[] insertionSort(int input[]) {

        int length = input.length;

        for(int i = 0; i < length; ++i) {

            int loc = i - 1;
            int val = input[i];

            for(; 0 <= loc && val < input[loc]; loc--)
                input[loc + 1] = input[loc];

            input[loc + 1] = val;
        }
        return input;
    }



    /*
     * Method      : mergeSort
     * Parameter   : input : int[], asc : boolean
     * Description : 외부에서 접근하기위해 만들어 놓은 병합정렬 메소드. asc = true(오름차순) / false(내림차순)
     */
    public final static void mergeSort(int input[], boolean asc) {

        int left = 0, right = input.length - 1;

        mergeSort(input, left, right);

        // 오름차순으로 정렬되기때문에 asc == False 인 경우만 역순으로 배치한다.
        if(!asc)
            reverse(input);
            //ArrayUtils.reverse(input);
    }

    /*
     * Method      : mergeSort
     * Parameter   : input : int[], left : int, right : int, asc : boolean
     * Description : 입력을 반으로 나눌 수 없을 때 까지 나눈 후 merge 함수를 호출하여 부분 정렬. asc 로 정렬 순서 선택
     * TimeSpace   :
     */
    private final static void mergeSort(int[] input, int left, int right) {

        // 원소가 적어도 두개 이상인 경우 Divide
        if(left < right) {

            // 중심 계산
            int pivot = (left + right)/2;

            // 왼쪽 나누기
            mergeSort(input, left, pivot);

            // 오른쪽 나누기
            mergeSort(input, pivot + 1, right);

            // 왼쪽, 오른쪽 다시 합치기. Conquer
            merge(input, left, pivot, right);
        }
    }

    /*
     * Method      : merge
     * Parameter   : input : int[], left : int, pivot : int, right : int, asc : boolean
     * Description : 두개로 나뉜 입력을 크기 순서대로 재배열한다. asc 로 정렬 순서 선택
     */
    private final static void merge(int[] input, int left, int pivot, int right) {

        int l = left, p = pivot + 1, r = right, count = 0;

        // 필요한 크기만큼의 배열 생성
        int[] tmp = new int[right - left + 1];

        // 왼쪽, 오른쪽 값 비교하면서 하나씩 넣기
        while(l <= pivot && p <= right)
            tmp[count++] = (input[l] < input[p] ? input[l++] : input[p++]);

        // 나머지 차례대로 넣기
        for(; l <= pivot; ++l, ++count)
            tmp[count] = input[l];
        for(; p <= right; ++p, ++count)
            tmp[count] = input[p];

        // 복사
        for(int i = left, k = 0; i <= right; ++i, ++k)
            input[i] = tmp[k];
    }

    /*
     * Method      : quickSort
     * Parameter   : input : int[], asc : boolean
     * Description : 외부에서 접근하기 위해 만들어 놓은 quickSort 함수. asc = true/false (오름차순/내림차순)
     */
    public final static void quickSort(int[] input, boolean asc) {

        int left = 0, pivot = input.length - 1;

        quickSort(input, left, pivot);

        if(!asc)
            reverse(input);
            //ArrayUtils.reverse(input);
    }

    /*
     * Method      : quickSort
     * Parameter   : input : int[], left : int, pivot : int, asc : boolean
     * Description : 외부에서 접근하기 위해 만들어 놓은 quickSort 함수. asc = true/false (오름차순/내림차순)
     */
    public final static void quickSort(int[] input, int left, int pivot) {

        // 원소가 2개 이상인 경우만 Divide
        if(left < pivot) {
            // 다음 기준점 확인.
            int nextPivot = partition(input, left, pivot);
            // 기준점 왼편
            quickSort(input, left, nextPivot - 1);
            // 기준점 오른편
            quickSort(input, nextPivot + 1, pivot);
        }
    }

    /*
     * Method      : partition
     * Parameter   : input : int[], asc : boolean
     * Description : 외부에서 접근하기 위해 만들어 놓은 quickSort 함수. asc = true/false (오름차순/내림차순)
     */
    public final static int partition(int[] input, int left, int pivot) {
        // i : pivot 이 들어갈 위치 계산
        int i = left - 1, j = left;

        for(; j < pivot; ++ j) {

            if(input[j] < input[pivot])
                swap(++i,j,input);
        }
        // pivot 을 적절한 위치로 보내기.
        swap(++i, pivot, input);
        // input 을 나누는 기준점 반환
        return i;
    }

    /*
     * Method      : heapSort
     * Parameter   : input : int[], asc : boolean
     * Description : 외부에서 접근하기 위해 만들어 놓은 heapSort 함수. asc = true/false (오름차순/내림차순)
     */
    public final static void heapSort(String[] input, boolean asc) {

        // input 을 Heap 에 맞게 변경
        buildHeap(input);
        // Heap 이 된 input 을 정렬
        heapSort(input);

        // 내림차순으로 정렬되기 때문에 asc == True 인 경우만 역순으로 배치
        if(asc)
            reverse(input);
            //ArrayUtils.reverse(input);
    }

    /*
     * Method      : heapSort
     * Parameter   : input : int[]
     * Description : heap의 형태를 만든 후, 루트노드를 반복적으로 POP 수행 -> 정렬된 배열 생성.
     */
    private final static void heapSort(String[] input) {

        int numOfNodes = input.length - 1;
        // POP 한 노드를 맨 마지막 노드의 위치로 보내기 => 반복문 실행 후 내림차순으로 input이 정렬됨.
        for(int i = numOfNodes, j = 0; i >= 0; --i, ++j) {
            swap(0, i, input);
            heapify(input,0, input.length - j - 1);
        }
    }

    /*
     * Method      : buildHeap
     * Parameter   : input : int[]
     * Description : 자식을 가질 수 있는 Level 부터 각각의 서브트리를 Heap 형태로 변경
     */
    private final static void buildHeap(String[] input) {

        // 배열이 0부터 시작하기에 - 1 을 한다.
        // 리프노드 Level 을 제외한 줄부터 Heap 에 맞게 변경
        int parNodeIdx = (input.length - 1) / 2;

        // Heap 에 맞게 변경
        for(int i = parNodeIdx; i >= 0; --i)
            heapify(input, i, input.length);
    }

    /*
     * Method      : heapify
     * Parameter   : input : int[], parNode : int, length : int
     * Description : 부모노드(parNode)를 루트노드로 하는 서브트리를 Heap 에 맞게 변경
     */
    private final static void heapify(String[] input, int parNode, int length) {

        int smaller, leftChild = parNode * 2 + 1, rightChild = parNode * 2 + 2;

        // 오른쪽 자식이 있는 경우, 작은 쪽의 자식 선택
        if(rightChild < length) {
            // 문자열 길이가 더 짧은 노드를 선택
            if(input[leftChild].length() < input[rightChild].length())
                smaller = leftChild;
            else if(input[leftChild].length() > input[rightChild].length())
                smaller = rightChild;
            // 두 문자열의 길이가 동일하다면 사전순서가 빠른 것을 선택(A.compareTo(B) -> A < B : -, A == B : 0, A < B : 0 +)
            else
                smaller = (input[leftChild].compareTo(input[rightChild]) < 0 ? leftChild : rightChild);
        }
        // 왼쪽 자식만 있는 경우, 왼쪽 자식 선택
        else if(leftChild < length)
            smaller = leftChild;
        // 자식이 없으면 Pass
        else
            return;

        // 부모, 자식 문자열의 길이 비교 후 자식의 길이가 짧으면 부모-자식 변경. 부모를 루트로 다시 힙하게 변경
        if(input[smaller].length() < input[parNode].length()) {
            swap(smaller, parNode, input);
            heapify(input, smaller, length);
        }
        // 부모, 자식의 길이가 같다면 사전순으로 변경
        else if(input[smaller].length() == input[parNode].length() && input[smaller].compareTo(input[parNode]) < 0) {
            swap(smaller, parNode, input);
            heapify(input, smaller, length);
        }
    }


    /*
     * Method      : sortedIndexOf
     * Parameter   : input : int[], idx : int
     * Description : idx번째로 작은 값 반환
     */
    public final static int sortedIndexOf(int[] input, int idx) throws Exception{

        if(input.length <= idx)
            throw new Exception();

        int val = select(input, 0, input.length - 1, idx);

        return val;
    }

	/*
     * Method      : select
     * Parameter   : input : int[], left : int, right : int, idx : int
     * Description : Pivot은 정렬된 위치이다. Pivot을 기준으로 IDX의 위치를 확인하고 Pivot 보다 작으면/크면 왼쪽/오른쪽으로 이동한다. 반복하여 수행.
	 *               만약 Pivot 과 찾고자하는 idx가 같으면 함수 종료. 
     */
    private final static int select(int[] input, int left, int right, int idx) {

        // 원소가 하나가 남았으면 해당 원소 리턴
        if(left == right) return input[left];

        // 찾고자하는 인덱스가 Pivot 을 기준으로 어디 위치해있는지 확인
        int pivot = partition(input, left, right);

        // 왼쪽에 위치한 경우
        if(idx < pivot) return select(input, left, pivot - 1, idx);
        // Pivot은 정렬된 위치를 가진다 => Pivot과 같으면 바로 pivot 자리 리턴
        else if(pivot == idx) return input[pivot];
        // 오른쪽에 위치한 경우
        else return select(input, pivot + 1, right, idx);

        /*
        if(left <= right) {

            int pivot = partition(input,left, right);

            if(idx < pivot) return select(input, left, pivot - 1, idx);
            else if(idx == pivot) return input[pivot];
            else return select(input, pivot + 1, right, idx);
        }*/
    }
}

