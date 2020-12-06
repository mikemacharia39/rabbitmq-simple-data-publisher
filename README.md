# RabbitMQ Simple Data Publisher

* This project shows a simple way of working with rabbitmq

### Getting started

* The application shows how to work with RabbitMQ to publish messages to  a queue
* The application auto set's up exchanges, queues and bindings

* Ensure you have installed the below applications

    |Application|Version|
    |---------|------------|
    |Erlang|10.5|
    |RabbitMQ|3.8.1|
    |JDK| \> 8 |


### Application flows and processes

*  EmployeeController - The class initializes the RabbitMQConfiguration that is responsible for opening connections to 
rabbitMQ
     - The class contains createEmployee() that calls the publishMessage() to queue the message for asynchronous processing


### Running the application

* `mvn clean install package`  

### Rest Invocation

* URL

    http://localhost:8081/employee/create

* Request Body 

    `{
        "employeeName": "Mikehenry", 
        "salary": 10000, 
        "age": 39
    }`
   
### More
* To see how to consume messages from the queue see RabbitMQ data Consumer

    https://github.com/mikemacharia39/rabbitmq-data-consumer
         
 