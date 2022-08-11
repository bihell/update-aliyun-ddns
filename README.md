## 功能

通过阿里云提供的API，把指定的域名解析到自己的公网IP(A记录)。

视频教程如下：可以从4分钟开始看

B站：https://www.bilibili.com/video/BV1Nz4y117Jh<br/>
油管：https://youtu.be/p-xbc1xZqx8<br/>
西瓜视频：https://www.ixigua.com/6934161327753527821<br/>

## 使用限制

域名必须由`阿里云/万网托管`

## 程序部署

1.下载[JDK/JRE](https://www.oracle.com/java/technologies/downloads/)。</br>
2.直接下载打包好的[Jar包](https://github.com/bihell/update-aliyun-ddns/releases)或自行下载源码编译。

## 使用

### 第一步:获取域名列表

用法:

    java -jar ./UpdateDomainRecord.jar AccessKeyId AccessKeySecret DomainName RR

举例:

将命令中的`AccessKeyId`和`AccessKeySecret`替换为你自己的[accesskey](https://help.aliyun.com/knowledge_detail/38738.html),`DomainName`改为你在万网购买的域名,`RR`为域名前缀几配置页面的主机记录。以我的域名为例


    更新bihell.com的记录值（IP）
    java -jar UpdateDomainRecord.jar LTAasdf234pQS3I hJda6Xkdasdf124vsqGfT0J3Ls7yK bihell.com @

    更新bigdata.bihell.com的记录值（IP）
    java -jar UpdateDomainRecord.jar LTAasdf234pQS3I hJda6Xkdasdf124vsqGfT0J3Ls7yK bihell.com bigdata


### 定时调用

各位可以自行使用Windows的计划任务或者Linux的Crontab进行定时任务调度。例子如下：

    放在Linux Crontab，每十分钟更新一次
    */10 * * * * java -jar UpdateDomainRecord.jar LTAasdf234pQS3I hJda6Xkdasdf124vsqGfT0J3Ls7yK bihell.com bigdata >> /tmp/ddns.log 2>&1'
