class文件结构

class是8位字节为一个基础单元的二进制流：两种数据类型，无符号数(u1,u2,u4,u8)和表(多个无符号数或者表组成的复合数据类型，_info结尾)

字节码指令：操作码(一个字节，最多256个指令)+操作数（参数）

class文件结构：

U4  魔数

U2  小版本

u2 大版本

U2 常量池的数量

常量池（cp_info）（数量等于U2的数量-1）:字面量(String字面量和final常量)和符号引用（类或者接口的全限定名、属性的名称和描述符、方法的名称和描述符）

Ljava.lang.Object代表引用类型,[代表数据类型,J是long,Z是boolean,V是void

都是u1大小的tag开头的，表明类型

u2 访问标示（public abstract final interface annotation enum）

u2 类索引

U2 父类索引

U2 接口数量

u2 接口索引集合

u2 属性数量

Field_info

u2 方法数量

Method_info

u2 属性集合

Attribute_info



操作码分类：

1 入栈和出栈指令：

​		出栈指令store(存入局部变量表)

​		入栈指令：

​				从局部变量表入栈：iload

​				常量入栈:iconst\sipush\bipush\ldc

2 运算指令: 

​		运算指令：iadd\isub\imul\idiv

3 类型转换指令：小转大，直接支持，大转小，舍N位，i2c,i2s,d2l

4 对象创建和访问指令：

​		对象创建：new newarray

​		访问类字段：getstatic putstatic

​		访问实例字段：getfield putfield

​		把数组中的元素储存到操作数栈中：iaload

​		把操作数栈的值存储到数组中：iastore

​		数组长度:arraylength

​		类型检查:instanceof checkcast

5 操作数栈指令：

​		元素出栈指令：pop\pop2

​		复制栈顶元素指令：dup\dup2\dup_x1\dpu2_x1

​		替换指令：swap

6 控制转移指令：

​		比较指令（double\float\long）：dcmpg\dcmpl\fcmpg\fcmpl\lcmp

​		条件跳转指令(都是与0或者null做比较)：ifeq\iflt\ifle\ifne\ifgt\ifge\ifnull\ifnonnull

​		比较条件条件指令(对整形或者引用类型有效)：if_icmpeq\if_icmpne\if_icmplt\if_icmple\if_icmpge\if_icmpgt   if_acmpeq\if_acmpne

​		复合条件分支：tableswitch\lookupswitch

​		无条件分支：goto

7 方法调用和返回

​		方法调用：invokertual\invokeinterface\invokespecial\invokestatic\invokedynamic

​		方法返回:  areturn

8 异常处理指令：

​		athrow

​		异常表（开始、结束、异常类型、跳转位置）

9 同步指令：

​		monitorenter

​		monitorexit(一个enter对应两个exit，一个是正常退出，一个是异常退出，会生成异常表，保证退出)



