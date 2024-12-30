package online.example.networking

import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DataTransformer {
    // Transforms input flow with multiple operators
    fun transformData(input: Flow<Int>) = input
        .map { it * 2 }        // Double each number
        .filter { it > 5 }     // Keep only numbers > 5
        .take(3)               // Take first 3 items

    // Combines two flows
    fun combineData(flow1: Flow<String>, flow2: Flow<Int>) =
        flow1.zip(flow2) { str, num ->
            "$str: $num"
        }
}

class TestingFlowOperators {
    @Test
    fun `test flow transformation operators`() = runTest {
        val input = flowOf(1, 2, 3, 4, 5)
        val transformer = DataTransformer()

        transformer.transformData(input).test {
            assertEquals(6, awaitItem())  // 3 * 2 = 6
            assertEquals(8, awaitItem())  // 4 * 2 = 8
            assertEquals(10, awaitItem()) // 5 * 2 = 10
            awaitComplete()              // Flow completes after 3 items
        }
    }

    @Test
    fun `test flow combination`() = runTest {
        val transformer = DataTransformer()
        val flow1 = flowOf("A", "B", "C")
        val flow2 = flowOf(1, 2, 3)

        transformer.combineData(flow1, flow2).test {
            assertEquals("A: 1", awaitItem())
            assertEquals("B: 2", awaitItem())
            assertEquals("C: 3", awaitItem())
            awaitComplete()
        }
    }
}