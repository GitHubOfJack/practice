1 jvm分为四个部分：运行时数据区，类加载系统和字节码文件，垃圾回收，jvm调优



2 运行时数据区：pc寄存器、虚拟机栈、本地方法栈、堆、方法区

2.1 pc寄存器（线程私有、不会oom，不会gc）存储的是下一个要执行的指令的位置

2.2虚拟机栈（线程私有、会oom，不会gc）

​	局部变量表：底层是数组结构，用来存储局部变量值（基本变量、对象引用等），在编译期间就已经确定下来大小。非静态方法存储的第一个位置是this变量。

​	操作数栈：底层是数据结构，但是当成栈来使用，只有入栈和出栈两种操作。存储的也是需要操作的变量值（基本变量、对象引用），在编译期间就已经确定下来大小。

​	动态链接：指向运行时常量池中方法的位置（表明当前方法是那个对象的什么方法）

​		符号引用：常量池中的字符串的字面量，java/lang/Object

​		直接引用：这个Object对象在内存中的位置0X00000001

​		非虚方法（虚方法）：编译期间就确定下来的方法就是非虚方法，如静态方法、final方法、private方法、构造器方法、父类方法

​		invokeStatic:执行静态方法

​		invokeSpecial：执行非虚方法（final方法除外）

​		invokeVirtual:执行虚方法(包含final方法)

​		invokeInterface:执行接口方法

​		虚方法表：存在方法区中（不用再循环父类找虚方法）

​	方法返回地址：保存的是方法调用的下一个指令地址，为了使方法正常退出时，可以知道返回到那个位置。

​		正常返回：需要恢复上层方法的局部变量表，操作数栈，吧返回值（如果有的话）压入调用者的操作数栈中。

​		异常返回：通过异常表来确定如何处理

2.3 本地方法栈（线程私有、会oom，不会gc）

2.4 堆（线程共享、会oom，会gc）

![](/Users/a0003/资料/分享/图片/对象分配流程.png)

堆分为：新生代和老年代，新生代包含1个Eden和2个Survivor

Minor GC:eden区满了之后&&满足空间担保，S0或者S1满并不会触发，Minor GC之后会清空Eden区和一个Survivor

Full GC:老年代满了、方法区满了、Eden区满了之后，不满足空间担保。

逃逸分析技术和标量替换：有了逃逸分析技术才有了栈上分配、锁消除（锁粗化）、标量替换。hotspot并没有支持逃逸分析技术。

2.5 方法区（线程共享、会oom，会gc）

方法区：类信息（域信息、方法信息）、常量、静态变量、jit及时编译的代码

类信息：类的全名称、修饰符、父类、接口列表

域信息：域名称、修饰符、类型

方法信息：方法名、返回类型、参数列表、修饰符、字节码、异常列表(init\clinit)

常量：数值类型的常量池（字符串类型的常量池1.6位于方法区，1.7迁移到堆中）

静态变量：也位于方法区（final修饰的static变量（非引用类型）是常量，在编译期间就直接赋值）

2.5 字符串常量池：



3 Object obj = new Object();底层的字节码指令

new #11 <java/lang/Object> (在内存中给对象分配空间，并把对象的地址入栈)

dup （把对象的地址copy一份入栈）

invokeSpecial #12 <java/lang/Object.<init>> （执行object的构造方法）

astore_1 （把操作数栈中的数据存储到局部变量表的第二个位置）

return （方法返回）





Cms  gc调优

https://tech.meituan.com/2019/12/05/aqs-theory-and-apply.html



java分析问题命令行工具

https://www.cnblogs.com/duanxz/p/4515437.html







jvm常用指令

arthas(阿里巴巴开源软件，可用于生产)

jps

jstat

jstack

jmap

javap

jinfo

jconsole (图形工具-不能用于生产)

jvisualvm(图形工具-不能用于生产)



-verbose:gc

-verbose:class

-Xms

-Xmx

-Xmn

-Xss

-XX:NewRatio

-XX:SurvivorRatio

-XX:PrintGCDetails

-XX:PrintFlagsInitial

-XX:PrintFlagsFinal

-XX:useTLAB

-XX:useG1(别的垃圾收集器)

-XX:MaxTenuringThreshold

-XX:PermSize

-XX:MaxPermSize

-XX:MetaspaceSize

-XX:MaxMetaspaceSize





问题一：FASTJSON使用不当导致内存溢出问题

场景：接口对外输出数据时候，输出JSON格式数据，并且要求时间格式是YYYY-MM-DD，但是默认时间的格式返回的是毫秒数

错误写法：

SerializeConfig config = new SerializeConfig();

config.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

String res  = JSON.toJSONString(srcRes, config);

导致的问题:jvm报错OOM:metaspce

分析思路：元空间溢出，基本可以锁定是加载了过多的类，但是项目已经运行了一段时间，应该是某个功能动态生成了很多类，所以在启动命令里面加了-verbose:class，程序启动之后在catalina.out文件中发现生成了大量的com.alibaba.fastjson.serializer.ASMSerializer,该类是利用asm技术生成的，每次new就生成一个，最终导致内存溢出。解决办法：单例一个对象