import java.util.HashMap;

/**
 * Created by Artur on 2/23/17.
 */
public class EndPoint {
    private int latencyDataCenter;
    private HashMap<Integer, Integer> cacheServersLatency;
    private HashMap<Integer, Integer> videoRequests;

    public EndPoint(int latency, int caches) {
        this.latencyDataCenter = latency;
        cacheServersLatency = new HashMap<>(caches);
        videoRequests = new HashMap<>(caches);
    }

    public void addLatencyToCache(int cache, int latency) {
        cacheServersLatency.put(cache, latency);
    }

    public void addRequestToVideo(int video, int request) {
        videoRequests.put(video, request);
    }

    public int getLatencyDataCenter() {
        return latencyDataCenter;
    }

    public HashMap<Integer, Integer> getCacheServersLatency() {
        return cacheServersLatency;
    }

    public HashMap<Integer, Integer> getVideoRequests() {
        return videoRequests;
    }
}
