package online.example.networking

import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class ErrorProne {
    fun errorFlow() = flow {
        emit("Start")
        throw IllegalStateException("Oops!")
    }

    fun timeoutFlow() = flow {
        emit("Start")
        delay(2000) // Long delay
        emit("End")
    }
}

class TestingErrorScenarios {
    @Test
    fun `test error handling`() = runTest {
        val errorProne = ErrorProne()

        errorProne.errorFlow().test {
            assertEquals("Start", awaitItem())         // First emission
            val error = awaitError()                  // Wait for error
            assertTrue(error is IllegalStateException)   // Verify error type
            assertEquals("Oops!", error.message)      // Verify error message
        }
    }

    @Test
    fun `test timeout handling`() = runTest {
        val errorProne = ErrorProne()

        errorProne.timeoutFlow()
            .timeout(1.seconds)  // Set timeout
            .test {
                assertEquals("Start", awaitItem())
                assertTrue(awaitError() is TimeoutCancellationException)
            }
    }
}