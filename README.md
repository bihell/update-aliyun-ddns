[中文说明](http://www.bihell.com/2017/08/07/aliyun-ddns/)

## Introduction

This application will get your external IP then update A record for your specificed domain via API provided by [Aliyun](https://www.alibabacloud.com/) .

## Limit

Your domain must be managed by Aliyun's domain service, otherwise it will not work.

## Depoly

1.Download JDK/JRE
2.Download [lastest release](https://github.com/bihell/UpdateDomainRecord/releases) or Source code

## Usage

### First: Get domain list

Usage:

    java -jar ./UpdateDomainRecord.jar DescribeDomainRecords AccessKeyId AccessKeySecret DomainName

Example:
Replace `AccessKeyId` and `AccessKeySecret` with your [accesskey](https://help.aliyun.com/knowledge_detail/38738.html) . Replace `DomainName` with the one you purchased from Aliyun.

    java -jar UpdateDomainRecord.jar DescribeDomainRecords LTAasdf234pQS3I hJda6Xkdasdf124vsqGfT0J3Ls7yK example.com

Results:

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

### Second: Set A record

Usage:

    java -jar /UpdateDomainRecord.jar UpdateDomainRecord AccessKeyId AccessKeySecret RecordId RR

Example:
Replace `AccessKeyId` and `AccessKeySecret` with your [accesskey](https://help.aliyun.com/knowledge_detail/38738.html), You can get `RecordId` and `RR` value from the results of first step.

    java -jar /UpdateDomainRecord.jar UpdateDomainRecord  LTAasdf234pQS3I hJda6Xkdasdf124vsqGfT0J3Ls7yK 1351234134 pan

### Scheduler

You can make a schedule in Cron Job (Linux) or Scheduler Plan (Window) .