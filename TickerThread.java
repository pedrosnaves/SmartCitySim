package br.pedro.smartgrid;

/** Thread simples que avança a simulação a cada segundo. */
public class TickerThread extends Thread {

    private volatile boolean running = true;
    private final SimulationManager manager = SimulationManager.getInstance();

    public TickerThread() { super("TickerThread"); }

    @Override
    public void run() {
        while (running) {
            manager.simulateTick();
            try { sleep(1000); } catch (InterruptedException ignored) { running = false; }
        }
    }

    public void stopTicker() { running = false; interrupt(); }
}
