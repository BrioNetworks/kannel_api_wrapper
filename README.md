## Kannel Api Wrapper

Simple REST Api sms center which recievies POST request, saves it to DB, redirects it to kannel and gets dlr.

#### Compile and Start

```./gradlew clean build (in case you have wrapper) OR gradle clean build```

#### Settings

[kannel](https://github.com/rasskazovaleksey/kannel_api_wrapper/blob/master/src/main/resources/application-kennel.yml)

[application](https://github.com/rasskazovaleksey/kannel_api_wrapper/blob/master/src/main/resources/application-main.yml)

#### Paths

###### Main 

[HAL Browser](http://10.1.255.254:12999/api) - http://10.1.255.254:12999/api

[SMS](http://10.1.255.254:12999/api/smses/1) - http://10.1.255.254:12999/api/1

[SMSES](http://10.1.255.254:12999/api/smses) - http://10.1.255.254:12999/api/smses{?page,size,sort}

###### Search

[Find by Created and Status](http://10.1.255.254:12999/api/smses/search/findByCreatedAndStatus?status=ACCEPTED&start=2018-01-12T00:00&end=2018-01-12T23:59:59) - http://10.1.255.254:12999/api/smses/search/findByCreatedAndStatus{?start,end,status,page,size,sort}

###### Sent

Sent - GET http://10.1.255.254:12999/api/sms/send{?recever,text} ```ex. http://10.1.255.254:12999/api/sms/send?recever=+0123456789&text=test_me}```
