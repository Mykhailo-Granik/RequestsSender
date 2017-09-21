import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class represents key abstraction for running the requests.
 */
public class ThreadManager {

    private static final int THREADS_NUMBER = 10;

    private long startTime;
    private PropertiesManager propertiesManager;

    public ThreadManager(PropertiesManager propertiesManager) {
        this.propertiesManager = propertiesManager;
    }

    public void execute() {

        int requestsNumber = propertiesManager.getRequestsNumber();
        String url = propertiesManager.getUrl();
        // Create an Executor service to manage the threads that will send requests in parallel.
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_NUMBER);
        RequestSenderProvider requestSenderProvider = new RequestSenderProvider();
        startTime = System.nanoTime();
        // Supply the executor with requests sending Runnables.
        IRequestSenderUtil requestSenderUtil = new RequestSenderUtil(THREADS_NUMBER, requestsNumber, startTime);
        Runnable requestSenderRunnable = requestSenderProvider.getRequestSenderRunnable(url, requestSenderUtil);
        for (int i = 0; i < requestsNumber; ++i) {
            executorService.submit(requestSenderRunnable);
        }
        // Shut down the executor service.
        shutDownExecutorService(executorService);
        long endTime = System.nanoTime();
        // Output statistics.
        System.out.println("Sent total of " + requestsNumber + " requests");
        int requestsSuccessfullySent = requestSenderUtil.getRequestsSuccessfullySent();
        System.out.println("Successfully sent " + requestsSuccessfullySent + " requests");
        System.out.println("Unsuccessfully sent " + (requestsNumber - requestsSuccessfullySent) + " requests");
        System.out.println("Total time: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) / 1000.0 + " seconds");
        System.out.println("******************************************");
    }

    /**
     * Gracefully shutdowns executor service
     * @param executorService ExecutorService object to be shut down.
     */
    private void shutDownExecutorService(ExecutorService executorService) {
        try {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            if (!executorService.isTerminated()) {
                executorService.shutdownNow();
            }
        }
    }

}
