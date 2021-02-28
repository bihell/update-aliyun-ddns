[中文](https://github.com/bihell/UpdateDomainRecord) [English](https://github.com/bihell/UpdateDomainRecord/blob/master/README-EN.md)

## 功能

通过阿里云提供的API，把指定的域名解析到自己的公网IP(A记录)。

视频教程如下：可以从4分钟开始看

B站：https://www.bilibili.com/video/BV1Nz4y117Jh
油管：https://youtu.be/p-xbc1xZqx8
西瓜视频：https://www.ixigua.com/6934161327753527821

## 使用限制

域名必须由阿里云/万网托管

## 程序部署

1.下载JDK/JRE。
2.直接下载打包好的Jar包或自行下载源码。

## 使用

### 第一步:获取域名列表

用法:

    java -jar ./UpdateDomainRecord.jar DescribeDomainRecords AccessKeyId AccessKeySecret DomainName

举例:
将命令中的`AccessKeyId`和`AccessKeySecret`替换为你自己的[accesskey](https://help.aliyun.com/knowledge_detail/38738.html),`DomainName`改为你在万网购买的域名.

    java -jar UpdateDomainRecord.jar DescribeDomainRecords LTAasdf234pQS3I hJda6Xkdasdf124vsqGfT0J3Ls7yK example.com

返回结果:

    {
        "PageNumber": 1,
        "TotalCount": 4,
        "PageSize": 20,
        "RequestId": "9A7EF620-CFFD-4EF1-9C6D-F3A486E85D73",
        "DomainRecords": {
            "Record": [
                {
                    "RR": "pan",
                    "Status": "ENABLE",
                    "Value": "10.2.33.222",
                    "RecordId": "1351234134",
                    "Type": "A",
                    "DomainName": "example.com",
                    "Locked": false,
                    "Line": "default",
                    "TTL": "600"
                },
            ]
        }
    }

### 第二部:设置解析

用法:

    java -jar /UpdateDomainRecord.jar UpdateDomainRecord AccessKeyId AccessKeySecret RecordId RR

举例:
将命令中的`AccessKeyId`和`AccessKeySecret`替换为你自己的[accesskey](https://help.aliyun.com/knowledge_detail/38738.html),`RecordId`和`RR`改为你上面返回结果中需要修改域名的对应值.

    java -jar /UpdateDomainRecord.jar UpdateDomainRecord  LTAasdf234pQS3I hJda6Xkdasdf124vsqGfT0J3Ls7yK 1351234134 pan

### 定时调用

各位可以自行使用Windows的计划任务或者Linux的Crontab进行定时任务调度。
