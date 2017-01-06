import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class LinearCountingProvidedData {
    private static final int MAP_SIZE = 1000;
    private Map<String, Integer> flowMap;
    private HashMap<String, boolean[]> countMap;
    private HashSet<Integer> results;

    public LinearCounting () {
        flowMap = new HashMap<>();
        countMap = new HashMap<>();
        results = new HashSet<>();
    }

    public static void main(String[] args) throws Exception {
        LinearCounting project = new LinearCounting();
        project.count();
    }

    public void count() throws Exception {

        // initialization
        Scanner scanner = new Scanner(new FileReader(System.getProperty("user.dir") + "/FlowTraffic.txt"));
        PrintWriter writer = new PrintWriter("LinearCountingResult.txt");
        Random random = new Random();

        // online operation
        scanner.nextLine();
        String[] split;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            split = parse(line);
            String flowId = split[0] + split[1];
            int size = Integer.parseInt(split[2]);

            // precisely count flows
            flowMap.put(flowId, size);

            // linear counting algorithm implementation
            boolean[] bitMap = new boolean[MAP_SIZE];
            for(int i = 0; i < size; i++) {
                int index = random.nextInt(MAP_SIZE);
                bitMap[index] = true;
            }
            countMap.put(flowId, bitMap);
        }
        scanner.close();

        // offline operation
        for(String flow: flowMap.keySet()) {
            int size = flowMap.get(flow);
            if(results.contains(size)) continue;
            boolean[] bitMap = countMap.get(flow);
            double fraction = countZeroFraction(bitMap);
            if(fraction == 0) continue;
            double result = - MAP_SIZE * Math.log(countZeroFraction(bitMap));
            System.out.println(flowMap.get(flow) + "\t" + result);
            writer.println(size + "\t" + result);
            results.add(size);
        }
        writer.close();
        System.out.println("Finish");
    }

    private static double countZeroFraction(boolean[] map) {
        double count = 0;
        for(int i = 0; i < map.length; i++) {
            if(!map[i]) {
                count++;
            }
        }
        return count / map.length;
    }

    private String[] parse(String line) {
        int left = 0;
        int right = 0;
        int index = 0;
        char[] chars = line.toCharArray();
        String[] result = new String[3];
        while(right < line.length()) {
            if(chars[right] == ' ' && chars[right - 1] != ' ') {
                result[index++] = line.substring(left, right);
            }
            if(right > 0 && chars[right] != ' ' && chars[right - 1] == ' ') {
                left = right;
            }
            right++;
        }
        return result;
    }
}
