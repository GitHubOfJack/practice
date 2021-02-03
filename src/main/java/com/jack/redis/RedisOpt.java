package com.jack.redis;

/**
 * 分布式：多台服务器上有多个模块
 * 集群：多台服务器上有一个模块
 *
 *
 * 分布式事务：
 *  2阶段提交
 *  3阶段提交
 *  tcc
 *
 * 分布式架构要在cap中作出取舍
 *
 * 1 关系型数据库可以满足acid的事务要求，一般都是满足cap理论中的ca
 *
 * 2 非关系型数据库
 *      1 文档型数据库
 *          mongdb：可以存放xml，json，bson等数据结构，即有分层的树状结构就可以
 *          存储方式：内存+持久化
 *          适用场景：日志、商品评论等（保存数据不是特别重要，例如通知，推送等，数据结构变化较大，同时并发要求较高的场景），可以支持复杂查询
 *          缺点：空间占用大
 *      2 kv数据库：
 *          redis：
 *              可以存储string,set,zset,list,hash等5中数据结构
 *              存储方式：内存+持久化（可配置持久化方式aof或者快照）
 *              适用场景：缓存
 *          memCache
 *              存储string类型数据
 *              存储方式:内存
 *      3 列存储数据库:
 *          hbase
 *      4 图存储
 *          neo4j
 *
 *      redis满足cap理论中的cp理论(zookeeper也是cp,eureka是ap)
 *
 *      现代的互联网公司的项目基本上是ap+base来保证数据一致性的
 *
 *      一致性分为：
 *          强一致：a把数据从0变成了1，则b看到的数据必须是1
 *          弱一致：a把数据从0变成了1，b看到0或者1都可以
 *          最终一致：a把数据从0变成了1，b在一段时间内看到的是0，但是过了这段时间就可以看到1
 *
 *      base-Basically Available（基本可用）、Soft state（软状态）和Eventually consistent（最终一致性）三个短语的简写
 *      满足base理论的事务，我们成为柔性事务
 *
 *      basically available：损失部分可用性，但是整体还是可用的
 *          时间上的损失：比如一个系统正常返回是0.3s，但是除了问题会在1-2s进行返回，
 *          功能上的损失：双11当天或者秒杀，点击功能显示被挤爆了，稍后再试
 *      soft state：允许数据存在中间状态
 *      eventually consistent:经过一段时候之后达到最终的数据一致性
 *
 *     1 redis的5中数据类型的常用操作
 *
 *     redis-cli -h host -p port -a password
 *
 *          key相关的操作:
 *              exists key [key...]   例如：exists k1 k2(可罗列多个)
 *              keys pattern        例如:keys *    keys k*
 *              del key [key...]    例如:del k1 k2
 *              expire key seconds  例如： expire k1 10 设置k1的过期时间是10秒
 *              expireat key timestamp  例如：expireat k1 1612259478 设置k1的过期时间是1612259478(unix时间戳，单位秒)
 *              pexpire key milliseconds    例如:pexpire k1 100 设置k1的过期时间是100毫秒
 *              pexpireat key milliseconds-timestamp    例如：pexpireat k1 1612259478000 设置k1的过期时间是1612259478000(unix时间戳，单位毫秒)
 *              persist key  例如：persist k1 设置k1为永久有效
 *              ttl key 例如：ttl k1       key剩余有效时间，返回integer(s),-1表示永久有效，-2表示key不存在
 *              pttl key 例如：ttl k1      key剩余有效时间，返回integer(ms),-1表示永久有效，-2表示key不存在
 *              rename key newkey       key重命名为newkey
 *              renamenx key newkey     key重命名为newkey，仅当newkey不存在时，才命名为newkey，1-设置成功，0-设置失败
 *              type key                返回key所存储值的类型
 *
 *          字符串相关操作
 *
 *     2 redis数据持久化的方式
 *     3 redis集群的方式（主-从模式、哨兵模式、集群模式？？？）
 *     4 redis集群的通信方式
 *     5 缓存雪崩、缓存击穿、缓存穿透以及相应的解决方案
 */
public class RedisOpt {
}
