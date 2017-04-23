![Plano Logo](/docs/plano.png)

# Plano
[![Build Status](https://travis-ci.org/milton0825/plano.svg?branch=master)](https://travis-ci.org/milton0825/plano) [![codecov](https://codecov.io/gh/milton0825/plano/branch/master/graph/badge.svg)](https://codecov.io/gh/milton0825/plano)

Plano is a simple scheduling service that allows clients to schedule asynchronous redirected HTTP requests.

# API
Plano provides convenient HTTP APIs for client to create, read, update and delete requests.

## GET
```
GET host:port/requests/{requestID} HTTP/1.1
```

## POST
```
POST host:port/requests HTTP/1.1

{
    "httpRequest":{
        "uri":"uri",
        "payload":"payload",
        "charset":"UTF-8",
        "httpMethod":"POST",
        "headers":{
            "header1":"header1",
            "header2":"header2"
        },
        "connectionTimeoutMs":1000,
        "socketTimeoutMs":1000,
        "connectionRequestTimeoutMs":1000
    },
    "executionTime":"Wed Dec 14 23:34:16 PST 2016",
    "schedulePolicy":{
        "multiplier":1,
        "executionIntervalMs":1000,
        "numberOfExecutions":10
    }
}
```

## PUT
```
PUT host:port/requests/{requestID} HTTP/1.1

{
    "httpRequest":{
        "uri":"uri",
        "payload":"payload",
        "charset":"UTF-8",
        "httpMethod":"POST",
        "headers":{
            "header1":"header1",
            "header2":"header2"
        },
        "connectionTimeoutMs":1000,
        "socketTimeoutMs":1000,
        "connectionRequestTimeoutMs":1000
    },
    "executionTime":"Wed Dec 14 23:34:16 PST 2016",
    "schedulePolicy":{
        "multiplier":1,
        "executionIntervalMs":1000,
        "numberOfExecutions":10
    }
}
```

## DELETE
```
DELETE host:port/requests/{requestID} HTTP/1.1
```

# Building Plano from Source
```
git clone git@github.com:milton0825/plano.git
cd plano
mvn clean install
```
