import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    private Scanner readFromFile;
    private PrintWriter printToFile;
    private int i, j, k, n, b, bagsLen;
    private Integer answerNumber[];
    private int bags[][];
    private int bagCombination[];
    private int outputCombinations[][];
    private int outputCombinationsLen;
    private final int RANDOM_LIMIT = 20;
    private final int MAX_STEP = 10000000;

    public static void main(String[] args) {
       Main test = new Main();
       test.init();
       test.solve();
    }

    public void init(){
        try {
            printToFile = new PrintWriter(new File("output.txt"));
            readFromFile = new Scanner(new File("input.txt"));
            n = readFromFile.nextInt();
            answerNumber = new Integer[n];
            for(int i = 0; i < n; i++){
                answerNumber[i] = readFromFile.nextInt();
            }
            b = readFromFile.nextInt();
            readFromFile.close();
        }
        catch (Exception ex){
            System.out.println("Ошибка: " + ex);
        }

        bags = new int[1 << n][];
        bagsLen = 0;
        bagCombination = new int[n];
        outputCombinations = new int[b][n];
        outputCombinationsLen = 0;
    }

    public void addBag(int k, int len){
        if (k == len){
            bags[bagsLen] = new int[len];
            bags[bagsLen++] = Arrays.copyOf(bagCombination, len);
            return;
        }

        for(int i = k > 0 ? bagCombination[k-1] + 1 : 0; i < n; i++){
			if((len - k) > (n - i)){
				return;
			}
            bagCombination[k] = i;
            addBag(k+1, len);
        }
    }

    public void allBags(){
        for(int len = 1; len <= n; len++)
            addBag(0, len);
    }

    public void solve(){
        int pointOfCombinationByLen[] = new int[n];
        int pointOfBestCombinationByLen[] = new int[n];
        int combination[] = new int[n];
        int bestCombination[] = new int[n];
        int minAnswer = Collections.min(Arrays.asList(answerNumber));
        Random rand = new Random();

        allBags();


        bagsLen = Math.min(bagsLen, MAX_STEP / (b * RANDOM_LIMIT));
        for(i = 1; i <= minAnswer; i++){
            Arrays.fill(combination, i);
            output(combination);
        }

        for(int t = minAnswer; t < b; t++){
            Arrays.fill(pointOfBestCombinationByLen, -1);
            for(int tt = 0; tt < RANDOM_LIMIT; tt++){
                for(k = 0; k < n; k++) {
                    combination[k] = rand.nextInt(answerNumber[k]) + 1;
                    pointOfCombinationByLen[k] = 0;
                }

                for(k = 0; k < bagsLen; k++){
                    for(i = 0; i < outputCombinationsLen; i++){
                        for(j = 0; j < bags[k].length; j++){
                            if(outputCombinations[i][bags[k][j]] != combination[bags[k][j]]){
                                break;
                            }
                        }
                        if(j == bags[k].length){
                            break;
                        }
                    }
                    if(i == outputCombinationsLen){
                        pointOfCombinationByLen[bags[k].length - 1]++;
                    }
                }

                for(k = 0; k < n; k++){
                    if(pointOfCombinationByLen[k] > pointOfBestCombinationByLen[k]) {
                        bestCombination = Arrays.copyOf(combination, n);
                        pointOfBestCombinationByLen = Arrays.copyOf(pointOfCombinationByLen, n);
                        break;
                    }
                }
            }
            output(bestCombination);
        }
        printToFile.close();
    }

    public void output(int[] array){
        for(int i = 0; i < n; i++)
            printToFile.print(array[i] + " ");
        printToFile.println();

        outputCombinations[outputCombinationsLen++] = Arrays.copyOf(array, n);
    }
}
