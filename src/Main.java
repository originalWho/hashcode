public class Main {

    private static final String FILE_NAME = "/Users/Artur/dc.in";

    public static void main(String[] args) {
        ParserI parser = new Parser();
        parser.setFile(FILE_NAME);
        parser.parse();
        DistributionI distribution = null;
        distribution.parser(parser);
        distribution.compute();

        System.out.println("Average Time: " + distribution.averageWaitingTime());

        for (String line: distribution.output()) {
            System.out.println(line);
        }

        System.out.println("Hello, Google Hash Code OQR!");
    }
}
