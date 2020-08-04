说明：spring版本是5.2.7

1 Spring常用类
    1.1 BeanDefinition       class lazy scope primary PropertyValues ConstructorArgumentValues FactoryBeanName InitMethodName
    1.2 BeanDefinitionHolder    beanDefinition beanName aliases
    1.3 BeanDefinitionRegister  registerBeanDefinition removeBeanDefinition getBeanDefinition
    1.4 BeanDefinitionReader和AnnotatedBeanDefinitionReader   getRegistry getResourceLoader getBeanClassLoader loadBeanDefinitions 
    1.5 AbstractApplicationContext
    1.6 DefaultListableBeanFactory
    1.7 BeanPostProcessor                     postProcessBeforeInitialization  postProcessAfterInitialization
    1.8 InstantiationAwareBeanPostProcessor   postProcessBeforeInstantiation   postProcessAfterInstantiation  postProcessProperties
    1.9 AutowiredAnnotationBeanPostProcessor
    1.10 BeanFactoryPostProcessor              postProcessBeanFactory
    1.11 BeanDefinitionRegistryPostProcessor   postProcessBeanDefinitionRegistry
    1.12 ConfigurationClassPostProcessor
    1.13 Listener
    1.14 Event
    1.15 Environment
    1.16 Aware


​    
2 Spring是如何解决循环依赖的
循环依赖分为两种情况：
1 构造器依赖
2 注入依赖

分为两种模式：
1 单例模式
2 原形模式

归纳起来共四种情况
1 构造器的单例模式
2 构造器的原形模式
3 依赖注入的单例模式
4 依赖注入的原形模式


Spring只能处理3情况的依赖注入，其他不能的原因如下：
如果是单例模式，在getSingleton时：beforeSingletonCreation会检查Set<String> singletonsCurrentlyInCreation这个集合中是否有要创建的对象，如果有直接报BeanCurrentlyInCreationException错误。如果没有创建bean，this.singletonsCurrentlyInCreation.remove(beanName)之后删除beanName
如果是原形模式，参考7，需要注意此时的ThreadLocal<Object> prototypesCurrentlyInCreation是个ThreadLoacl变量

3 Spring启动容器的流程
构造方法{
     this();//会在此处把SPRING内部的BEAN加入到beanDefinitionMap中，其中最重要的是ConfigurationClassPostProcessor和AutowiredAnnotationBeanPostProcessor这两个BEAN
     register(componentClasses);//把CONFIG类加入到beanDefinitionMap中
     refresh();
 }



 3 容器启动过程

以AnnotationConfigApplicationContext容器启动为例：

```
public AnnotationConfigApplicationContext(Class<?>... componentClasses) {
   this();
   register(componentClasses);
   refresh();
}
```

1 this()方法

1 注册5个Spring常用的bean 
    1.1 ConfigurationClassPostProcessor      处理@Configration注解(也会处理@Bean @ComponentScan @Import @ImportSource @PropertySources)
        ConfigurationClassPostProcessor解析@Configration流程
        1 postProcessBeanDefinitionRegistry
        2 找出@Configration的类并为它打上lite\full标示
                checkConfigurationClassCandidate()会判断一个是否是一个配置类,并为BeanDefinition设置属性为lite或者full。
                在这儿为BeanDefinition设置lite和full属性值是为了后面在使用
                如果加了@Configuration，那么对应的BeanDefinition为full;
                如果加了@Bean,@Component,@ComponentScan,@Import,@ImportResource这些注解，则为lite。
                lite和full均表示这个BeanDefinition对应的类是一个配置类
        3 解析找出@Bean @ComponentScan @Import注解相关的类
                解析配置类，在此处会解析配置类上的注解(ComponentScan扫描出的类，@Import注册的类，以及@Bean方法定义的类)
                注意：这一步只会将加了@ComponentScan注解扫描的类才会加入到BeanDefinitionMap中
                通过其他注解(例如@Import、@Bean)的方式，在parse()方法这一步并不会将其解析为BeanDefinition放入到BeanDefinitionMap中，
                先把@Import解析成ConfigurationClass类，并且@Bean并不会解析成ConfigurationClass类
                真正放入到map中是在下面的this.reader.loadBeanDefinitions()方法中实现的
        4 通过reader读取beanDefinition
                将上一步parser解析出的ConfigurationClass类加载成BeanDefinition
                实际上经过上一步的parse()后，解析出来的bean已经放入到BeanDefinition中了，但是由于这些bean可能会引入新的bean，例如实现了ImportBeanDefinitionRegistrar或者ImportSelector接口的bean，或者bean中存在被@Bean注解的方法
                因此需要执行一次loadBeanDefinition()，这样就会执行ImportBeanDefinitionRegistrar或者ImportSelector接口的方法或者@Bean注释的方法
        5 postProcessorBeanFactory
        6 enhanceConfigurationClasses(beanFactory)---增强配置类(为full的类进行CGLIB加强)
          对加了@Configuration注解的配置类进行Cglib代理
                添加了两个MethodInterceptor。(BeanMethodInterceptor和BeanFactoryAwareMethodInterceptor)
                通过这两个类的名称，可以猜出，前者是对加了@Bean注解的方法进行增强，后者是为代理对象的beanFactory属性进行增强
                被代理的对象，如何对方法进行增强呢？就是通过MethodInterceptor拦截器实现的
                类似于SpringMVC中的拦截器，每次执行请求时，都会对经过拦截器。
                同样，加了MethodInterceptor，那么在每次代理对象的方法时，都会先经过MethodInterceptor中的方法
        7 增加ImportAwareBeanPostProcessor 
                为被CGLIB增强时实现了EnhancedConfiguration接口的代理类，设置beanFactory属性?????
    1.2 AutowiredAnnotationBeanPostProcessor 处理@Autowired @Value @Inject注解
    1.3 CommonAnnotationBeanPostProcessor    处理@Resource注解
    1.4 EventListenerMethodProcessor         处理ApplicationEvent和ApplicationListener
    1.5 DefaultEventListenerFactory
2 把@Conponent和@Resource\@Inject注解加入List<TypeFilter> includeFilters中

2 register(componentClasses)

3 refresh()方法

 AbstractApplicationContext.refresh()

 1 prepareRefresh()
 在1中会记录一些状态值，比如STOP ACTIVE  还会初始化earlyApplicationListeners和earlyApplicationEvents两个SET

 2 obtainFreshBeanFactory()
 给工厂启个名字，然后返回一个BEAN工厂,如果是XML文件的工厂，此处会解析XML中的内容加入beanDefinitionMap

 3 prepareBeanFactory()
 给容器添加一些特性比如：classloader\environment

 4 postProcessBeanFactory()
 留给子类实现的方法，可以在此处在beanFactory中添加特性

 5 invokeBeanFactoryPostProcessors()
 执行所有的BeanFactoryPostProcessor处理器

 6 registerBeanPostProcessors()
 注册BeanPostProcessor

 7 initMessageSource()
 Initialize message source for this context.
 初始化messageSource

 8 initApplicationEventMulticaster()
 Initialize event multicaster for this context.
 初始化事件多播器


 9 onRefresh()
 Initialize other special beans in specific context subclasses.
 留给子类实现的，初始化其他BEAN

 10 registerListeners()
 注册监听器

 11 finishBeanFactoryInitialization()-->getBean()
 完成剩余所有BEAN的创建

 12 finishRefresh()
 发布相应的事件

 13 resetCommonCaches()
 完成相应的清理工作




 getBean流程:
  AbstractBeanFactory.doGetBean()
   1 transformedBeanName->处理beanfactory类型对象名字
   2 getSingleton->获取单例对象,此处主要是从缓存中获取,singletonObjects,earlySingletonObjects,singletonFactorie，如果singletonFactorie中存在，则把对象从singletonFactorie删除，并加入到earlySingletonObjects对象中
   3 如果单例对象存在则getObjectForBeanInstance->获取真正的BEA，处理beanfactory的对象
   4 如果单例对象不存在   isPrototypeCurrentlyInCreation(ThreadLocal<Object> prototypesCurrentlyInCreation)->检查对象是否在创建，如果在创建则报BeanCurrentlyInCreationException
   5 markBeanAsCreated->把对象标记为创建中 Set<String> alreadyCreated
   6 如果是单例对象   getSingleton(beanName, ObjectFactory)->
     6.1 通过匿名内部类实现ObjectFactory->调用createBean生成返回的bean对象
       AbstractAutowireCapableBeanFactory.createBean()的调用流程:
       6.1.1 resolveBeforeInstantiation()->在实例化前，如果InstantiationAwareBeanPostProcessor存在，则调用postProcessBeforeInstantiation创建对象，再执行所有BeanPostProcessor的postProcessAfterInitialization方法，然后返回

       6.1.2 调用AbstractAutowireCapableBeanFactory.doCreateBean()
       doCreateBean的调用流程
         6.1.2.1 createBeanInstance->instantiateBean()返回BeanWrapper，BEAN的封装对象
         6.1.2.2 addSingletonFactory->把BEAN加入到singletonFactories中（SmartInstantiationAwareBeanPostProcessor）
         6.1.2.3 populateBea()->执行postProcessAfterInstantiation方法，然后调用SETTER方法给属性赋值  @Autowired
         6.1.2.4 initializeBean()->执行BEAN的初始化方法
                 1 invokeAwareMethods->执行实现了BeanNameAware，BeanClassLoaderAware，BeanFactoryAware接口的属性
                 2 执行BeanPostProcessor处理器的postProcessBeforeInitialization方法
                 3 invokeInitMethods->
                   3.1 执行实现了InitializingBean的方法
                   3.2 执行指定了init_method()的方法
                 4 执行BeanPostProcessor处理器的postProcessAfterInitialization
         6.1.2.5 注册DisposableBean
     6.2 addSingleton->把BEAN加入singletonObjects和registeredSingletons中，并从singletonFactories和earlySingletonObjects中删除
     6.3 通过6.1返回的对象，调用getObjectForBeanInstance，原理同上3
   7 如果是原型模式
     7.1 beforePrototypeCreation -> 在prototypesCurrentlyInCreation中加入当前的BEAN
     7.2 createBean->创建对象 同6.1
     7.3 afterPrototypeCreation-> 把prototypesCurrentlyInCreation中的BEAN删除
     7.4 getObjectForBeanInstance->同3
   8 如果是其他SCOPE
     同7
     
4 Spring中@Configration和@Autowired工作原理

5 Spring事务的原理，AOP
    @PointCut表达式的写法
        execute      execution(* com.xyz.service..*.*(..))
        within       within(com.xyz.service..*) 表达式格式：包名.* 或者 包名..*
        this         this(com.xyz.service.AccountService)  目标对象使用aop之后生成的代理对象必须是指定的类型才会被拦截，注意是目标对象被代理之后生成的代理对象和指定的类型匹配才会被拦截
        target       target(com.xyz.service.AccountService) 目标对象为指定的类型被拦截
        args         args(com.ms.aop.args.demo1.UserModel,..) 匹配第一个参数类型为com.ms.aop.args.demo1.UserModel的所有方法, .. 表示任意个参数
        @target      @target(com.ms.aop.jtarget.Annotation1)  目标对象中包含com.ms.aop.jtarget.Annotation1注解，调用该目标对象的任意方法都会被拦截
        @within      @within(com.ms.aop.jwithin.Annotation1)  声明有com.ms.aop.jwithin.Annotation1注解的类中的所有方法都会被拦截
        @annotation  @annotation(com.ms.aop.jannotation.demo2.Annotation1) 匹配有指定注解的方法（注解作用在方法上面）
        @args        方法参数所属的类型上有指定的注解，被匹配
    @EnableAspectJAutoProxy->@Import(AspectJAutoProxyRegistrar.class)
        AspectJAutoProxyRegistrar->注册internalAutoProxyCreator:AnnotationAwareAspectJAutoProxyCreator.class

​	AnnotationAwareAspectJAutoProxyCreator extends InstantiationAwareBeanPostProcessor,BeanFactoryAware

   所以在registerBeanPostProcessors的时候会把AnnotationAwareAspectJAutoProxyCreator这个对象放到容器中,
   在创建目标对象时，在后置处理器中通过proxyFactory生成代理对象  CGLIB
   执行目标方法时   CglibAopProxy
   List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

```
new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed()
```



@EnableTransactionManagement

​	->@Import(TransactionManagementConfigurationSelector.class) extends ImportSelector

​		->AutoProxyRegistrar ProxyTransactionManagementConfiguration
            AutoProxyRegistrar implements ImportBeanDefinitionRegistrar -> 注册了一个internalAutoProxyCreator=InfrastructureAdvisorAutoProxyCreator
            ProxyTransactionManagementConfiguration是一个@Configuration -> BeanFactoryTransactionAttributeSourceAdvisor\TransactionAttributeSource\TransactionInterceptor三个bean
            
                InfrastructureAdvisorAutoProxyCreator extends InstantiationAwareBeanPostProcessor  作用是把BeanFactoryTransactionAttributeSourceAdvisor这个bean变成增强器
             
             
            JdkDynamicAopProxy 
            	List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
                MethodInvocation invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
                retVal = invocation.proceed();
             
            CglibAopProxy  
            
            
            TransactionManager
             TransactionStatus getTransaction(@Nullable TransactionDefinition definition)
             void commit(TransactionStatus status)
             void rollback(TransactionStatus status)
             
            AnnotationTransactionAttributeSource
            TransactionAnnotationParser   TransactionAttribute parseTransactionAnnotation(AnnotatedElement element)
            TransactionInterceptor   
            TransactionStatus       savepoint
            TransactionDefinition   7大传播属性 4种隔离级别
             

6 ApplicationContext和BeanFactory的区别
  ApplicationContext extends EnvironmentCapable, MessageSource, ApplicationEventPublisher, ResourceLoader



7 Spring的设计模式

​	1 单例模式

​	2 工厂模式

​	3 观察者模式-listener

​	4 责任链模式

​	5 代理模式

   6 适配器模式

8 CGLIIB
    无法为final方法或者类创建代理，无法为static方法创建代理，无法为private方法创建代理
    可以使用System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "target/cglib");设置CGLIB生成的字节码类生成位置
    使用步骤
    1 Enhancer enhancer = new Enhancer();创建一个增强器
    2 enhancer.setSuperclass(NormalClass.class); 设置它的父类为目标类
    3 enhancer.setCallback(new MethodInterceptor() {});设置一个方法拦截器，增强方法
    4 NormalClass normalClass = (NormalClass) enhancer.create(); 创建目标代理类
    5 normalClass.publicMethod(); 执行代理类的方法
    
9 Spring-boot启动流程
    spring-boot自动装配原理
    spring-boot加载tomcat原理