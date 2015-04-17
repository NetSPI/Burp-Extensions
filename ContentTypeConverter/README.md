Content-Type Converter
=========

Burp extension to convert XML to JSON, JSON to XML, x-www-form-urlencoded to XML, and x-www-form-urlencoded to JSON.

###Body Parameter###

```
POST /test HTTP/1.1
Host: www.example.com
Proxy-Connection: keep-alive
Content-Length: 32

parameter1=1&parameters2="test"

```

#####To XML#####
```
POST /test HTTP/1.1
Host: www.example.com
Proxy-Connection: keep-alive
Content-Length: 136
Content-Type: application/xml;charset=UTF-8

<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<root>
<parameters2>"test"</parameters2>
<parameter1>1</parameter1>
</root>
```
#####To JSON#####
```
POST /test HTTP/1.1
Host: www.example.com
Proxy-Connection: keep-alive
Content-Length: 43
Content-Type: application/json;charset=UTF-8

{"parameter1":"1","parameters2":"\"test\""}
```

###JSON to XML###

```
POST /test HTTP/1.1
Host: www.example.com
Proxy-Connection: keep-alive
Content-Length: 43
Content-Type: application/json;charset=UTF-8

{"parameter1":"1","parameters2":"\"test\""}
```
#####To XML#####
```
POST /test HTTP/1.1
Host: www.example.com
Proxy-Connection: keep-alive
Content-Length: 136
Content-Type: application/xml;charset=UTF-8

<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<root>
<parameters2>"test"</parameters2>
<parameter1>1</parameter1>
</root>
```

###XML to JSON###

```
POST /test HTTP/1.1
Host: www.example.com
Proxy-Connection: keep-alive
Content-Length: 136
Content-Type: application/xml;charset=UTF-8

<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<root>
<parameters2>"test"</parameters2>
<parameter1>1</parameter1>
</root>
```
#####To JSON#####
```
POST /text HTTP/1.1
Host: www.example.com
Proxy-Connection: keep-alive
Content-Length: 60
Content-Type: application/json;charset=UTF-8

{"root": {
  "parameters2": "\"test\"",
  "parameter1": 1
}}
```
