package worldCupBets;

import java.awt.Label;
import java.util.GregorianCalendar;

public class MatchCountdownTimer implements Runnable {
    private final Label targetLabel;
    private volatile boolean running = true;
    private volatile GregorianCalendar targetDate;

    public MatchCountdownTimer(Label targetLabel, GregorianCalendar targetDate) {
        this.targetLabel = targetLabel;
        this.targetDate = targetDate;
    }

    public void setTargetDate(GregorianCalendar newTargetDate) {
        this.targetDate = newTargetDate;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            GregorianCalendar td = targetDate;
            if (td == null) {
                targetLabel.setText("Starts in: -");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                continue;
            }

            long nowMs = System.currentTimeMillis();
            long targetMs = td.getTimeInMillis();
            long diffMs = targetMs - nowMs;

            if (diffMs <= 0) {
                targetLabel.setText("Starts in: 0d 0h 0m 0s");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                continue;
            }

            long totalSeconds = diffMs / 1000;
            long days = totalSeconds / 86400;
            long hours = (totalSeconds % 86400) / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            long seconds = totalSeconds % 60;

            targetLabel.setText("Starts in: " + days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }
}
