package online.example.networking

import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class Counter {
    private val _count = MutableStateFlow(0)
    val count = _count.asStateFlow()

    fun increment() {
        _count.value++
    }

    fun decrement() {
        _count.value--
    }
}

class TestingStateFlow {
    @Test
    fun `test counter state flow`() = runTest {
        val counter = Counter()

        counter.count.test {
            // StateFlow always emits initial value
            assertEquals(0, awaitItem())

            counter.increment()
            assertEquals(1, awaitItem()) // New state after increment

            counter.increment()
            assertEquals(2, awaitItem()) // New state after second increment

            counter.decrement()
            assertEquals(1, awaitItem()) // New state after decrement

            cancelAndIgnoreRemainingEvents() // Important for StateFlow!
        }
    }
}