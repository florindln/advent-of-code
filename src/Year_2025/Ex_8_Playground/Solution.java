package Year_2025.Ex_8_Playground;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    record JunctionBox(int x, int y, int z){}
    record JunctionBoxPair(JunctionBox b1, JunctionBox b2){}

    public static List<JunctionBox> getInputData(String path) throws FileNotFoundException {
        File input = new File(path);
        Scanner myReader = new Scanner(input);

        List<JunctionBox> junctionBoxes =new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            var split=line.split(",");
            junctionBoxes.add(new JunctionBox(Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2])));
        }

        myReader.close();

        junctionBoxes.sort((c1, c2)->{
            return c1.z-c2.z;
        });
        junctionBoxes.sort((c1, c2)->{
            return c1.y-c2.y;
        });
        junctionBoxes.sort((c1, c2)->{
            return c1.x-c2.x;
        });

        return junctionBoxes;
    }

    static long solutionPart1(List<JunctionBox> junctionBoxes, int maxNumberOfConnections){
        var map = getDistanceToBoxesMap(junctionBoxes);

        var connectionsMade=0;
        var connectedCircuits=new ArrayList<Set<JunctionBox>>();
        for (var distance:map.keySet()){
            var coordsList=map.get(distance);
            for (var coordPair:coordsList){
                var b1=coordPair.b1;
                var b2=coordPair.b2;

                boolean isB1Connected=isConnected(connectedCircuits,b1);
                boolean isB2Connected=isConnected(connectedCircuits,b2);

                if (isB1Connected&&isB2Connected){
                    mergeCircuits(connectedCircuits,b1,b2);
                }
                else if (isB1Connected) {
                    connectCircuit(connectedCircuits,b1,b2);
                }
                else if (isB2Connected) {
                    connectCircuit(connectedCircuits,b2,b1);
                }
                else {
                    connectedCircuits.add(new HashSet<>(List.of(b1,b2)));
                }
                connectionsMade++;
                if (connectionsMade==maxNumberOfConnections)
                    return sortAndGetMaxThreeLargestSum(connectedCircuits);

            }
        }
        return sortAndGetMaxThreeLargestSum(connectedCircuits);
    }

    private static TreeMap<Double, List<JunctionBoxPair>> getDistanceToBoxesMap(List<JunctionBox> junctionBoxes) {
        //use a map of coordPairs where key=distance and value is a list<pair<coord>>
        //use the map then to connect the closest distances and keep track of existing with a set
        var map=new TreeMap<Double,List<JunctionBoxPair>>();

        for (int i = 0; i < junctionBoxes.size(); i++) {
            for (int j = i+1; j < junctionBoxes.size(); j++) {
                var b1= junctionBoxes.get(i);
                var b2= junctionBoxes.get(j);
                var distance=getDistance(b1,b2);
                if(!map.containsKey(distance))
                    map.put(distance,new ArrayList<>());
                map.get(distance).add(new JunctionBoxPair(b1,b2));
            }
        }
        return map;
    }

    private static void connectCircuit(ArrayList<Set<JunctionBox>> connectedCircuits,JunctionBox existingBox, JunctionBox newBox) {
        var existingCircuit=connectedCircuits.stream().filter(circuit->circuit.contains(existingBox)).flatMap(Set::stream).collect(Collectors.toSet());
        var index=connectedCircuits.indexOf(existingCircuit);
        connectedCircuits.get(index).add(newBox);
    }

    private static void mergeCircuits(ArrayList<Set<JunctionBox>> connectedCircuits, JunctionBox b1, JunctionBox b2) {
        var c1=connectedCircuits.stream().filter(circuit->circuit.contains(b1)).flatMap(Set::stream).collect(Collectors.toSet());
        var c2=connectedCircuits.stream().filter(circuit->circuit.contains(b2)).flatMap(Set::stream).collect(Collectors.toSet());
        connectedCircuits.remove(c1);
        connectedCircuits.remove(c2);
        var newSet=new HashSet<JunctionBox>();
        newSet.addAll(c1);
        newSet.addAll(c2);
        connectedCircuits.add(newSet);
    }

    static boolean isConnected(ArrayList<Set<JunctionBox>> connectedCircuits, JunctionBox box) {
        for (var circuit:connectedCircuits){
            for (var connectedBox:circuit){
                if (connectedBox.equals(box))
                    return true;
            }
        }
        return false;
    }

    static long sortAndGetMaxThreeLargestSum(ArrayList<Set<JunctionBox>> connectedCircuits){
        connectedCircuits.sort((a,b)->b.size()-a.size());
        var res=connectedCircuits.get(0).size()*connectedCircuits.get(1).size()*connectedCircuits.get(2).size();
        return res;
    }

    static double getDistance(JunctionBox c1, JunctionBox c2){
        var xDiff=c1.x-c2.x;
        var yDiff=c1.y-c2.y;
        var zDiff=c1.z-c2.z;

        var res=Math.sqrt(Math.pow(xDiff,2)+Math.pow(yDiff,2)+Math.pow(zDiff,2));
//        if (res != Math.floor(res)) {
//            throw new IllegalArgumentException("Double value has a fractional part and cannot be strictly converted to int: " + res);
//        }
        var rounded=round(res,4);
        return rounded;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    static long solutionPart2(List<JunctionBox> junctionBoxes){
        var map = getDistanceToBoxesMap(junctionBoxes);

        var connectedCircuits=new ArrayList<Set<JunctionBox>>();
        for (var distance:map.keySet()){
            var coordsList=map.get(distance);
            for (var coordPair:coordsList){
                var b1=coordPair.b1;
                var b2=coordPair.b2;

                boolean isB1Connected=isConnected(connectedCircuits,b1);
                boolean isB2Connected=isConnected(connectedCircuits,b2);

                if (isB1Connected&&isB2Connected){
                    mergeCircuits(connectedCircuits,b1,b2);
                }
                else if (isB1Connected) {
                    connectCircuit(connectedCircuits,b1,b2);
                }
                else if (isB2Connected) {
                    connectCircuit(connectedCircuits,b2,b1);
                }
                else {
                    connectedCircuits.add(new HashSet<>(List.of(b1,b2)));
                }
                if (connectedCircuits.size()==1 && connectedCircuits.get(0).size()==junctionBoxes.size()){
                    return (long) b1.x * b2.x;
                }
            }
        }
        return -1;
    }

}
