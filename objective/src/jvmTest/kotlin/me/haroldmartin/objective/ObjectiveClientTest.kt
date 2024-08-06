package me.haroldmartin.objective

import kotlinx.coroutines.test.runTest
import java.lang.System
import kotlin.test.Test

class ObjectiveClientTest {
    @Test
    fun shouldListIndexes() =
        runTest {
            val client = ObjectiveClient(System.getenv("OBJECTIVE_KEY"))
            val indexes = client.getIndexes()
            println(indexes)
            assert(indexes.isNotEmpty())
        }

    @Test
    fun shouldListObjects() =
        runTest {
            val client = ObjectiveClient(System.getenv("OBJECTIVE_KEY"))
            val objects = client.getObjects(includeObject = true, limit = 100)
            println(objects)
            assert(objects.isNotEmpty())
            println(objects.first().objectData.toString())
        }
}
