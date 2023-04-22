package homemade.game.loop

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class BackgroundLoop : GameLoopBase<GameEvent>(GameEvent::class.sealedSubclasses) {
    private val context = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private var maxCountdown = 40

    init {
        thread {
            runBlocking {
                launch(context) {
                    for (event in channel) {
                        if (event is GameOver) {
                            if (event.countdown >= maxCountdown) {
                                continue
                            } else {
                                maxCountdown = event.countdown
                            }
                        }
                        propagateEvent(event)
                    }
                }
            }
        }
    }

    inline fun <reified EventType> subscribe(handler: GameEventHandler<GameEvent>) where EventType : GameEvent {
        handlers[EventType::class]!!.add(handler)
    }
}

class UILoop : GameLoopBase<UIEvent>(UIEvent::class.sealedSubclasses) {

    fun tryPropagateEvents() {
        var event = channel.tryReceive()
        while (event.isSuccess) {
            propagateEvent(event.getOrThrow())
            event = channel.tryReceive()
        }
    }

    inline fun <reified EventType> subscribe(handler: GameEventHandler<UIEvent>) where EventType : UIEvent {
        handlers[EventType::class]!!.add(handler)
    }
}

/**
 * Twin channels are necessary for communicating with the UI thread (although we could have just used polling)
 */
class GameLoop {

    val model = BackgroundLoop()
    val ui = UILoop()

    private val delayedEvents = ArrayList<DelayedEvent>()

    init {
        ui.subscribe<ShutDown>(object : GameEventHandler<UIEvent> {
            override fun handle(event: UIEvent) {
                ui.channel.close()
                model.channel.close()
            }
        })

        model.subscribe<TimeElapsed>(object : GameEventHandler<GameEvent> {
            override fun handle(event: GameEvent) {
                if (event is TimeElapsed) {
                    for (knownEvent in delayedEvents) {
                        knownEvent.delayMs -= event.diffMs
                        if (knownEvent.delayMs <= 0) {
                            model.post(knownEvent.event)
                        }
                    }
                    delayedEvents.retainAll { it.delayMs > 0 }
                }
            }
        })
        model.subscribe<DelayedEvent>(object : GameEventHandler<GameEvent> {
            override fun handle(event: GameEvent) {
                if (event is DelayedEvent) {
                    delayedEvents.add(event)
                }
            }
        })
    }
}
