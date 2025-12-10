package Year_2025.Ex_10_Factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {
    record InputRow(List<Boolean> lights,List<Button> buttons, List<Integer> joltages){}
    record InputData(List<InputRow> inputRows){}
    record Button(List<Integer> toggledLights){
        @Override
        public String toString() {
            String lightValues = toggledLights.stream()
                    .map(String::valueOf)
                    .collect(java.util.stream.Collectors.joining(", "));
            return "("+lightValues+")";
        }
    }

    public static InputData getInputData(String path) throws FileNotFoundException {
        File input = new File(path);
        Scanner myReader = new Scanner(input);

        List<InputRow> inputRows=new ArrayList<>();
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();

            List<Boolean> lights=new ArrayList<>();
            for (var c:line.toCharArray()){
                if (c=='.')
                    lights.add(false);
                if (c=='#')
                    lights.add(true);
            }

            List<Button> buttons=new ArrayList<>();
            String buttonRegex = "\\((.*?)\\)";
            Pattern buttonPattern = Pattern.compile(buttonRegex);
            Matcher buttonMatcher = buttonPattern.matcher(line);
            while (buttonMatcher.find()) {
                String content = buttonMatcher.group(1);
                var numsString=content.split(",");
                var toggledLights=Arrays.stream(numsString).map(ns->Integer.parseInt(ns)).toList();
                buttons.add(new Button(toggledLights));
            }

            List<Integer> joltages=new ArrayList<>();
            String joltageRegex = "\\{(.*?)\\}";
            Pattern joltagePattern = Pattern.compile(joltageRegex);
            Matcher joltageMatcher = joltagePattern.matcher(line);
            while (joltageMatcher.find()) {
                String content = joltageMatcher.group(1);
                var numsString=content.split(",");
                joltages=Arrays.stream(numsString).map(ns->Integer.parseInt(ns)).toList();
            }

            inputRows.add(new InputRow(lights,buttons,joltages));
        }

        myReader.close();

        return new InputData(inputRows);
    }

    static long solutionPart1(InputData inputData){
        var minimums=new ArrayList<Integer>();
        for (var row:inputData.inputRows){
            var startState = new ArrayList<Boolean>();
            for (int i = 0; i < row.lights.size(); i++) {
                startState.add(false);
            }

            List<Boolean> targetState = row.lights;

            Queue<List<Boolean>> q = new LinkedList<>();
            Set<String> visitedStates = new HashSet<>();

            q.add(startState);
            visitedStates.add(startState.toString());
            var iterations = 0;
            var finishedRow = false;

            while (!q.isEmpty()) {
                var qSize = q.size();
                iterations++;

                if (iterations > 1000) {
                    throw new RuntimeException("Hit sanity check");
                }

                for (int i = 0; i < qSize; i++) {
                    var currentState = q.poll();

                    for (var button : row.buttons) {
                        var nextState = new ArrayList<>(currentState);
                        applyButton(nextState, button);

                        if (nextState.equals(targetState)) {
                            finishedRow = true;
                            break;
                        }

                        var nextStateKey = nextState.toString();
                        if (!visitedStates.contains(nextStateKey)) {
                            visitedStates.add(nextStateKey);
                            q.add(nextState);
                        }
                    }

                    if (finishedRow) {
                        break;
                    }
                }

                if (finishedRow) {
                    minimums.add(iterations);
                    break;
                }
            }
        }

        var res=minimums.stream().mapToInt(a -> a).sum();
        return res;
    }

    private static void applyButton(List<Boolean> sequence, Button button) {
        for (var toggledLight : button.toggledLights) {
            sequence.set(toggledLight, !sequence.get(toggledLight));
        }
    }
}
