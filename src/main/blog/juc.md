# JUC

1 java.util.concurrent

​	Callable

​	ArrayBlockingQueue

​	ConcurrentHashMap

​	CopyOnWriteArrayList

​	CountDownLatch

​	CyclicBarrier

​	Exchanger

​	Executors

​	FutureTask

​	LinkedBlockingQueue

​	Semaphore

​	ThreadPoolExecutor

​	

2 java.util.concurrent.locks

​	AbstractQueuedSynchronizer

​	Condition

​	Lock

​	LockSupport

​	ReentrantLock

​	ReadWriteLock

​	ReentrantReadWriteLock

​	

3 java.util.concurrent.atomic

​	AtomicInteger

​	AtomicReference



4 synchronized关键字

​	1 用户态和内核态

​	2 cas

​	3 对象的内存布局

​	4 锁升级

5 volatile关键字

6 aqs

​	基于chl队列的state共享变量的锁模式，通过模版方法模式实现

​	AQS实现的排它锁：ReentrantLock

​	AQS实现的共享锁: ReadWriteLock\Semaphore\CountDownLatch\CyclicBarrier





讲解aqs

https://tech.meituan.com/2019/12/05/aqs-theory-and-apply.html

