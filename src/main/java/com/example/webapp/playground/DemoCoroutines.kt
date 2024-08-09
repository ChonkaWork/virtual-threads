import kotlinx.coroutines.*
import java.time.Duration
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() = runBlocking {
    println("~~~~~ Heavy Load Comparison")
    val (virtualThreadsTime, platformThreadsTime) = heavyLoadComparison()

    println("~~~~~ Summary")
    println("Coroutines heavy load time: $virtualThreadsTime ms, " +
            "Platform threads heavy load time: $platformThreadsTime ms")
}

suspend fun heavyLoadComparison(): Pair<Long, Long> {
    val taskCount = 1000

    println("Running with coroutines...")
    var start = Instant.now()
    runBlocking {
        val jobs = List(taskCount) {
            launch {
                simulateHeavyTaskCoroutine(it)
            }
        }
        jobs.forEach { it.join() }
    }
    var end = Instant.now()
    val coroutinesTime = Duration.between(start, end).toMillis()
    println("Coroutines time: $coroutinesTime ms")

    println("Running with platform threads...")
    start = Instant.now()
    val executor = Executors.newFixedThreadPool(100)
    val futures = (0 until taskCount).map { i ->
        executor.submit {
            simulateHeavyTaskThread(i)
        }
    }
    futures.forEach { it.get() }
    executor.shutdown()
    executor.awaitTermination(1, TimeUnit.HOURS)
    end = Instant.now()
    val platformThreadsTime = Duration.between(start, end).toMillis()
    println("Platform threads time: $platformThreadsTime ms")

    return Pair(coroutinesTime, platformThreadsTime)
}

suspend fun simulateHeavyTaskCoroutine(taskId: Int) {
    println("Coroutine - Task $taskId started")
    delay(1000)
    println("Coroutine - Task $taskId completed")
}

fun simulateHeavyTaskThread(taskId: Int) {
    println("Thread - Task $taskId started")
    try {
        Thread.sleep(1000)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
    println("Thread - Task $taskId completed")
}
