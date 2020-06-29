1 日志
    默认是logback,可以把logback.xml或者logback-spring.xml配置文件放在resources目录下,即可生效。
    logback-spring.xml优先级比较高（会覆盖配置文件中的logging.file.path），logging.file.path同时是为监控/actuator/logfile使用的
  
2 ENDPOINTS
    2.1 POM中添加ACTUATOR依赖，就可以使用spring boot提供的监控，在1.X的版本中，大部分端口默认是开启的,访问路径是/info，而在2.X的版本中，只开启了health,info端口，访问路径是/actuator/info。
    2.2 开启端口：
    management.endpoints.enabled-by-default   是否把端口加入applicationcontext中，与management.endpoints.web.exposure区别是：exposure的端口都是上下文中的，只是要不要暴露
    management.endpoint.<id>.enabled=true     如果management.endpoints.enabled-by-default=false，management.endpoint.<id>.enabled=true表明把端口加入上下文中，并未暴露
    默认情况下shutdown端点是不加入上下文的，可以通过management.endpoint.shutdown.enabled=true把端口加入上下文
    management.endpoints.web.exposure.include 开启一批端口，例如:info,health,env,beans等等，如果是*表明开启所有端口
    常用的端口health,info,beans,logfile,env,loggers,metrics,mappings,scheduledtasks,shutdown,threaddump,heapdump
    /env路径是干啥的

```
management.endpoints.web.exposure.include=health,info,beans,logfile,env,loggers,metrics,mappings,scheduledtasks,shutdown,threaddump,heapdump
logging.file.name=myspring.log
```