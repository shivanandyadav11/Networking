package online.example.networking

import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NumberGenerator {
    fun numbers() = flow {
        emit(1)
        delay(100)
        emit(2)
        delay(100)
        emit(3)
    }
}

class BasicFlowTesting {

    @Test
    fun `test basic number flow`() = runTest {
        val generator = NumberGenerator()

        generator.numbers().test {
            assertEquals(1, awaitItem()) // Wait for first emission
            assertEquals(2, awaitItem()) // Wait for second emission
            assertEquals(3, awaitItem()) // Wait for third emission
            awaitComplete()             // Ensure Flow completes
        }
    }
}