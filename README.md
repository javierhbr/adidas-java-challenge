
# Adidas Code challenge by Javier Benavides.


Adidas Code challenge by Javier Benavides.

#### Stack:
- Java 11
- Gradle
- Spring Boot
- MongoDB
- RabittMQ
- Swagger
- Docker Compose

###Summary:

- **email-service** : Service to listen and consume all em notification messages, thinking on process async because it's has a 60secs of, this service just consumes messages from a queue (RabiitMQ).
- **public-service** : It's a public entry point for newsletter subscription, this service check the security to access it using Basic auth  
- **subscription-service**: This service contains the source of truth for subscription and persists the data with MongoDB and sends message notification when it needs it.

The 3 services were made in order to scale functionality easily change dependency like DB or Messaging tools.

###### Diagram:

![Optional Text](images/challengeDiagram.png)


#####Considerations:

- One important thing in order to make it more resilient is to implement a circuit breaker especially for the public service to avoid overload the subscription service if something happened, which I ran out of time to implement, but can be implemented on each service or gateway side.
- All the Error are handler by a controllerAdvice in order to return a unique response.
- Because I ran out of time I couldn't implement a proper testing suite like integration test and 100% unit test coverage.
- Most of the configurations use spring profiles. it's will be easy to change configuration using a config server service.
- The services use spring sleuth to tracing the transactions across all the services


##Endpoint:

#### Test endpoint:
There are 2 http files to test the endpoints:

- http-test/public-service.http
- http-test/subscription-service.http 

Swagger
- URL: http://localhost:8080/swagger-ui.html

### Public-service:

- URL API : http://localhost:8080/api/
- Swagger URL: http://localhost:8080/swagger-ui.html

###### Authentication:
Use basic authentication: user **challenge** and pwd **challenge**
`Authorization: Basic Y2hhbGxlbmdlOmNoYWxsZW5nZQ==`

###### Create a new subscription: 
 Validate request Data and call subscription-service to return the ID of the registrations 
  
Example:
http://localhost:3000/swagger-docs/#/Subscriptions/post_subscriptions

Example:
```json
POST http://localhost:8080/api/subscriptions
accept: application/json
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InN0cmluZyIsImxhc3RMb2dpbiI6IjIwMjEtMDMtMjQgMjA6MjQ6NTEiLCJsb2dpbkRhdGUiOiIyMDIxLTAzLTI0IDIwOjI0OjUxIiwiZXhwaXJlc0luIjoiMzYwZCIsImV4cGlyZURhdGUiOiIyMDIyLTAzLTE5IDIwOjI0OjUxIiwiaWF0IjoxNjE2NjE3NDkxLCJleHAiOjE2MTY2MjEwOTF9.h-9Q_dvQ0a_qGGUHXLyE7qOeINXPDdOQ37a8Cl8CFMc

{
"email": "email@test.com",
"firstName": "string",
"dateOfBird": "1970-01-01",
"isConsent": true,
"newsletterId": "string"
}
```

###### Cancel existing subscription: 
Validate request Data and call subscription-service to remove a subscription from the DB

Example:
```json
DELETE http://localhost:8080/api/subscriptions/{ID}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InN0cmluZyIsImxhc3RMb2dpbiI6IjIwMjEtMDMtMjQgMjA6MjQ6NTEiLCJsb2dpbkRhdGUiOiIyMDIxLTAzLTI0IDIwOjI0OjUxIiwiZXhwaXJlc0luIjoiMzYwZCIsImV4cGlyZURhdGUiOiIyMDIyLTAzLTE5IDIwOjI0OjUxIiwiaWF0IjoxNjE2NjE3NDkxLCJleHAiOjE2MTY2MjEwOTF9.h-9Q_dvQ0a_qGGUHXLyE7qOeINXPDdOQ37a8Cl8CFMc
accept: application/json
```

###### Get details of a subscription: 
Validate subscriptionId and retrieve the data from subscription-service, it will return a 404 if the id is not found.
```json
GET http://localhost:8080/api/subscriptions/ID
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InN0cmluZyIsImxhc3RMb2dpbiI6IjIwMjEtMDMtMjQgMjA6MjQ6NTEiLCJsb2dpbkRhdGUiOiIyMDIxLTAzLTI0IDIwOjI0OjUxIiwiZXhwaXJlc0luIjoiMzYwZCIsImV4cGlyZURhdGUiOiIyMDIyLTAzLTE5IDIwOjI0OjUxIiwiaWF0IjoxNjE2NjE3NDkxLCJleHAiOjE2MTY2MjEwOTF9.h-9Q_dvQ0a_qGGUHXLyE7qOeINXPDdOQ37a8Cl8CFMc
accept: application/json
```

###### Get all subscriptions: 
It will return all the data from subscription-service, but using pagination to avoid overload the network and the DB.
- page: it's the number of the page, where 0 is the first.
- pageSize is the number of items per page.

Example:
```json
GET http://localhost:3000/api/subscriptions?page=0&sizePage=1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InN0cmluZyIsImxhc3RMb2dpbiI6IjIwMjEtMDMtMjQgMjA6MjQ6NTEiLCJsb2dpbkRhdGUiOiIyMDIxLTAzLTI0IDIwOjI0OjUxIiwiZXhwaXJlc0luIjoiMzYwZCIsImV4cGlyZURhdGUiOiIyMDIyLTAzLTE5IDIwOjI0OjUxIiwiaWF0IjoxNjE2NjE3NDkxLCJleHAiOjE2MTY2MjEwOTF9.h-9Q_dvQ0a_qGGUHXLyE7qOeINXPDdOQ37a8Cl8CFMc
accept: application/json
```


### Subscription-service:

###### Create a new subscription: 
Validate request Data and save it into the DB to return the ID after sent an email notification
Example:
```json
POST http://localhost:9090/api/subscriptions
accept: application/json
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InN0cmluZyIsImxhc3RMb2dpbiI6IjIwMjEtMDMtMjQgMjA6MjQ6NTEiLCJsb2dpbkRhdGUiOiIyMDIxLTAzLTI0IDIwOjI0OjUxIiwiZXhwaXJlc0luIjoiMzYwZCIsImV4cGlyZURhdGUiOiIyMDIyLTAzLTE5IDIwOjI0OjUxIiwiaWF0IjoxNjE2NjE3NDkxLCJleHAiOjE2MTY2MjEwOTF9.h-9Q_dvQ0a_qGGUHXLyE7qOeINXPDdOQ37a8Cl8CFMc

{
"email": "email@test.com",
"firstName": "string",
"dateOfBird": "1970-01-01",
"isConsent": true,
"newsletterId": "string"
}
```

###### Cancel existing subscription: 
Validate request Data and delete it from the DB, after that send an email notification.
Example:
```json
DELETE http://localhost:9090/api/subscriptions/{ID}
accept: application/json
```

###### Get details of a subscription: 
Validate subscription and retrieve the data from the DB, it will return a 404 if the id is not found.
Example:
```json
GET http://localhost:9090/api/subscriptions/ID
accept: application/json

```

###### Get all subscriptions: 
It will return all the data from DB using pagination to avoid overload the network and the DB.
- page: it's the number of the page, where 0 is the first.
- pageSize is the number of items per page.
Example:
```json
GET http://localhost:9090/api/subscriptions?page=0&sizePage=1
accept: application/json
```


### Email-service:

###### Notification listener: 
This listener is consuming a queue to process any type of notification, designed to process  Email, SMS, or PUSH notification for any action.
Consuming messages from queue `newsletter_notification`

Message Format:
```json
{
  "notificationType": "EMAIL",
  "destination": "string",
  "message": "string"
}
```
Notification Types: EMAIL, SMS, MOBILE_PUSH.


###### RabbitMQ console:

URL : http://localhost:15672/
user /pwd  : challenge / challenge


token:

```json
  {
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InN0cmluZyIsImxhc3RMb2dpbiI6IjIwMjEtMDMtMjEgMjE6MjI6MTYiLCJsb2dpbkRhdGUiOiIyMDIxLTAzLTIxIDIxOjIyOjE2IiwiZXhwaXJlc0luIjoiMzYwZCIsImV4cGlyZURhdGUiOiIyMDIyLTAzLTE2IDIxOjIyOjE2IiwiaWF0IjoxNjE2Mzc5NzM2LCJleHAiOjE2MTYzODMzMzZ9.mwG6Ry9qgnrl3jX0yotfYe-46pqL83Ay2VWVkbLn2NU"
  }

```




## Installation

```bash
$ ./gradlew clean build 
```

## Running the app

On each module:
```bash
# development
$ ./gradlew bootRun 
```

run with help:

execute **runServices.sh** with options for:
- A = Run only services (No DB or RabbitMQ).
- B = Run environments tools (Mongo and RabbitMQ) . 
- C = Run 3 services with  Mongo and RabbitMQ.

```
./runServices.sh

```

With docker-compose:

A- `docker-compose-services.yaml` run 3 services, without  Mongo and RabbitMQ 

```
docker-compose -f docker-compose-services.yaml up --build

``` 

B- `docker-compose-services-env.yaml` run only Mongo and RabbitMQ 

```
docker-compose -f docker-compose-services.yaml up --build

``` 

B- `docker-compose-services.yaml` run 3 services with  Mongo and RabbitMQ 

```
docker-compose -f docker-compose.yaml up --build

``` 


## Test

```bash
# unit tests
$ npm run test

# test coverage
$ npm run test:cov
```

Testing report  on each module inside package:

packages:
- email-service
- public-service
- subscription-service

packages/{MODULE_NAME}/coverage/lcov-report/index.html