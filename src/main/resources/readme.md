# Hystrix

## 1. HystrixCommand
##### 1.1 observe() vs execute() vs queue()
##### 1.3 Reusing command
##### 1.4 Hystrix observeOn() and subsequent heavy operations on stream

## 2. Command configuration
##### 2.1 Basic configuration
##### 2.2 CB properties

## 3. Fallback

## 4. Hystrix thread pools
##### 4.1 thread pools - configuration
##### 4.2 isolation strategy - semaphore vs thread
###### 4.2.1 - thread
###### 4.2.2 - semaphore (show what will happen when provide different command key like using counter
###### 4.2.3 - semaphore vs thread in terms of time out
##### 4.3 ignoring rxjava observeOn() and subscribeOn()

## 5. Hystrix error handling
##### 5.1 HystrixRuntimeException
##### 5.2 HystrixBadRequestException
##### 5.3 unwrapHystrixException()

## 6. Request caching

## 7. Request batching/collapsing

## 8. Issue with MDC

## 9. Hystrix dashboard
