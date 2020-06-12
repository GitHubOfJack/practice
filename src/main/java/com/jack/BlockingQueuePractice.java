package com.jack;

import java.util.concurrent.ArrayBlockingQueue;
/**
 * ArrayBlockingQueue
 * LinkedBlockingDeque
 *      Executors.newFixedThreadPool()和Executors.newSingleThreadExecutor()底层均使用此阻塞队列,会造成内存溢出的情况
 * SynchronousQueue
 *      Executors.newCachedThreadPool()使用此阻塞队列，会大量创建线程
 *
 *      add(E e)  remove() 会报错
 *      offer(E e)  poll()  返回
 *      put(E e)   take()   会阻塞
 */
class Share{
    //模拟饭店只有5张桌子
    ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(5);
    //每桌都订了鱼
    public void cookFish() {
        /*blockingQueue.add();
        blockingQueue.remove();
        blockingQueue.put();
        blockingQueue.take();
        blockingQueue.offer();
        blockingQueue.poll();
        blockingQueue.offer();
        blockingQueue.poll();*/
    }
}

public class BlockingQueuePractice {
    public static void main(String[] args) {

    }
}
