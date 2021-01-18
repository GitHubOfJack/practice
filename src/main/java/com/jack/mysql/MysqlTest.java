package com.jack.mysql;

/**
 * SQL命令
 *  1 mysql -h 127.0.0.1 -P 3306 -u root -p(链接数据库)
 *  2 show databases(查看所有数据库)
 *  3 use test(切换至test库)
 *  4 show tables(查看所有表)
 *  5 desc a(查看a表的字段)
 *  6 show index from a(查看a表中的索引)
 *  7 truncate table a(清空表数据)
 *  8 drop table a(删除表)
 *  9 create table a(id int not null auto_increment, name varchar(10), primary key (id))(创建表)
 *  10 ALTER TABLE a ADD INDEX idx_name(name(5))（创建索引）
 *      CREATE INDEX idx_name ON a(name(5))
 *  11 alter table a drop index idx_name（删除索引）
 *      drop index inx_name on a
 *  12 show variables like '%buffer%' (查看变量)
 *  13 show profiles(没有开启,返回空)
 *  +----------+------------+-----------------+
 * | Query_ID | Duration   | Query           |
 * +----------+------------+-----------------+
 * |        1 | 0.00102925 | select * from a |
 * +----------+------------+-----------------+
 *  14 set profiling =1(开启) set profiling =0(关闭)
 *  15 show profile [all|cpu|默认是duration] for query 1（查看13中返回的查询列表中的操作耗时详情）
 *  +----------------------+----------+
 * | Status               | Duration |
 * +----------------------+----------+
 * | starting             | 0.000036 |
 * | checking permissions | 0.000011 |
 * | Opening tables       | 0.000783 |
 * | init                 | 0.000014 |
 * | System lock          | 0.000012 |
 * | optimizing           | 0.000004 |
 * | statistics           | 0.000011 |
 * | preparing            | 0.000009 |
 * | executing            | 0.000002 |
 * | Sending data         | 0.000021 |
 * | end                  | 0.000002 |
 * | query end            | 0.000004 |
 * | closing tables       | 0.000005 |
 * | freeing items        | 0.000087 |
 * | cleaning up          | 0.000030 |
 * +----------------------+----------+
 * 16 show processlist(查看该数据库有多少个连接，以及连接情况)
 * +--------+------+-----------------+------+---------+------+-------+------------------+
 * | Id     | User | Host            | db   | Command | Time | State | Info             |
 * +--------+------+-----------------+------+---------+------+-------+------------------+
 * | 303612 | root | localhost:63631 | NULL | Sleep   | 3592 |       | NULL             |
 * | 303623 | root | localhost:63659 | test | Query   |    0 | init  | show processlist |
 * | 304005 | root | localhost:64953 | test | Sleep   | 2812 |       | NULL             |
 * | 305822 | root | localhost:54144 | NULL | Sleep   |    8 |       | NULL             |
 * | 305823 | root | localhost:54147 | test | Sleep   |    5 |       | NULL             |
 * +--------+------+-----------------+------+---------+------+-------+------------------+
 * 17 select version();查看MYSQL版本
 * 18 help show;查看show的所有情况
 *
 * 19 begin(后面不需要跟transaction)/start transaction
 * 20 set session autocommit=on/off
 * 21 commit/rollback
 *
 *
 *
 * mysql从以下几点梳理
 *  1 存储引擎（存储方式）
 *  2 事务（acid）-a(redo\lock\undo) binlog
 *  3 索引
 *  4 优化
 *  5 explain
 *      id select_type table type possible_keys key ke_len ref rows extra
 *      id
 *          表示执行的顺序，规则如下：
 *          1 id相同：执行顺序从上到下
 *          2 id不同：值越大，越先执行
 *      select_type :
 *          simple:简单的查询、查询中不包含自查询或者union
 *          primary: 子查询中，最外层的查询
 *          subquery:在select或where列表中包含了子查询
 *          derived:在from列表中包含的子查询被标记为derived（衍生），MySQL会递归执行这些子查询，把结果放到临时表中
 *          union:如果第二个select出现在UNION之后，则被标记为UNION，如果union包含在from子句的子查询中，外层select被标记为derived
 *          union result：UNION 的结果
 *      type:
 *          system:表只有一条记录
 *          const：只有一个匹配行，用于主键或者唯一索引
 *          eq_ref：多表连接中使用primary key或者 unique key作为关联条件
 *          ref:非唯一索引扫描，返回匹配某个单独值的所有行（查询条件使用了普通索引），一般用于=查询
 *          rang：检索给定范围的行，一般用在between < > in等查询
 *          index：遍历整个索引树
 *          all：遍历整个表
 *      possible_keys:可能使用到的索引，一个或者多个（但不一定实际被使用）
 *      key:实际使用的索引，如果为null，则没有使用索引，查询中若使用了覆盖索引，则该索引仅出现在key列表中
 *      key_len:索引中使用的字节数
 *      ref:显示索引的那一列被使用，如果可能的话，是一个常数。那些列或者常量被用于查找索引列上的值（组合索引中，有那些列被真正使用）
 *      rows:MySQL根据表统计信息及索引选用情况，估算的找到所需的记录所需要读取的行数
 *      extra:
 *          using filesort：MySQL中无法利用索引完成排序操作称为“文件排序”
 *          using index：使用索引覆盖
 *          using temporary：mysql使用临时表来处理查询，常见于排序、子查询、分组查询
 *          using where：mysql服务器层使用where过滤数据
 *
 *
 */
public class MysqlTest {
}
