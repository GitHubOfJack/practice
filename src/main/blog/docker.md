VMware->docker->k8s

硬件->操作系统->应用(qq,wechat等等) 目前的架构

多个硬件->hypervisor(虚拟化操作系统-可以把底层的多个硬件变成一个大的硬件，虚拟化技术)->操作系统->应用

docker可以使用宿主机的全部资源，虚拟机只能用分配给他的资源

docker引擎包含：

Docker守护进程<-rest api<-docker命令行工具（container\image\network\data volumes）

Docker image(class) docker container(instance)

docker bulid\docker pull\docker run



apt

Yum





wlp3s0

Enp2s0f0

192.168.90.142

255.255.255.0

192.168.90.255

1  curl -fsSL https://get.docker.com -o get-docker.sh
2  sudo sh get-docker.sh --mirror Aliyun  使用阿里云镜像安装DOCKER
3  在/ETC/DOCKER目录创建daemon.json文件，添加如下内容
{
	"registry-mirrors": ["https://alzgoonw.mirror.aliyuncs.com"]
}
4 执行命令  
root@ubuntu:~# systemctl daemon-reload
root@ubuntu:~# systemctl restart docker
5 使用docker
 docker pull tomcat[:tag] 
 docker images
 docker rmi <image_id>
 docker run -it --rm -p 8888:8080 tomcat[:tag](第一个端口是在容器中的端口，第二个端口是宿主机的端口)
 docker ps <-a> 如果没有-a表示查看当前运行的容器，如果有-a表示所有的容器
 docker rm <容器id>
