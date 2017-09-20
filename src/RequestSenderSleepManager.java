import java.util.concurrent.TimeUnit;

public class RequestSenderSleepManager implements IRequestSenderSleepManager {

    private static final long TOTAL_TIME = TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS);

    private int threadNumber;
    private int totalRequests;
    private long startTime;
    private long totalResponseTime;
    private int requestsSent;

    public RequestSenderSleepManager(int threadNumber, int totalRequests, long startTime) {
        this.threadNumber = threadNumber;
        this.totalRequests = totalRequests;
        this.startTime = startTime;
    }

    @Override
    public synchronized void notifyRequestSent(long responseTime) {
        requestsSent++;
        totalResponseTime += responseTime;
    }

    @Override
    public synchronized long getEstimatedSleepTime() {
        if (requestsSent == 0) {
            return 0;
        }
        int requestsLeft = totalRequests - requestsSent;
        int requestsPerThread = (requestsLeft + threadNumber - 1) / threadNumber;
        long averageResponseTime = totalResponseTime / requestsSent;
        long currentTime = System.nanoTime();
        long timeLeft = TOTAL_TIME - (currentTime - startTime);
        return Math.max(0, (timeLeft - averageResponseTime * requestsPerThread) / requestsPerThread);
    }
}
