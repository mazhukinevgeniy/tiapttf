package homemade.game.loop

interface GameEventHandler<EventType> {
    fun handle(event: EventType)
}
