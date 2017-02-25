import java.util.*;

/**
 * Created by Artur on 2/23/17.
 */
public class DistributionTwo implements DistributionI {
    private int numberOfVideos;
    private int numberOfEndpoints;
    private int numberOfRequestDescriptions;
    private int numberOfCacheServers;
    private int capacityOfCacheServers;
    private int[] videoSizes;
    private boolean[] videoStored;
    private int[] cacheVolume;
    private HashMap<Integer, Set<Integer>> cacheVideo;
    private ArrayList<EndPoint> endPoints;
    private Map<Integer, Double> videoValue;

    @Override
    public void parser(ParserI parser) {
        numberOfVideos = parser.getNumberOfVideos();
        numberOfEndpoints = parser.getNumberOfEndpoint();
        numberOfCacheServers = parser.getNumberOfCacheServers();
        numberOfRequestDescriptions = parser.getNumberOfRequestDescriptions();
        videoSizes = parser.getVideoSizes();
        capacityOfCacheServers = parser.getCapacityOfCacheServer();
        endPoints = parser.getEndpoints();
        cacheVolume = new int[numberOfCacheServers];
        for (int i = 0; i < numberOfCacheServers; i++)
            cacheVolume[i] = capacityOfCacheServers;
        videoStored = new boolean[numberOfVideos];
        for (int i = 0; i < numberOfVideos; i++)
            videoStored[i] = false;
    }

    @Override
    public void compute() {

        cacheVideo = new HashMap<>();
        videoValue = new HashMap<>();

        for (EndPoint endPoint : endPoints) {
            Map<Integer, Integer> cacheLatency = endPoint.getCacheServersLatency();
            int latencies = 1;
            for (Map.Entry<Integer, Integer> entry : cacheLatency.entrySet()) {
                latencies *= entry.getValue();
            }
            Map<Integer, Integer> videoRequests = endPoint.getVideoRequests();
            int dbLatency = endPoint.getLatencyDataCenter();
            for (Map.Entry<Integer, Integer> video: videoRequests.entrySet()) {
                int req = video.getValue();
                Double value = 1.0 * req * dbLatency * latencies / videoSizes[video.getKey()];
                if (!videoValue.containsKey(video.getKey()))
                    videoValue.put(video.getKey(), value);
                else
                    videoValue.put(video.getKey(), videoValue.get(video.getKey()) + value);
            }

        }

        videoValue = sortMapByValueDouble(videoValue, true);
        /*
        for (Map.Entry<Integer, Double> entry : videoValue.entrySet()) {
            for (EndPoint endPoint : endPoints) {
                Map<Integer, Integer> cacheLatency = endPoint.getCacheServersLatency();
                for (Map.Entry<Integer, Integer> cache : cacheLatency.entrySet()) {
                    if (videoSizes[entry.getKey()] <= cacheVolume[cache.getKey()]) {
                        cacheVolume[cache.getKey()] -= videoSizes[entry.getKey()];
                        if (!cacheVideo.containsKey(cache.getKey()))
                            cacheVideo.put(cache.getKey(), new HashSet<>());
                        cacheVideo.get(cache.getKey()).add(entry.getKey());
                    }

                }
            }
        }
        */


        for (EndPoint endPoint : endPoints) {
            Map<Integer, Integer> cacheLatency = endPoint.getCacheServersLatency();
            int dbLatency = endPoint.getLatencyDataCenter();
            cacheLatency.put(Integer.MAX_VALUE, dbLatency);
            cacheLatency = sortMapByValue(cacheLatency, false);

            Map<Integer, Integer> videoRequests = endPoint.getVideoRequests();
            videoRequests = sortMapByValue(videoRequests, true);

            for (Map.Entry<Integer, Integer> video : videoRequests.entrySet()) {
                int size = videoSizes[video.getKey()];
                //boolean put = false;
                for (Map.Entry<Integer, Integer> cacheServer: cacheLatency.entrySet()) {
                    if (!videoStored[video.getKey()] &&cacheServer.getKey() != Integer.MAX_VALUE && size <= cacheVolume[cacheServer.getKey()]) {
                        cacheVolume[cacheServer.getKey()] -= size;
                        if (!cacheVideo.containsKey(cacheServer.getKey()))
                            cacheVideo.put(cacheServer.getKey(), new HashSet<>());
                        cacheVideo.get(cacheServer.getKey()).add(video.getKey());
                        //videoStored[video.getKey()] = true;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < numberOfCacheServers; i++) {
            for (Map.Entry<Integer, Double> entry : videoValue.entrySet()) {
                if (videoSizes[entry.getKey()] <= cacheVolume[i]) {
                    cacheVolume[i] -= videoSizes[entry.getKey()];
                    if (!cacheVideo.containsKey(i))
                        cacheVideo.put(i, new HashSet<>());
                    cacheVideo.get(i).add(entry.getKey());
                }
            }
        }
    }

    @Override
    public double averageWaitingTime() {
        return 0;
    }

    @Override
    public String[] output() {
        String[] output = new String[cacheVideo.size() + 1];
        output[0] = "" + cacheVideo.size();
        int i = 1;
        for (Map.Entry<Integer, Set<Integer>> entry : cacheVideo.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(entry.getKey() + " ");
            for (Integer video : entry.getValue())
                stringBuilder.append(video + " ");
            output[i++] = stringBuilder.toString();
        }
        return output;
    }

    private LinkedHashMap<Integer, Integer> sortMapByValue(Map<Integer, Integer> map, boolean desc) {
        List<Map.Entry<Integer, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort( list, new Comparator<Map.Entry<Integer, Integer>>()
        {
            public int compare( Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2 )
            {
                if (desc)
                    return (o2.getValue()).compareTo( o1.getValue() );
                else
                    return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );

        LinkedHashMap<Integer, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    private LinkedHashMap<Integer, Double> sortMapByValueDouble(Map<Integer, Double> map, boolean desc) {
        List<Map.Entry<Integer, Double>> list = new LinkedList<>(map.entrySet());
        Collections.sort( list, new Comparator<Map.Entry<Integer, Double>>()
        {
            public int compare( Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2 )
            {
                if (desc)
                    return (o2.getValue()).compareTo( o1.getValue() );
                else
                    return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );

        LinkedHashMap<Integer, Double> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }
}
