1 LINUX目录结构

比较重要的是：/home /etc /var /usr/local

  bin:二进制可执行文件（ls,mkdir）
  boot:用于存放系统引导时使用的文件
  dev:用于存放设备文件
  etc:用于存放系统配置文件
  home:存放所有用户文件的根目录
  lib:存放跟文件系统中的程序运行所需要的共享库及内核模块
  mnt:系统管理员安装临时文件系统的安装点
  opt:额外安装的可选应用程序包所存放的位置
  proc:虚拟文件系统，存放当前内存的映射
  root:超级用户目录
  sbin:存放二进制可执行文件，只有ROOT用户才能访问
  tmp:存放临时文件
  usr:用户存放系统应用程序，比较重要的目录/usr/local本地管理员软件安装目录
  var:用户存放运行时需要改变数据的文件

3 常用命令

  文件操作命令

  ls -al

  mkdir

  head

  tail

  find

  grep

  free -h

  stat

  du -h

  df -h

  top

 netstat

 iostat 

 chmod 

 chown

 ps

  