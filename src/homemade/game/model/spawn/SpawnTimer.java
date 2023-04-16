package homemade.game.model.spawn;

public interface SpawnTimer {
    void toggleSpawnPause();

    void stop();

    void timeElapsed(int time);

    /**
     * Can be used to handle pause calls in turn-based mode until we need them for something
     */
    class EmptyTimer implements SpawnTimer {
        @Override
        public void toggleSpawnPause() {

        }

        @Override
        public void stop() {

        }

        @Override
        public void timeElapsed(int time) {
            
        }
    }
}
