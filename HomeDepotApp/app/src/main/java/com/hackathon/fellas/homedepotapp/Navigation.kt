package com.hackathon.fellas.homedepotapp

import java.lang.Math.exp
import java.util.*

class NavigationMap {
    class Signal(var strength: Short, var timestamp: Long, var signalFilt: Double, var inRangeCount: Int = 0)

    val nodes = arrayListOf(
            Node(0.0, 0.0, arrayListOf(Connection(1, 90.0))),
            Node(0.0, 3.0, arrayListOf(Connection(0, -90.0), Connection(2, 90.0))),
            Node(0.0, 6.0, arrayListOf(Connection(1, -90.0)))
    )

    class Node(val x: Double, val y: Double, val connections: ArrayList<Connection>, var lastSignal: Signal? = null)
    class Connection(val to: Int, val angle: Double)
    class Path(var nodes: ArrayList<Int>)

    val queuedNavigationInstructions = LinkedList<String>()

    // The index of the next node in the path
    var pathIdx = -1

    var currentPath: Path? = null

    fun updateSignalReading(beaconId: Int, strength: Short) {
        val timeConstant = 1.5

        if (beaconId < 0 || beaconId > nodes.size) {
            throw IllegalArgumentException("Beacon $beaconId does not exist!")
        }

        if (nodes[beaconId].lastSignal != null) {
            val dt = (System.currentTimeMillis() - nodes[beaconId].lastSignal!!.timestamp) / 1000.0
            val k = exp(-dt / timeConstant)
            val signalFilt = k * nodes[beaconId].lastSignal!!.signalFilt + (1 - k) * strength
            nodes[beaconId].lastSignal = Signal(strength, System.currentTimeMillis(), signalFilt, 0)
        } else {
            nodes[beaconId].lastSignal = Signal(strength, System.currentTimeMillis(), strength.toDouble(), 0)
        }

        if (currentPath != null && pathIdx != -1) {
            val pathNext = currentPath!!.nodes[pathIdx]

            if (pathNext == beaconId) {
                if (nodes[beaconId].lastSignal!!.signalFilt > -65) {
                    nodes[beaconId].lastSignal!!.inRangeCount++
                } else {
                    nodes[beaconId].lastSignal!!.inRangeCount = 0
                }

                if (nodes[beaconId].lastSignal!!.inRangeCount > 5) {
                    nextNavigationInstruction()
                }
            }
        }
    }

    fun getNextConnection(): Connection? {
        if (pathIdx >= currentPath!!.nodes.size - 1) {
            return null
        }
        for (connection in nodes[currentPath!!.nodes[pathIdx]].connections) {
            if (connection.to == currentPath!!.nodes[pathIdx + 1]) {
                return connection
            }
        }
        return null
    }

    fun getPreviousConnection(): Connection? {
        if (pathIdx <= 0) {
            return null
        }
        for (connection in nodes[currentPath!!.nodes[pathIdx]].connections) {
            if (connection.to == currentPath!!.nodes[pathIdx]) {
                return connection
            }
        }
        return null
    }

    fun nextNavigationInstruction() {
        if (pathIdx < (currentPath?.nodes?.size ?: 0) - 1) {
            val toNext = getNextConnection()
            val fromLast = getPreviousConnection()
            var angleDiff = (toNext?.angle ?: return) - (fromLast?.angle ?: return)
            if (angleDiff > 360.0) {
                angleDiff -= 360.0
            } else if (angleDiff < -360.0) {
                angleDiff += 360.0
            }
            val distance = Math.pow(nodes[pathIdx + 1].x - nodes[pathIdx].x, 2.0) + Math.pow(nodes[pathIdx + 1].y - nodes[pathIdx].y, 2.0)
            val directionString = if (angleDiff > 0.0) "left" else "right"
            queuedNavigationInstructions.addFirst("Turn $directionString and continue for $distance meters")
            pathIdx++
        } else {
            pathIdx = -1
            queuedNavigationInstructions.addFirst("You have arrived at your destination.")
            return
        }
    }
}