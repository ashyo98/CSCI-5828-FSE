package io.collective

import io.collective.SimpleAgedCache.ExpirableEntry

class SimpleAgedKache {
    var size = 0
    var elements = arrayOfNulls<ExpirableEntry?>(10)
    var clock: java.time.Clock? = null

    constructor(clock: java.time.Clock?) {
        this.clock = clock
    }

    constructor()

    fun put(key: String?, value: String?, retentionInMillis: Long) {
        val newEntry = ExpirableEntry()
        newEntry.key = key
        newEntry.value = value
        newEntry.retention = retentionInMillis
        if (clock != null) {
            newEntry.baseTime = clock!!.millis()
        }
        elements[size] = newEntry
        size += 1
    }

    fun isEmpty(): Boolean {
        if (size() == 0) {
            return true
        }
        return false
    }

    fun size(): Int {
        if (clock == null) {
            return size
        }
        val currTime: Long = clock!!.instant().toEpochMilli()
        var count = 0
        for (index in 0 until size) {
            val baseTime = elements[index]!!.baseTime
            val retentionTime = elements[index]!!.retention
            if (baseTime + retentionTime < currTime) {
                elements[index] = null
                count++
            }
        }
        return size - count
    }

    operator fun get(key: Any): Any? {
        for (index in 0 until size) {
            if (elements[index] != null && elements[index]!!.key === key) {
                return elements[index]!!.value
            }
        }
        return null
    }

    class ExpirableEntry {
        var key: String? = null
        var value: String? = null
        var retention: Long = 0
        var baseTime: Long = 0
    }
}