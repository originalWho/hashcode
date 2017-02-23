/**
 * Created by Artur on 2/23/17.
 */
public interface DistributionI {
    void parser(ParserI parser);
    void compute();
    double averageWaitingTime();
    String[] output();
}
