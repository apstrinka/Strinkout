package net.strinka.strinkout

import android.os.SystemClock
import android.os.Handler
import android.os.Message
import android.os.CountDownTimer
import android.util.Log
import kotlin.math.max


class PausableTimer {
    private val MSG = 1
    var running = false
    private val interval : Long
    private val endAt : Long
    private var elapsed : Long = 0
    private var startTime : Long = 0
    private var tickHandler : Handler
    private var eventHandler : Handler
    private var events : MutableList<Pair<Long, () -> Unit>>

    constructor(interval: Long, endAt: Long, onTick: (millisElapsed: Long) -> Unit, onFinish: () -> Unit){
        this.interval = interval
        this.endAt = endAt
        this.events = mutableListOf()

        tickHandler = object : Handler(){
            override fun handleMessage(msg: Message){
                synchronized(this@PausableTimer) {
                    if (!running)
                        return

                    val millisElapsed = getCurrentElapsed()

                    if (millisElapsed >= endAt){
                        running = false
                        onFinish()
                        return
                    }

                    val lastTickStart = SystemClock.elapsedRealtime()
                    onTick(millisElapsed)
                    // take into account user's onTick taking time to execute
                    val lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart
                    var delay: Long
                    val millisLeft = endAt - millisElapsed
                    if (millisLeft < interval){
                        delay = millisLeft - lastTickDuration
                        if (delay < 0) {
                            delay = 0
                        }
                    } else {
                        delay = interval - lastTickDuration
                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) {
                            delay += interval
                        }
                    }
                    sendMessageDelayed(obtainMessage(MSG), delay)
                }
            }
        }

        eventHandler = object : Handler(){
            override fun handleMessage(msg: Message){
                synchronized(this@PausableTimer) {
                    if (!running)
                        return

                    val index = msg.what
                    val event = events.get(index).second
                    event()
                }
            }
        }
    }

    fun getCurrentElapsed(): Long{
        if (running)
            return elapsed + SystemClock.elapsedRealtime() - startTime
        else
            return elapsed
    }

    @Synchronized
    fun start(){
        if (!running) {
            running = true
            startTime = SystemClock.elapsedRealtime()
            tickHandler.sendMessage(tickHandler.obtainMessage(MSG))
            for ((index,value) in events.withIndex()){
                val time = value.first
                if (time >= elapsed) {
                    eventHandler.sendMessageDelayed(eventHandler.obtainMessage(index), time - elapsed)
                }
            }
        }
    }

    @Synchronized
    fun pause(){
        if (running) {
            elapsed = getCurrentElapsed()
            running = false
            tickHandler.removeMessages(MSG)
            for (i in events.indices) {
                eventHandler.removeMessages(i)
            }
        }
    }

    @Synchronized
    fun addEvent(time: Long, event: () -> Unit){
        val currentTime = getCurrentElapsed()
        if (time >= currentTime && time < endAt){
            if (running) {
                eventHandler.sendMessageDelayed(eventHandler.obtainMessage(events.size), time - currentTime)
            }
            events.add(Pair(time, event))
        }
    }
}