package com.jack.arithmetic;

/**
 * @author 马钊
 * @date 2020-11-09 15:21
 *
 * 给定两个大小为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的中位数。
 *
 * 进阶：你能设计一个时间复杂度为 O(log (m+n)) 的算法解决此问题吗？
 *
 * 输入：nums1 = [1,3], nums2 = [2]
 * 输出：2.00000
 * 解释：合并数组 = [1,2,3] ，中位数 2
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/median-of-two-sorted-arrays
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class FindMedianSortedArrays {
    public static void main(String[] args) {
        int[] nums1 = new int[]{1, 3, 5, 7};
        int[] nums2 = new int[]{2};
        //int[] nums2 = new int[]{2, 4, 6, 8};
        findMedianSortedArrays2(nums1, nums2);
    }

    public static double findMedianSortedArrays1(int[] nums1, int[] nums2) {
        int l1 = nums1.length;
        int l2 = nums2.length;
        int total = l1 + l2;
        int[] newNums = new int[total];
        if (l1 > l2) {
            for (int i2 = 0; i2 < l2; i2++) {
                if (nums2[i2] < nums1[l1/2]) {

                }
            }
        }
        return 0;

    }

    public static void c(int num, int[] nums, int start, int end, int newNums) {

        int middle = (start + end) / 2;
        while (start - end > 1) {
            if (num > nums[middle]) {
                start = middle;
            } else if (num < nums[middle]){
                end = middle;
            }
        }

    }

    public static double findMedianSortedArrays2(int[] nums1, int[] nums2) {
        //如果是偶数
        if ((nums1.length + nums2.length) % 2 == 0) {
            return (get_loction(nums1, nums2, 0, 0, (nums1.length + nums2.length) / 2) + get_loction(nums1, nums2, 0, 0, (nums1.length + nums2.length) / 2 + 1)) / 2.0;
        } else {
            //如果是奇数
            return (double) get_loction(nums1, nums2, 0, 0, (nums1.length + nums2.length) / 2 + 1);
        }
    }

    public static int get_loction(int[] nums1, int[] nums2, int begin1, int begin2, int k) {
        if (begin1 == nums1.length) {
            return nums2[begin2 + k - 1];
        } else if (begin2 == nums2.length) {
            return nums1[begin1 + k - 1];
        } else if (k == 1) {
            return nums1[begin1] < nums2[begin2] ? nums1[begin1]: nums2[begin2];
        } else {
            int end1, end2;
            if (begin1 + k / 2 - 1 >= nums1.length) {
                end1 = nums1.length - 1;
            } else {
                end1 = begin1 + k / 2 - 1;
            }
            if (begin2 + k / 2 - 1 >= nums2.length) {
                end2 = nums2.length - 1;
            } else {
                end2 = begin2 + k / 2 - 1;
            }
            if (nums1[end1] <= nums2[end2]) {
                return get_loction(nums1, nums2, end1 + 1, begin2, k - end1 - 1 + begin1);
            } else {
                return  get_loction(nums1, nums2, begin1, end2 + 1, k - end2 - 1 + begin2);
            }
        }
    }
}
