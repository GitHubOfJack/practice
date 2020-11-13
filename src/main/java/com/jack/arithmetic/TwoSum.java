package com.jack.arithmetic;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 马钊
 * @date 2020-11-06 16:00
 */
public class TwoSum {
    public static void main(String[] args) {
        int[] nums = new int[]{2,7,11,15};
        int[] ints = twoSum(nums, 9);
        System.out.println(ints);
    }

    /**
     * 方法一：
     * 两层循环，两层数值相加=目标值
     * */
    public static int[] twoSum(int[] nums, int target) {
        return method1(nums, target);
    }

    public static int[] method1(int[] nums, int target) {
        if(null == nums) {
            return null;
        } else if (1 >= nums.length) {
            return null;
        } else if (2 == nums.length) {
            if (nums[0] + nums[1] == target) {
                return new int[]{0, 1};
            } else {
                return null;
            }
        } else {
            for (int i=0; i<nums.length;  i++) {
                int first = nums[i];
                for (int j=i+1; j<nums.length; j++) {
                    if ((first + nums[j]) == target) {
                        return new int[]{i, j};
                    } else {
                        continue;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 方法二：
     * 一次循环
     * 用一个Map来保存对应关系，key=target-nums[i],value=i
     * */
    public static int[] method2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i=0; i<nums.length; i++) {
            if (null != map.get(nums[i])) {
                return new int[]{map.get(nums[i]), i};
            } else {
                map.put(target-nums[i], i);
            }
        }
        return null;
    }
}
