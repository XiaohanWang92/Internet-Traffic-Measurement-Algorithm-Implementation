import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class FMSketch {
    private static final int SKETCH_SIZE = 150;
    private Map<String, Integer> flowMap;
    private Map<String, int[]> sketchMap;

    public FMSketch () {
        flowMap = new HashMap<>();
        sketchMap = new HashMap<>();
    }

    public static void main(String[] args) throws Exception {
        FMSketch project = new FMSketch();
        project.count();
    }

    public void count() throws Exception {

        // initialization
        Scanner scanner = new Scanner(new FileReader(System.getProperty("user.dir") + "/FlowTraffic.txt"));
        PrintWriter writer = new PrintWriter("FMSketchResult.txt");
        Random random = new Random();

        // online operation
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = parse(line);
            String flowId = split[0] + split[1];
            int size = Integer.parseInt(split[2]);
            if(size < 200) continue;

            // precisely count flows
            flowMap.put(flowId, size);

            // use FM sketch to count cardinality
            int[] sketch = new int[SKETCH_SIZE];
            for(int i = 0; i < size; i++) {
                Integer element = random.nextInt();
                int hashCode = Math.abs(element.hashCode());
                int sketchIndex = hashCode % SKETCH_SIZE;
                hashCode = hashCode >>> 1;

                // get the least significant 1
                int height = 0;
                while(height < 31 && (hashCode & 1) == 1) {
                    hashCode = hashCode >>> 1;
                    height++;
                }
                int counter = 1;
                counter = counter << height;
                sketch[sketchIndex] |= counter;
            }
            sketchMap.put(flowId, sketch);
        }
        scanner.close();

        // offline operation
        for(String flow : sketchMap.keySet()) {
            int size = flowMap.get(flow);
            int[] tempSketch = sketchMap.get(flow);
            double sum = 0;
            for(int temp: tempSketch) {
                int z = countTrailingOnes(temp);
                sum += z;
            }

            // calculate the result by formula
            double constant = 0.7213 / (1 + 1.079 / (SKETCH_SIZE));
            double result = SKETCH_SIZE * Math.pow(2, sum / SKETCH_SIZE) / constant;
            System.out.println(size + "\t" + result);
            writer.println(size + "\t" + result);
        }
        writer.close();
        System.out.println("Finish");
    }

    private int countTrailingOnes(int number) {

        // border case
        if(number  == 0) return 0;

        // initialization
        int count = 0;
        while((number & 1) == 1) {
            number = number >>> 1;
            count++;
        }
        return count;
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
