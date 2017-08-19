# Hystrix

## 00. Setup
Maven dependency - includes rxjava
Log4j configuration for logging thread name and time

## 01. HystrixCommand
##### synchronous vs asynchronous - observe() vs execute() vs queue()
 **observe()**
`command.execute();`
Blocking execution in hystrix thread

## 02. Resuability
Every single call should be done in a new instance of hystrix command - they are not reusable.

## 03. Exceptions
## 04. Configuration
##### 1 Timeout
##### 2 Threads - bulkhead
Increase total request + 1 (more than total threads)  - show request execution rejection
##### 3 Circuit breaker
https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/circuit-breaker-1280.png

CB properties

*withCircuitBreakerRequestVolumeThreshold()*
Minimum number of requests in the metricsRollingStatisticalWindowInMilliseconds() that must exist before the HystrixCircuitBreaker will trip.
This property sets the minimum number of requests in a rolling window that will trip the circuit.

For example, if the value is 20, then if only 19 requests are received in the rolling window (say a window of 10 seconds) the circuit will not trip open even if all 19 failed.

*.withCircuitBreakerErrorThresholdPercentage()*
Error percentage threshold (as whole number such as 50) at which point the circuit breaker will trip open and reject requests.

*withMetricsRollingStatisticalWindowInMilliseconds()*
Duration of statistical rolling window in milliseconds.

*withCircuitBreakerSleepWindowInMilliseconds()*
The time in milliseconds after a HystrixCircuitBreaker trips open that it should wait before trying requests again.

*.withCircuitBreakerForceOpen(true)* and *HystrixCircuitBreaker hystrixCircuitBreaker = HystrixCircuitBreaker.Factory.getInstance(HystrixCommandKey.Factory.asKey("CommandKey"));
                                                  System.out.println(hystrixCircuitBreaker.allowRequest());*
Show than we can query the state of the command (even future)

##### 4 Other properties
Explain command key / group key / thread pool key differences
*andCommandKey()*
Sets the name of the command executed - by default is the class name

Default ThreadPoolName is taken from HystrixCommandGroupKey
If we logically want these commands grouped together but want them isolated differently then we would use HystrixThreadPoolKey to give each of them a different thread-pool.


## 05. Fallback
Show exceptions when no fallback available and default response for command with fallback
Will back to fallback when we discuss exceptions in hystrix


## 5. Hystrix error handling
##### 5.1 HystrixRuntimeException (failure types)
        - command exception,
        - timeout (add more than 1 sec),
        - shortcircuit (uncomment withCircuitBreakerForceOpen)
        - rejectedThreadExecution (ToManyCommandsMain)
##### 5.2 HystrixBadRequestException (fallback)
Show failing command
https://netflix.github.io/Hystrix/javadoc/com/netflix/hystrix/exception/HystrixBadRequestException.html
An exception representing an error with provided arguments or state rather than an execution failure.
Unlike all other exceptions thrown by a HystrixCommand this will not trigger fallback, not count against failure metrics and thus not trigger the circuit breaker.

NOTE: This should only be used when an error is due to user input such as IllegalArgumentException otherwise it defeats the purpose of fault-tolerance and fallback behavior.

Use FailingCommandWithFallback - fallback is useless here as exception type is HystrixBADRequestException

##### 5.3 unwrapHystrixException() - show code from UMG ?

## 6. Request caching

##### 6.1 Request caching
 - use HystrixRequestContext, show what happened when close context and reinit
Run it and show that cache does not work. Commands are executed every time

`HystrixRequestContext context = HystrixRequestContext.initializeContext();`
Since this depends on request context we must initialize the HystrixRequestContext.

Show that only 2 tasks were executed on hystrix thread pool. Third one was cached

##### 6.2 Invalidating cache - GET-SET-GET
If you are implementing a Get-Set-Get use case where the Get receives enough traffic that request caching is desired but sometimes a Set occurs on another command that should invalidate the cache within the same request, you can invalidate the cache by calling HystrixRequestCache.clear().


## 7. Request batching/collapsing
        - configure withTimerDelayInMilliseconds (delay), and withMaxRequestsInBatch, without HystrixRequestContext no exception!!

## 8. Issue with MDC
http://netflix.github.io/Hystrix/javadoc/com/netflix/hystrix/strategy/concurrency/HystrixConcurrencyStrategy.html#wrapCallable(java.util.concurrent.Callable)


## 9. Hystrix dashboard (open Web project, run Hystrix Dasbhoard from CMD, us ab.exe)

ab -n 20 -c 10 "http://localhost:8080/failing?probability=0.8&sleep=3100"