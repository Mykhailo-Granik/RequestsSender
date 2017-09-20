import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadManager {

    private static final int THREADS_NUMBER = 100;

    private long startTime;
    private PropertiesManager propertiesManager;

    public ThreadManager(PropertiesManager propertiesManager) {
        this.propertiesManager = propertiesManager;
    }

    public void execute() {

        int requestsNumber = propertiesManager.getRequestsNumber();
        String url = propertiesManager.getUrl();
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_NUMBER);
        RequestSenderProvider requestSenderProvider = null;
        requestSenderProvider = new RequestSenderProvider();
        startTime = System.nanoTime();
        IRequestSenderSleepManager requestSenderSleepManager = new RequestSenderSleepManager(THREADS_NUMBER, requestsNumber, startTime);
        Runnable requestSenderRunnable = requestSenderProvider.getRequestSenderRunnable(url, requestSenderSleepManager);
        for (int i = 0; i < requestsNumber; ++i) {
            executorService.submit(requestSenderRunnable);
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.println("Performed " + requestsNumber + " requests in " +
                TimeUnit.NANOSECONDS.toMillis(endTime - startTime) / 1000.0 + " seconds");
    }

}
