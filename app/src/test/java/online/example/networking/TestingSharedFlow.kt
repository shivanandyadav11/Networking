package online.example.networking

import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test


class EventBus {
    private val _events = MutableSharedFlow<String>(replay = 0)
    val events = _events.asSharedFlow()

    suspend fun emitEvent(event: String) {
        _events.emit(event)
    }
}

class TestingSharedFlow {
    @Test
    fun `test shared flow events`() = runTest {
        val eventBus = EventBus()

        eventBus.events.test {
            // No initial value emitted (replay = 0)
            expectNoEvents() // Verify no events initially

            eventBus.emitEvent("First")
            assertEquals("First", awaitItem())

            eventBus.emitEvent("Second")
            assertEquals("Second", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}