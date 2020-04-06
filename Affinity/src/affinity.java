/**
 * @author Dory Mauretour
 * @author Prof. Greenwell
 * Instructions: read and parse csv (assign to data structure)
 * print out the confidences & supports of items based on purchase history
 * Date: 4/5/2020
 * Affinity Analysis
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class affinity {

    public static void main(String[] args) throws IOException {

        File sampleFile = new File("./src/sampleFile.csv");
        BufferedReader reader;

        if (!sampleFile.exists()) {
            System.out.println("File doesn't exist");
        }

        // Reader and line for file reading
        reader = new BufferedReader(new FileReader(sampleFile));
        String line = null;
        int transactions = 0;

        // read the first line and split(using FP) for header and assign number of features
        String[] features = reader.readLine().split(",");
        int numberOfFeatures = features.length;

        List <int[]> sampleResults = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            sampleResults.add(Arrays.stream(line.split(","))
                    .mapToInt(Integer::parseInt).toArray());
            transactions++;
        }
        reader.close();

        Map<String, Integer> fullResults = new HashMap<>();
        Map<HashSet<String>, Integer> validResults = new HashMap<>();

        for(int[] sample : sampleResults){
            for (int premise = 0; premise < numberOfFeatures; premise++){
                if (sample[premise] == 1){
                    fullResults.put(features[premise],fullResults.getOrDefault(features[premise],0)+1);
                }

                for (int conclusion = 0; conclusion < numberOfFeatures; conclusion++){
                    if(conclusion == premise){
                        continue;
                    }

                    if(sample[conclusion] == 1){
                        validResults.put(
                                new HashSet<String>(Arrays.asList(
                                        features[premise],features[conclusion])),
                                validResults.getOrDefault(new HashSet<String>(Arrays.asList(
                                        features[premise],features[conclusion])), 0)+1);
                    }
                }
            }
        }

        for (HashSet<String > featureSet : validResults.keySet()){
            List<String> featureList = featureSet.stream().collect(Collectors.toList());
            double confidence = (double) fullResults.get(featureList.get(0)) / validResults.get(featureSet);
            double support = (double) validResults.get(featureSet) / transactions ;
            //print out confidence and support
            System.out.printf(" We show confidence of %f that a person who "
                    + "likes %s category will also like %s%n         and a support of %f that "
                    + "a person will like these category together at all. %n", confidence, featureList.get(0), featureList.get(1), support
            );
        }
    }

}

