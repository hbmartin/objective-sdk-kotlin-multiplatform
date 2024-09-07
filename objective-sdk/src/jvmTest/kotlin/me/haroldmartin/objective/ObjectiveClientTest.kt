package me.haroldmartin.objective

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonObject
import java.lang.System
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ObjectiveClientTest {
    @Test
    fun shouldListIndexes() =
        runTest {
            val client = ObjectiveClient(System.getenv("OBJECTIVE_KEY"))
            val indexes = client.getIndexes()
            assert(indexes.isNotEmpty())
        }

    @Test
    fun shouldListObjects() =
        runTest {
            val client = ObjectiveClient(System.getenv("OBJECTIVE_KEY"))
            val objects = client.getObjects<JsonObject>(includeObject = true, limit = 10)
            assertEquals(10, objects.size)
        }

    @Test
    fun shouldThrowOnUrlEncodableIdsWhenConfigured() =
        runTest {
            val client = ObjectiveClient(System.getenv("OBJECTIVE_KEY"), autoUrlEncodeIds = false)
            assertFailsWith(UnencodedIdException::class) {
                client.getObject<JsonObject>("#test")
            }
        }
}
