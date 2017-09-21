/**
 * Interface for classes, that manage sleep time for request sending and serve as a callback to report finish of
 * request sending.
 */
public interface IRequestSenderUtil {

    /**
     * Callback method, that request performing routines should call to notify, that request was successfully sent.
     * @param responseTime A time that it took to launch the request and to receive the response, in nanoseconds.
     */
    void notifyRequestSent(long responseTime, boolean success);

    /**
     * @return Time that request performing routine should sleep before sending the request, in nanoseconds.
     */
    long getEstimatedSleepTime();

    /**
     * @return Number of requests, which were successfully sent.
     */
    public int getRequestsSuccessfullySent();

}
