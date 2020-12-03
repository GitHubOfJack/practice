package com.jack.jvm;

/**
 *
 * 写屏障
 * 内存屏障
 *
 * 主内存->read->load->工作内存->user->工作线程
 * 工作线程->assign->工作内存->store->write->主内存
 *
 * synchronized关键字底层原理：
 * 1 用户态、内核态
 * 2 cas
 *      cas会引起aba问题，解决方案:AtomicStampedReference
 *      cas怎么解决原子性问题：lock
 *
 *
 *
 */
public class JMMTest {

}
