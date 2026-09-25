package 요격시스템;

import java.util.*;
class Solution {

    static int start = 100000000;
    static int end = 0;
    static int count = 0;
    public int solution(int[][] targets) {
        Map<Integer, int []> hash = new HashMap<>();




        int answer = 0;

        for (int[] i : targets) {
            int min_candidate = i[0];
            int max_candidate = i[1];
            start = Math.min(start, min_candidate);
            end   = Math.max(end, max_candidate);
            hash.put(count, i);
            count ++;
        }

        while(!hash.isEmpty()) {
            Map.Entry<Integer, int[]> entry = hash.entrySet().iterator().next();
            int key = entry.getKey();
            int [] target = entry.getValue();









        }


        return answer;
    }


    public static void main(String[] args) {

        Solution solution  = new Solution();
        int result = solution.solution(new int [][] {
                {4,5},{4,8},{10,14},{11,13},{5,12},{3,7},{1,4}
        });

        System.out.println(result);

    }



}


