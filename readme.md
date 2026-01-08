# Reproducer for Netty ByteBuf leak

The test case demonstrates a Netty ByteBuf leak issue caused by the combination of Spring Boot Actuator, Reactive Spring Data MongoDB and Spring WebFlux.

Test case: [src/test/java/com/example/demo/DemoApplicationTests.java](src/test/java/com/example/demo/DemoApplicationTests.java)

If either of the following dependencies is removed, the leak does not occur:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```
2026-01-08T18:06:35.175+01:00 ERROR 29243 --- [demo] [tter-2-thread-1] io.netty.util.ResourceLeakDetector       : LEAK: ByteBuf.release() was not called before it's garbage-collected. See https://netty.io/wiki/reference-counted-objects.html for more information.
Recent access records: 
Created at:
        io.netty.buffer.AdaptiveByteBufAllocator.newDirectBuffer(AdaptiveByteBufAllocator.java:67)
        io.netty.buffer.AbstractByteBufAllocator.directBuffer(AbstractByteBufAllocator.java:168)
        io.netty.buffer.AbstractByteBufAllocator.buffer(AbstractByteBufAllocator.java:104)
        com.mongodb.internal.connection.netty.NettyStream.getBuffer(NettyStream.java:160)
        com.mongodb.internal.connection.InternalStreamConnection.getBuffer(InternalStreamConnection.java:872)
        com.mongodb.internal.connection.ByteBufferBsonOutput.getByteBufferAtIndex(ByteBufferBsonOutput.java:190)
        com.mongodb.internal.connection.ByteBufferBsonOutput.getCurrentByteBuffer(ByteBufferBsonOutput.java:176)
        com.mongodb.internal.connection.ByteBufferBsonOutput.writeInt32(ByteBufferBsonOutput.java:91)
        com.mongodb.internal.connection.RequestMessage.writeMessagePrologue(RequestMessage.java:105)
        com.mongodb.internal.connection.RequestMessage.encode(RequestMessage.java:94)
        com.mongodb.internal.connection.CommandMessage.encode(CommandMessage.java:73)
        com.mongodb.internal.connection.InternalStreamConnection.sendAndReceiveAsyncInternal(InternalStreamConnection.java:555)
        com.mongodb.internal.connection.InternalStreamConnection.lambda$sendAndReceiveAsync$1(InternalStreamConnection.java:392)
        com.mongodb.internal.async.AsyncSupplier.finish(AsyncSupplier.java:68)
        com.mongodb.internal.async.AsyncSupplier.getAsync(AsyncSupplier.java:49)
        com.mongodb.internal.connection.InternalStreamConnection.lambda$sendAndReceiveAsync$2(InternalStreamConnection.java:395)
        com.mongodb.internal.async.AsyncSupplier.finish(AsyncSupplier.java:68)
        com.mongodb.internal.async.AsyncRunnable.lambda$thenSupply$7(AsyncRunnable.java:218)
        com.mongodb.internal.async.SingleResultCallback.complete(SingleResultCallback.java:63)
        com.mongodb.internal.async.AsyncRunnable.lambda$beginAsync$0(AsyncRunnable.java:123)
        com.mongodb.internal.async.AsyncRunnable.lambda$thenSupply$8(AsyncRunnable.java:216)
        com.mongodb.internal.async.AsyncSupplier.finish(AsyncSupplier.java:68)
        com.mongodb.internal.async.AsyncSupplier.lambda$onErrorIf$8(AsyncSupplier.java:171)
        com.mongodb.internal.async.AsyncSupplier.finish(AsyncSupplier.java:68)
        com.mongodb.internal.connection.InternalStreamConnection.sendAndReceiveAsync(InternalStreamConnection.java:398)
        com.mongodb.internal.connection.UsageTrackingInternalConnection.sendAndReceiveAsync(UsageTrackingInternalConnection.java:141)
        com.mongodb.internal.connection.DefaultConnectionPool$PooledConnection.sendAndReceiveAsync(DefaultConnectionPool.java:782)
        com.mongodb.internal.connection.CommandProtocolImpl.executeAsync(CommandProtocolImpl.java:66)
        com.mongodb.internal.connection.DefaultServer$DefaultServerProtocolExecutor.executeAsync(DefaultServer.java:230)
        com.mongodb.internal.connection.DefaultServerConnection.executeProtocolAsync(DefaultServerConnection.java:119)
        com.mongodb.internal.connection.DefaultServerConnection.commandAsync(DefaultServerConnection.java:100)
        com.mongodb.internal.connection.DefaultServer$AsyncOperationCountTrackingConnection.commandAsync(DefaultServer.java:368)
        com.mongodb.internal.operation.MixedBulkWriteOperation.executeCommandAsync(MixedBulkWriteOperation.java:444)
        com.mongodb.internal.operation.MixedBulkWriteOperation.lambda$executeBulkWriteBatchAsync$8(MixedBulkWriteOperation.java:331)
        com.mongodb.internal.async.function.AsyncCallbackLoop.run(AsyncCallbackLoop.java:56)
        com.mongodb.internal.operation.MixedBulkWriteOperation.executeBulkWriteBatchAsync(MixedBulkWriteOperation.java:365)
        com.mongodb.internal.operation.MixedBulkWriteOperation.lambda$executeAsync$4(MixedBulkWriteOperation.java:259)
        com.mongodb.internal.operation.AsyncOperationHelper.lambda$withAsyncSourceAndConnection$0(AsyncOperationHelper.java:121)
        com.mongodb.internal.operation.AsyncOperationHelper.lambda$withAsyncSuppliedResource$2(AsyncOperationHelper.java:138)
        com.mongodb.internal.async.function.AsyncCallbackSupplier.lambda$whenComplete$2(AsyncCallbackSupplier.java:97)
        com.mongodb.internal.operation.AsyncOperationHelper.lambda$withAsyncSuppliedResource$3(AsyncOperationHelper.java:139)
        com.mongodb.internal.connection.DefaultServer.lambda$getConnectionAsync$0(DefaultServer.java:127)
        com.mongodb.internal.async.ErrorHandlingResultCallback.onResult(ErrorHandlingResultCallback.java:47)
        com.mongodb.internal.connection.DefaultConnectionPool.lambda$getAsync$0(DefaultConnectionPool.java:219)
        com.mongodb.internal.connection.DefaultConnectionPool.lambda$getAsync$1(DefaultConnectionPool.java:242)
        com.mongodb.internal.connection.DefaultConnectionPool$Task.doComplete(DefaultConnectionPool.java:1433)
        com.mongodb.internal.connection.DefaultConnectionPool$Task.execute(DefaultConnectionPool.java:1419)
        com.mongodb.internal.connection.DefaultConnectionPool$AsyncWorkManager.lambda$workerRun$4(DefaultConnectionPool.java:1356)
        com.mongodb.internal.time.Timeout.lambda$run$9(Timeout.java:200)
        com.mongodb.internal.time.TimePoint.checkedCall(TimePoint.java:101)
        com.mongodb.internal.time.Timeout.call(Timeout.java:176)
        com.mongodb.internal.time.Timeout.run(Timeout.java:196)
        com.mongodb.internal.connection.DefaultConnectionPool$AsyncWorkManager.workerRun(DefaultConnectionPool.java:1354)
        com.mongodb.internal.connection.DefaultConnectionPool$AsyncWorkManager.runAndLogUncaught(DefaultConnectionPool.java:1383)
        com.mongodb.internal.connection.DefaultConnectionPool$AsyncWorkManager.lambda$initUnlessClosed$1(DefaultConnectionPool.java:1324)
        java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:545)
        java.base/java.util.concurrent.FutureTask.run(FutureTask.java:328)
        java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
        java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
        java.base/java.lang.Thread.run(Thread.java:1474)
```