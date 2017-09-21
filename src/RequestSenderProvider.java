import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * The goal of the class is to provide a Runnable that sends one request and waits for the answer from the server.
 */
public class RequestSenderProvider {

    private static final int CODE_OK = 200;
    private static final String REQUEST_METHOD = "GET";

    /**
     * @param url The URL of the HTTP server to send requests to.
     * @param requestSenderUtil Object that provides the number of nanoseconds to sleep before launching the
     *                                  request; also its notifyRequestSent() callback method should be called when
     *                                  response was successfully received or launching of the request failed.
     * @return A Runnable object that sends one request and waits for the answer from the server.
     */
    public Runnable getRequestSenderRunnable(String url, IRequestSenderUtil requestSenderUtil) {
        return () -> {
            // Send a thread to sleep.
            long sleepTime = requestSenderUtil.getEstimatedSleepTime();
            try {
                TimeUnit.NANOSECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println("Unable to send a thread to a sleep mode");
            }
            // Perform request, measure time it takes to get a response.
            long startTime = System.nanoTime();
            boolean success = true;
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                int code = connection.getResponseCode();
                if (code != CODE_OK) {
                    success = false;
                }
            } catch (IOException e) {
                success = false;
            } finally {
                long finishTime = System.nanoTime();
                // Notify a callback object and close connection.
                requestSenderUtil.notifyRequestSent(finishTime - startTime, success);
                connection.disconnect();
            }
        };
    }

}
