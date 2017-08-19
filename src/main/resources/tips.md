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
Unlike all other exceptions thrown by a HystrixCommand this will not trigger fallback, not count against failure metrics and thus not trigger the circuit breaker.