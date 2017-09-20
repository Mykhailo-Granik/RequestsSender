import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class RequestSenderProvider {

    public Runnable getRequestSenderRunnable(String url, IRequestSenderSleepManager requestSenderSleepManager) {
        return () -> {
            long sleepTime = requestSenderSleepManager.getEstimatedSleepTime();
            try {
                TimeUnit.NANOSECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println("Unable to make send a thread to a sleep mode");
            }
            try {
                long startTime = System.nanoTime();
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.getResponseCode();
                long finishTime = System.nanoTime();
                requestSenderSleepManager.notifyRequestSent(finishTime - startTime);
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

}
