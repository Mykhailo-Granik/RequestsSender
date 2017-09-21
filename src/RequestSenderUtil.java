import java.util.concurrent.TimeUnit;

public class RequestSenderUtil implements IRequestSenderUtil {

    private static final long TOTAL_TIME = TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS);

    private int threadNumber;
    private int totalRequests;
    private long startTime;
    private long totalResponseTime;
    private int requestsSent;
    private int requestsSuccessfullySent;

    public RequestSenderUtil(int threadNumber, int totalRequests, long startTime) {
        this.threadNumber = threadNumber;
        this.totalRequests = totalRequests;
        this.startTime = startTime;
    }

    @Override
    public synchronized void notifyRequestSent(long responseTime, boolean success) {
        requestsSent++;
        if (success) {
            requestsSuccessfullySent++;
        }
        totalResponseTime += responseTime;
    }

    @Override
    public synchronized long getEstimatedSleepTime() {
        // If there is no information about sent requests - we cannot estimate a sleep time, so it is better to not
        // pause a thread at all.
        if (requestsSent == 0) {
            return 0;
        }
        // Estimate a number of requests per thread as a number of unfinished requests divided by number of threads
        // (pessimistically rounded up).
        int requestsLeft = totalRequests - requestsSent;
        int requestsPerThread = (requestsLeft + threadNumber - 1) / threadNumber;
        // Calculate the sleep time based on a formula:
        // timeLeft = requestsPerThread * (averageResponseTime + sleepTime)
        long averageResponseTime = totalResponseTime / requestsSent;
        long currentTime = System.nanoTime();
        long timeLeft = TOTAL_TIME - (currentTime - startTime);
        return Math.max(0, (timeLeft - averageResponseTime * requestsPerThread) / requestsPerThread);
    }

    @Override
    public int getRequestsSuccessfullySent() {
        return requestsSuccessfullySent;
    }
}
