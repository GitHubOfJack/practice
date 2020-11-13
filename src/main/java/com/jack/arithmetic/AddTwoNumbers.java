package com.jack.arithmetic;

import java.math.BigDecimal;

/**
 * @author 马钊
 * @date 2020-11-06 16:55
 *
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 *
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 *
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 *
 * 示例：
 *
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 */
public class AddTwoNumbers {

    public static void main(String[] args) {
        AddTwoNumbers addTwoNumbers = new AddTwoNumbers();
        ListNode l1 = addTwoNumbers.new ListNode(5, null);
        ListNode l2 = addTwoNumbers.new ListNode(6, l1);
        ListNode l3 = addTwoNumbers.new ListNode(4, l2);

        ListNode l4 = addTwoNumbers.new ListNode(1, null);
        ListNode l5 = addTwoNumbers.new ListNode(0, l4);
        ListNode l6 = addTwoNumbers.new ListNode(0, l5);
        ListNode l7 = addTwoNumbers.new ListNode(0, l6);
        ListNode l8 = addTwoNumbers.new ListNode(0, l7);
        ListNode l9 = addTwoNumbers.new ListNode(0, l8);
        ListNode l10 = addTwoNumbers.new ListNode(0, l9);
        ListNode l11 = addTwoNumbers.new ListNode(0, l10);
        ListNode l12 = addTwoNumbers.new ListNode(0, l11);
        ListNode l13 = addTwoNumbers.new ListNode(0, l12);
        ListNode l14 = addTwoNumbers.new ListNode(0, l13);
        ListNode l15 = addTwoNumbers.new ListNode(0, l14);
        ListNode l16 = addTwoNumbers.new ListNode(0, l15);
        ListNode l17 = addTwoNumbers.new ListNode(0, l16);
        ListNode l18 = addTwoNumbers.new ListNode(0, l17);
        ListNode l19 = addTwoNumbers.new ListNode(0, l18);
        ListNode l20 = addTwoNumbers.new ListNode(0, l19);
        ListNode l21 = addTwoNumbers.new ListNode(0, l20);
        ListNode l22 = addTwoNumbers.new ListNode(0, l21);
        ListNode l23 = addTwoNumbers.new ListNode(0, l22);
        ListNode l24 = addTwoNumbers.new ListNode(0, l23);
        ListNode l25 = addTwoNumbers.new ListNode(0, l24);
        ListNode l26 = addTwoNumbers.new ListNode(1, l25);

        ListNode listNode = addTwoNumbers.addTwoNumbers1(l3, l26);
        System.out.println(listNode.val);
        ListNode next = listNode.next;
        while (next != null) {
            System.out.println(next.val);
            next = next.next;
        }
    }

    /**
     * 方法一：遍历出所有位置，对应位置*10^i,然后相加
     * 比如
     * 3->4->5 变成3*10^0+4*10^1+5*10^2
     * 需要考虑long最大值的问题，所以使用Bigdecimal
     * */
    public ListNode addTwoNumbers1(ListNode l1, ListNode l2) {
        BigDecimal a = BigDecimal.valueOf(l1.val), b=BigDecimal.valueOf(l2.val);
        ListNode l1Next = l1.next;
        ListNode l2Next = l2.next;
        int i = 1;
        while (null != l1Next || null != l2Next) {
            if (null != l1Next) {
                a = a.add(BigDecimal.valueOf(l1Next.val).multiply(BigDecimal.TEN.pow(i)));
                l1Next = l1Next.next;
            }
            if (null != l2Next) {
                b = b.add(BigDecimal.valueOf(l2Next.val).multiply(BigDecimal.TEN.pow(i)));
                l2Next = l2Next.next;
            }
            i++;
        }
        BigDecimal c = a.add(b);
        System.out.println(c);
        int d;
        ListNode next = null;
        for (int j=i; j>=0; j--) {
            d = c.divideToIntegralValue(BigDecimal.TEN.pow(j)).intValue();
            if (j==i && d == 0) {
                continue;
            }
            c = c.subtract(BigDecimal.valueOf(d).multiply(BigDecimal.TEN.pow(j)));
            next = new ListNode(d, next);
        }
        return next;

    }

    /**
     * 思路：
     * 1 遍历一次：相同位置相加，相加结果如果大于0，进一，用于下一组相加，之后取余，余数是当前NODE的值
     * 2 有两个指针，head,cur指针
     *   刚开始，head=cur=默认节点，没循环一次，cur.next=新建节点，cur=cur.nex,依次循环，如果最后一次，位置相加等于1，标示进一位，cur.next=新建节点
     *   最终返回head.next就是所需要的节点
     * */
    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode headNode = new ListNode(-1);
        ListNode curNode = headNode;
        int n1;
        int n2;
        int carry = 0;
        int sum = 0;
        int nodeValue = 0;
        while (l1 != null || l2 != null) {
            n1 = null == l1 ? 0 : l1.val;
            n2 = null == l2 ? 0 : l2.val;
            sum = n1 + n2 + carry;
            carry = sum / 10;
            nodeValue = sum % 10;
            curNode.next = new ListNode(nodeValue);
            curNode = curNode.next;
            if (null != l1) {
                l1 = l1.next;
            }
            if (null != l2) {
                l2 = l2.next;
            }
        }
        if (1 == carry) {
            curNode.next = new ListNode(1);
        }
        return headNode.next;
    }

    class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
