package homemade.game.loop

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlin.reflect.KClass

interface EventPoster<EventType> {
    fun post(event: EventType)
}

/**
 * One loop per session. On game over, loop goes into the 'stopping' state.
 */
open class GameLoopBase<EventType>(subclasses: List<KClass<out EventType>>) : EventPoster<EventType>
        where EventType : Any {

    internal val channel = Channel<EventType>(Channel.UNLIMITED)

    val handlers: Map<KClass<out EventType>, ArrayList<GameEventHandler<EventType>>>

    @OptIn(ExperimentalCoroutinesApi::class)
    val isEmpty: Boolean
        get() = channel.isEmpty

    init {
        handlers = subclasses.associateBy({ it }, { ArrayList() })
    }

    protected fun propagateEvent(event: EventType) {
        for (handler in handlers[event::class]!!) {
            handler.handle(event)
        }
    }

    override fun post(event: EventType) {
        val result = channel.trySend(event)
        if (result.isFailure && !result.isClosed) {
            System.err.println(result.exceptionOrNull()?.message ?: "posting failed, no throwable $event")
        }
    }
}
