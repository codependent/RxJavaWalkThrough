# RxWalkThrough

RxJava use examples:

* **sample1**: Hello World
* **sample2**: stopwatch
* **sample3**: hot/cold observables + different types of subjects + unsubscribing
* **sample4**: videoService observable API
* **sample5**: Spring Boot Video App, showing sync and async controller usage of the sample4 observable API.
  Run with `--spring.config.name=sample5-application`
* **sampleapi**: Spring Boot Video Microservices with Eureka Server, Hystrix Clients and Hystrix Dashboard. Each application has to be run with its corresponding `spring.config.name` flag:
  * `--spring.config.name=sample6-eureka`
  * `--spring.config.name=sample6-dashboard`
  * `--spring.config.name=sample6-videoapi-ms`
  * `--spring.config.name=sample6-videoinfo-ms`
  * `--spring.config.name=sample6-videorating-ms`
