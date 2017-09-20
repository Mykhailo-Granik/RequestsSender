public interface IRequestSenderSleepManager {

    void notifyRequestSent(long responseTime);

    long getEstimatedSleepTime();

}
