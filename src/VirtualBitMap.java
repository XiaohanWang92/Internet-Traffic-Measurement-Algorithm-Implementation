import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class VirtualBitMap {
    private static final int VIRTUAL_MAP_SIZE = 10000;
    private static final int ACTUAL_MAP_SIZE = 30000000;
    private int[] Random;
    private boolean[] ActualBitMap;
    private Map<String, Integer> flowIdMap;
    private Map<Integer, Integer> flowMap;

    public VirtualBitMap() {
        Random = new int[ACTUAL_MAP_SIZE];
        ActualBitMap = new boolean[ACTUAL_MAP_SIZE];
        flowIdMap = new HashMap<>();
        flowMap = new HashMap<>();
    }

    public static void main(String[] args) throws Exception {
        VirtualBitMap project = new VirtualBitMap();
        project.count();
    }

    public void count() throws Exception {

        // initialization
        Scanner scanner = new Scanner(new FileReader(System.getProperty("user.dir") + "/FlowTraffic.txt"));
        PrintWriter writer = new PrintWriter("VirtualBitMapResult.txt");
        Random random = new Random();

        // online operation
        int count = 0;
        GenerateRandom(Random);
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(" +");
            String sourceIP = split[0];
            String destinationIP = split[1];
            String flow = sourceIP + destinationIP;
            flowIdMap.put(flow, count);
            int size = Integer.parseInt(split[2]);

            // precisely count flows
            flowMap.put(count, size);

            // use virtual bitmap to count cardinality
            for(int i = 0; i < size; i++) {
                Integer element = random.nextInt();
                int randomIndex = Math.abs(element.hashCode()) % VIRTUAL_MAP_SIZE;
                Integer temp = count ^ Random[randomIndex];
                int actualIndex = Math.abs(temp.hashCode()) % ACTUAL_MAP_SIZE;
                ActualBitMap[actualIndex] = true;
            }
            count++;
        }
        scanner.close();

        // offline operation
        double actualZeroPercent = countZeroFraction(ActualBitMap); 
        for(Integer flowID: flowMap.keySet()) {
            int size = flowMap.get(flowID);
            if(size < 300) continue;
            boolean[] virtualMap = new boolean[VIRTUAL_MAP_SIZE];
            for(int i = 0; i < VIRTUAL_MAP_SIZE; i++) {
                Integer temp = flowID ^ Random[i];
                virtualMap[i] = ActualBitMap[Math.abs(temp.hashCode()) % ACTUAL_MAP_SIZE];
            }
            
            // calculate the result
            double virtualZeroPercent = countZeroFraction(virtualMap);
            if(virtualZeroPercent == 0 || virtualZeroPercent == 1) continue;
            double result = VIRTUAL_MAP_SIZE * (Math.log(actualZeroPercent) - Math.log(virtualZeroPercent));
            System.out.println(size + "\t" + result);
            writer.println(size + "\t" + result);
        }
        writer.close();
        System.out.println("Finish");
    }

    public static double countZeroFraction(boolean[] map) {
        double count = 0;
        for(int i = 0; i < map.length; i++) {
            if(!map[i]) {
                count++;
            }
        }
        return count / map.length;
    }

    public static long convert(String s) {
        long result = 0;
        int shiftNum = 24;
        String[] chunks = s.split("\\.");
        for(String chunk : chunks) {
            long chunkNum = 0;
            for(char c : chunk.toCharArray()) {
                chunkNum = chunkNum * 10 + (c - '0');
            }
            result += chunkNum * Math.pow(2, shiftNum);
            shiftNum -= 8;
        }
        return result;
    }

    private void GenerateRandom(int[] Random) {
        Random rand = new Random();
        for(int i = 0; i < Random.length; i++) {
            Random[i] = rand.nextInt();
        }
    }
}
