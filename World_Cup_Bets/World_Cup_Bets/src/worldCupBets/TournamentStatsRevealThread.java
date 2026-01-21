package worldCupBets;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class TournamentStatsRevealThread extends Thread {
    private final DefaultTableModel model;
    private final Object[][] data;
    private volatile boolean running = true;

    public TournamentStatsRevealThread(DefaultTableModel model, Object[][] data) {
        super("TournamentStatsRevealThread");
        this.model = model;
        this.data = data;
        setDaemon(true);
    }

    public void stopAnimation() {
        running = false;
        interrupt();
    }

    @Override
    public void run() {
        for (int i = 0; i < data.length && running; i++) {
            final Object[] row = data[i];
            SwingUtilities.invokeLater(() -> model.addRow(row)); // Update GUI 
            sleepSafe(1000); // Pause between reveals 1 second
        }
    }

    private void sleepSafe(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
