package drawapptutorial.com.example.thatwaseasy_twe;

import android.os.AsyncTask;

public class TimerTask extends AsyncTask<Double, Double, Double> {
    private boolean running = true;
    private long startTime;
    private TimerCallback callback;

    public TimerTask(TimerCallback callback) {
        this.callback = callback;
    }

    public void finish() {
        running = false;
    }

    public double getTime() {
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }

    @Override
    protected Double doInBackground(Double... initialTime) {
        if (initialTime.length == 0)
            startTime = System.currentTimeMillis();
        else if (initialTime.length == 1)
            startTime = System.currentTimeMillis() - (long)(initialTime[0] * 1000);
        else
            throw new IllegalArgumentException("TimerTask must only takes zero or one double parameters");

        while (running) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
            }

            publishProgress(getTime());
        }
        return getTime();
    }

    @Override
    protected void onProgressUpdate(Double... progress) {
        this.callback.update(progress[0]);
    }
}
