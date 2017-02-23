import java.util.ArrayList;

/**
 * Created by Artur on 2/23/17.
 */
public interface ParserI {
    boolean setFile(String fileName);
    boolean parse();
    int getNumberOfVideos();
    int getNumberOfEndpoint();
    int getNumberOfRequestDescriptions();
    int getNumberOfCacheServers();
    int getCapacityOfCacheServer();
    int[] getVideoSizes();
    ArrayList<EndPoint> getEndpoints();
}
