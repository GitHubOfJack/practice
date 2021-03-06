---
typora-root-url: ..\image
---

```java
/**
 * MQ作用
 * 异步：同步接口做成异步，可以提供性能
 * 解耦：解决各个系统之间的耦合度
 * 限流：当数据量比较大时，可以控制消费端的消费速率来实现限流
 *
 * 缺点
 * 降低了系统的可用性（需要考虑消息服务器的监控状态）
 * 提高了系统的复杂性（需要考虑消息重复、消息丢失、消息顺序性、消息堆积）
 * 需要考虑一致性问题（同一个消息ABC系统都处理了，但是D系统处理失败了）
 *
 *
 * rocketmq的消费端没有真正意义的PUSH，底层是长轮训机制+监听（看着像是PUSH）
 * 之所以采用PULL是考虑服务器端消费太慢，数据堆积的问题。PULL需要自己维护Offset。
 *
 * rocketmq的PUSH模式的流程
 *  消费者端
 *  1 后台独立线程—rebalanceService根据Topic中消息队列个数和当前消费组内消费者个数进行负载均衡，
 *      将产生的对应PullRequest实例放入阻塞队列—pullRequestQueue中
 *  2 后台独立的线程—PullMessageService不断地从阻塞队列—pullRequestQueue
 *      中获取PullRequest请求并通过网络通信模块发送Pull消息的RPC请求给Broker端
 *  Broker端
 *      1 消费者如果第一次尝试Pull消息失败（比如：Broker端没有可以消费的消息），并不立即给消费者客户端返回Response的响应，而是先hold住并且挂起请求（将请求保存至pullRequestTable本地缓存变量中）
 *      2 然后Broker端的后台独立线程—PullRequestHoldService会从pullRequestTable本地缓存变量中不断地去取，具体的做法是查询待拉取消息的偏移量是否小于消费队列最大偏移量，如果条件成立则说明有新消息达到Broker端
 *
 *
 * RocketMQ-Push模式下并发消费和顺序消费的区别
 * 并发消费：consumer.registerMessageListener(new MessageListenerConcurrently() {}
 *      // 消费成功
 *     CONSUME_SUCCESS,
 *     // 消费失败，稍后再从Broker拉取消息重新消费（并发消费（重新消费的消息由Broker复制原消息，并丢入重试队列））
 *     RECONSUME_LATER;
 * 顺序消费：consumer.registerMessageListener(new MessageListenerOrderly() {}
 *      // 消费成功
 *     SUCCESS,
 *     // 消费失败，挂起当前队列，挂起期间，当前消息重试消费，直到消息进入死信队列(重新消费不涉及Broker)
 *     SUSPEND_CURRENT_QUEUE_A_MOMENT;
 *
 *
 *  TOPIC:标识消息的第一级别，例如贷前消息、贷后消息、催收消息等
 *  TAG：标识消息的第二级别，例如合同信息消息、还款计划消息等
 *  QUEUE:一个topic下，我们可以设置多个queue(消息队列)
 *  Producer 与 Producer Group
 *  Consumer 与 Consumer Group(消费者有推拉两种模式)
 *  Broker:消息的中转者，负责存储和转发消息
 *  广播消费:一条消息被多个Consumer消费，即使这些Consumer属于同一个Consumer Group，消息也会被Consumer Group中的每个Consumer都消费一次
 *  集群消费:一个Consumer Group中的Consumer实例平均分摊消费消息。例如某个Topic有 9 条消息，其中一个Consumer Group有 3 个实例(可能是 3 个进程,或者 3 台机器)，那么每个实例只消费其中的 3 条消息。
 *  NameServer:
 *      接收broker的请求，注册broker的路由信息
 *      接收client的请求，根据某个topic获取其到broker的路由信息
 *      NameServer没有状态，可以横向扩展。每个broker在启动的时候会到NameServer注册；Producer在发送消息前会根据topic到NameServer获取路由(到broker)信息；Consumer也会定时获取topic路由信息。
 *
 * 1 重复消费的解决方案： 接口做幂等校验
 * 2 消息丢失问题（ACTIVEMQ和ROCKETMQ可能还不太一样，只是给出一个大致的解决方案）：
 *      1 发送端消息丢失
 *          发送方式有同步发送、异步发送
 *          同步发送会返回SEND_OK，FLUSH_DISK_TIMEOUT，FlUSH_SLAVE_TIMEOUT，SLAVE_NOT_AVAILABLE等结果，根据结果进行相应的处理
 *          异步发送会注册一个回调函数，优点是性能高，可以把结果记录日志或者数据库，定时任务处理相关失败数据+邮件人工处理)
 *      2 MQ服务器本身消息丢失
 *          配置持久化方案（可以采用落盘+主从备份完成）
 *          设置为同步刷盘 即FlushDiskType = SYSNC_FLUSH（默认为异步刷盘方式）-损耗性能，需要根据场景来使用
 *          若有slave备份：设置为同步复制 ，即SYSNC_MASTER(默认是异步Master即ASYNC_MASTER)
 *      3 消费端消息丢失
 *          消息消费成功之后会发送CONSUME_SUCCESS给Broker代表消费成功
 *          如果消费超时、消费服务宕机等情况MQ会重试发送（默认是16次），如果重试16次不成功会进入死信队列，需要单独处理死信队列中的数据
 *
 * 3 顺序消息
 *      原理是生产者根据业务需求，比如合同号，订单号之类的数据按照一定规则分片，然后按照顺序放到同一个队列中，消费者顺序消费,ROCKETMQ提供了MessageQueueSelector接口
 * 4 消息堆积
 *      1 分析堆积产生的原因（生产者生产太快或者消费者消费太慢或者消费者宕机）
 *      2 如果生产者太快，对MQ服务器造成了影响，可以采用生产者限流等操作
 *      3 如果是消费者太慢，增加消费者服务（需要考虑对其他资源的压力情况，例如数据库等）
 *        如果消息都在一个队列中，业务允许的情况下则可以把一个队列拆分成多个队列，并行消费，增加效率
 *      4 如果是消费者程序出错，如果简单就修复
 *          如果短时间内难以解决，可以临时把堆积的数据放入到一个新的broker中，然后再解决问题。
 *
 * 5 ROCKETMQ的事物消息
 *      1 发送half消息给MQ（此消息消费端看不到，此消息不是直接放入目标的Topic队列，而是放入RMQ_SYS_TRANS_HALF_TOPIC队列等收到commit命令之后再放入真实的队列）
 *      2 MQ发送half消息接收成功
 *      3 执行本地事物
 *      4 发送commit或者rollback指令给MQ
 *      5 如果是commit，则发送消息给消费端（推模式）
 *      6 如果是rollback，则删除消息
 *      7 如果commit或者rollback指令发送失败，则MQ会有回查机制（需要发送端提供接口）查询事物状态(回查也有次数限制，默认是15次，超过之后默认rollback)
 *
 * 6 Rocketmq原理
 *      RocketMQ由NameServer注册中心集群、Producer生产者集群、Consumer消费者集群和若干Broker（RocketMQ进程）组成，它的架构原理是这样的：
 *      1 Broker在启动的时候去向所有的NameServer注册，并保持长连接，每30s发送一次心跳
 *      2 Producer在发送消息的时候从NameServer获取Broker服务器地址，根据负载均衡算法选择一台服务器来发送消息
 *      3 Consumer消费消息的时候同样从NameServer获取Broker地址，然后采用PUSH模式推送给客户端
 *      生产者在第一次发送消息的时候从NameServer获取到Broker地址后缓存到本地，如果NameServer整个集群不可用，短时间内对于生产者和消费者并不会产生太大影响。
 *      心跳机制
 * 单个Broker跟所有Namesrv保持心跳请求，心跳间隔为30秒，心跳请求中包括当前Broker所有的Topic信息。Namesrv会反查Broer的心跳信息， 如果某个Broker在2分钟之内都没有心跳，则认为该Broker下线，调整Topic跟Broker的对应关系。但此时Namesrv不会主动通知Producer、Consumer有Broker宕机。
 * Consumer跟Broker是长连接，会每隔30秒发心跳信息到Broker。Broker端每10秒检查一次当前存活的Consumer，若发现某个Consumer 2分钟内没有心跳， 就断开与该Consumer的连接，并且向该消费组的其他实例发送通知，触发该消费者集群的负载均衡(rebalance)。
 * 生产者每30秒从Namesrv获取Topic跟Broker的映射关系，更新到本地内存中。再跟Topic涉及的所有Broker建立长连接，每隔30秒发一次心跳。 在Broker端也会每10秒扫描一次当前注册的Producer，如果发现某个Producer超过2分钟都没有发心跳，则断开连接。
 *
 *
 * 7 Rocketmq的集群模式
 *      1 单Master模式
 *      2 多Master模式
 *      3 多master,多slave异步复制模式（master宕机、磁盘损坏，会丢失部分数据）,master宕机后，可以从slave消费消息
 *      4 多master,多slave同步双写模式（性能受到影响）,master,slave必须都写入成功之后才会返回成功
 *
 * 8 RocketMQ的部署结构有以下特点：
 *      1 Name Server是一个几乎无状态节点，可集群部署，节点之间无任何信息同步。
 *      2 Broker 部署相对复杂，Broker 分为 Master 与 Slave，一个 Master（主人） 可以对应多个 Slave(奴隶)，但是一个Slave只能对应一个Master，
 *        Master与Slave的对应关系通过指定相同的 BrokerName，不同的BrokerId来定义，BrokerId为0表示Master，非0表示Slave。Master也可以部署多个，
 *        每个Broker与Name Server集群中的所有节点建立长连接，定时注册Topic 信息到所有 Name Server。
 *      3 Producer 与 Name Server 集群中的其中一个节点（随机选择）建立长连接，定期从Name Server取Topic路由信息，并向提供 Topic 服务的 Master 建立长连接，
 *          且定时向 Master 发送心跳。Producer完全无状态，可集群部署。
 *      4 Consumer 与 Name Server 集群中的其中一个节点（随机选择）建立长连接，定期从Name Server取Topic路由信息，并向提供 Topic 服务的 Master、Slave 建立长连接，
 *          且定时向 Master、Slave发送心跳。Consumer既可以从Master订阅消息，也可以从Slave订阅消息，订阅规则由Broker配置决定。
 *
 * 9 为什么RocketMQ不使用Zookeeper作为注册中心呢？
 *      ZK设计是满足CP的，会损失可用性，而MQ客户端是弱依赖注册中心的，它会把部分信息保存到本地（类似DUBBO），性能考虑。
 *      ZK可以实现其他功能比如选主复杂功能，MQ的注册中心不用这些功能，只用简单的发现功能就可以了。
 *
 * 10 Broker是怎么保存数据
 *      RocketMQ主要的存储文件包括commitlog文件、consumequeue文件、indexfile文件。
 *      Broker在收到消息之后，会把消息保存到commitlog的文件当中，而同时在分布式的存储当中，每个broker都会保存一部分topic的数据，
 *      同时，每个topic对应的messagequeue下都会生成consumequeue文件用于保存commitlog的物理位置偏移量offset，indexfile中会保存key和offset的对应关系。
 *      CommitLog文件保存于${Rocket_Home}/store/commitlog目录中，从图中我们可以明显看出来文件名的偏移量，每个文件默认1G，写满后自动生成一个新的文件。
 *      由于同一个topic的消息并不是连续的存储在commitlog中，消费者如果直接从commitlog获取消息效率非常低，所以通过consumequeue保存commitlog中消息的偏移量的物理地址，
 *      这样消费者在消费的时候先从consumequeue中根据偏移量定位到具体的commitlog物理文件，然后根据一定的规则（offset和文件大小取模）在commitlog中快速定位。
 *
 *      RocketMQ的消息的存储是由ConsumeQueue和CommitLog配合来完成的，ConsumeQueue中只存储很少的数据，消息主体都是通过CommitLog来进行读写。
 *      如果某个消息只在CommitLog中有数据，而ConsumeQueue中没有，则消费者无法消费，RocketMQ的事务消息实现就利用了这一点。
 *
 *
 *
 *
 * 11 Master和Slave之间是怎么同步数据的呢？
 *  消息在master和slave之间的同步是根据raft协议来进行的：
 *
 *  在broker收到消息后，会被标记为uncommitted状态
 *  然后会把消息发送给所有的slave
 *  slave在收到消息之后返回ack响应给master
 *  master在收到超过半数的ack之后，把消息标记为committed
 *  发送committed消息给所有slave，slave也修改状态为committed
 *
 * 12 你知道RocketMQ为什么速度快吗？
 * 是因为使用了顺序存储、零拷贝，Page Cache和异步刷盘。
 *
 * 我们在写入commitlog的时候是顺序写入的，这样比随机写入的性能就会提高很多
 * 写入commitlog的时候并不是直接写入磁盘，而是先写入操作系统的PageCache
 * 最后由操作系统异步将缓存中的数据刷到磁盘
 *
 * 13 Push模式下并发消费和顺序消费的区别
 *  并发消费（重新消费的消息由Broker复制原消息，并丢入重试队列）：
 *      消费者返回ConsumeConcurrentlyStatus.RECONSUME_LATER时， Broker会创建一条与原先消息属性相同的消息，并分配新的唯一的msgId，另外存储原消息的msgId，新消息会存入到commitLog文件中，并进入重试队列，拥有一个全新的队列偏移量，延迟5s后重新消费。如果消费者仍然返回RECONSUME_LATER，那么会重复上面的操作，直到重新消费maxReconsumerTimes次，当重新消费次数超过最大次数时，进入死信队列，消息消费成功。
 *  顺序消费（重新消费不涉及Broker）：
 *      消费者返回ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT时，当前队列会挂起（此消息后面的消息停止消费，直到此消息完成消息重新消费的整个过程），然后此消息会在消费者的线程池中重新消费，即不需要Broker重新创建新的消息（不涉及重试队列），如果消息重新消费超过maxReconsumerTimes最大次数时，进入死信队列。当消息放入死信队列时，Broker服务器认为消息时消费成功的，继续消费该队列后续消息。
 *
 *  14 commitlog consumerqueue indexfile
 *      commitlog每个文件默认1G，文件名称20位，是起始偏移量不足20位，左边补0，所有数据均存在commitlog中，消息是顺序读写
 *      一个consumerqueue对应一个TOPIC下的一个队列，内容是消息在commitlog中的偏移量，可以理解成索引文件
 *      读取数据需要先从队列的consumerqueue中找出offset再从commitlog中找出消息。
 *      indexfile:如果我们需要根据消息ID查询消息，为了避免遍历commitlog，建立了indexfile
 *
 *  15 消息事物的原理
 *      1 先写入一个half message到commitlog，通过更改half message的topic值，来保证这个half message并不会加入到consumerqueue中从而实现消费者无法看到。(所有的半消息都使用同一个topic)
 *      broker内部还会维护一个op消息，用来标识事物消息状态是否确定（commit或者rollback），它会将所有没有终态的消息放入一个OP消息队列，用来做回查.
 *      Commit相对于Rollback只是在写入Op消息前创建Half消息的索引。
 *      2 如果消息需要回滚则写入OP消息
 *      3 如果是COMMIT，读取出half message并设置成真正的top,然后生成一个普通消息，重新写入commitlog中(事物消息的内容会存在两份)
 *
 *
 *
 *
 *
 * */
```



https://zhuanlan.zhihu.com/p/347131921
https://www.cnblogs.com/javazhiyin/p/13327925.html

https://my.oschina.net/javazhiyin/blog/4401725

https://www.cnblogs.com/xuwc/p/9034352.html

![rocketmq-topic-broker-queue关系](/rocketmq-架构图.png)

![rocketmq-topic-broker-queue关系](/rocketmq-topic-broker-queue关系.png)

![rocketmq事物](/rocketmq事物.png)