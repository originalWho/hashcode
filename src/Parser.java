import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Artur on 2/23/17.
 */
public class Parser implements ParserI {
    File file;
    Scanner scanner;
    int numberOfVideos;
    int numberOfEndpoints;
    int numberOfRequestDescriptions;
    int numberOfCacheServers;
    int capacityOfCacheServers;
    int[] videoSizes;
    ArrayList<EndPoint> endPoints;

    @Override
    public boolean setFile(String fileName) {
        file = new File(fileName);
        try {
            scanner = new Scanner(file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean parse() {
        try {
            numberOfVideos = scanner.nextInt();
            numberOfEndpoints = scanner.nextInt();
            numberOfRequestDescriptions = scanner.nextInt();
            numberOfCacheServers = scanner.nextInt();
            capacityOfCacheServers = scanner.nextInt();
            scanner.nextLine();

            videoSizes = new int[numberOfVideos];
            endPoints = new ArrayList<>(numberOfEndpoints);
            String videos = scanner.nextLine();
            Scanner videoScanner = new Scanner(videos);
            int video = 0;
            while (videoScanner.hasNextInt()) {
                videoSizes[video++] = videoScanner.nextInt();
            }

            for (int ep = 0; ep < numberOfEndpoints; ep++) {
                int latency = scanner.nextInt();
                int caches = scanner.nextInt();
                EndPoint endPoint = new EndPoint(latency, caches);
                scanner.nextLine();

                for (int c = 0; c < caches; c++) {
                    int cache = scanner.nextInt();
                    int cacheLatency = scanner.nextInt();
                    endPoint.addLatencyToCache(cache, cacheLatency);
                    scanner.nextLine();
                }
                endPoints.add(endPoint);
            }

            for (int req = 0; req < numberOfRequestDescriptions; req++) {
                int videoNumber = scanner.nextInt();
                int endpointNumber = scanner.nextInt();
                int requests = scanner.nextInt();
                scanner.nextLine();
                endPoints.get(endpointNumber).addRequestToVideo(videoNumber, requests);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public int getNumberOfVideos() {
        return numberOfVideos;
    }

    @Override
    public int getNumberOfEndpoint() {
        return numberOfEndpoints;
    }

    @Override
    public int getNumberOfRequestDescriptions() {
        return numberOfRequestDescriptions;
    }

    @Override
    public int getNumberOfCacheServers() {
        return numberOfCacheServers;
    }

    @Override
    public int getCapacityOfCacheServer() {
        return numberOfCacheServers;
    }

    @Override
    public int[] getVideoSizes() {
        return videoSizes;
    }

    @Override
    public ArrayList<EndPoint> getEndpoints() {
        return endPoints;
    }
}
