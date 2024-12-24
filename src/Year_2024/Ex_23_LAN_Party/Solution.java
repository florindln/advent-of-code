package Year_2024.Ex_23_LAN_Party;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

//record Connection(String left,String right){}

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_23_LAN_Party/input.txt");
        Scanner myReader = new Scanner(input);

        List<String[]> connections=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            var parts=line.split("-");
            connections.add(parts);
        }
        myReader.close();

//        List<Set<String>> networks=makeNetworks(connections);
//
//        List<List<String>> networksOfThree=new ArrayList<>();
//        for (var network:networks){
//            var networkList=new ArrayList<>(network);
//            var networksOfThreeForThisNetwork=getNetWorksOfThree(networkList);
//            networksOfThree.addAll(networksOfThreeForThisNetwork);
//        }


//        for (var conn:connections)
//            System.out.print(Arrays.toString(conn));

        System.out.println("Result part 1: "+ resPart1(connections));
        System.out.println("Result part 2: "+ resPart2(connections));
    }

    private static String resPart2(List<String[]> connections) {
        Map<String, Set<String>> neighbors = makeNeighbors(connections);
        List<String> uniqueComputers = getUniqueComputers(connections);
        var possibleCombinationsOfThree = getCombinationsOfXSet(uniqueComputers, 3);

        List<Set<String>> threeInterconnected = new ArrayList<>();
        for (var possible : possibleCombinationsOfThree) {
            if (connectionPossible(neighbors, possible))
                threeInterconnected.add(possible);
        }

        //for list in our set of 3, get a list of all computers. for each computer see if we can find another computer connected to all. if so then we have a set of 4 interconnected. keep expanding until we find the highest element
        Set<Set<String>> prev = new HashSet<>(threeInterconnected);
        while (prev.size() > 1) {
//            System.out.println("size "+prev.size());
            prev = expandOnce(prev, uniqueComputers, neighbors);

        }
        var res = prev.stream().findFirst().get().stream().sorted().collect(Collectors.joining(","));
        return res;
    }

    private static Set<Set<String>> expandOnce(Set<Set<String>> threeInterconnectedSet, List<String> uniqueComputers, Map<String, Set<String>> neighbors) {
        Set<Set<String>> fourInterconnected=new HashSet<>();
        for (var three: threeInterconnectedSet){
            for (var computer: uniqueComputers) {
                if(three.contains(computer))
                    continue;

                var four = new HashSet<>(three);
                four.add(computer);
                if(connectionPossible(neighbors,four)){
                    fourInterconnected.add(four);
                }
            }
        }
        return fourInterconnected;
    }

    private static int resPart1(List<String[]> connections) {
        Map<String,Set<String>> neighbors=makeNeighbors(connections);
        List<String> uniqueComputers=getUniqueComputers(connections);
        var possibleCombinationsOfThree= getCombinationsOfXSet(uniqueComputers,3);

        List<Set<String>> threeInterconnected=new ArrayList<>();
        for (var possible:possibleCombinationsOfThree){
            if(connectionPossible(neighbors,possible))
                threeInterconnected.add(possible);
        }

        var threeInterconnectedStartsWithT=threeInterconnected.stream().filter(set->set.stream().anyMatch(computer->computer.startsWith("t"))).toList();

        return threeInterconnectedStartsWithT.size();
    }

    private static boolean connectionPossible(Map<String, Set<String>> neighbors, Set<String> possible) {
        var possArr=new ArrayList<>(possible);

        for (int i = 0; i < possArr.size(); i++) {
            for (int j = i + 1; j < possArr.size(); j++) {
                if (!neighbors.get(possArr.get(i)).contains(possArr.get(j)) || !neighbors.get(possArr.get(j)).contains(possArr.get(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    private static List<String> getUniqueComputers(List<String[]> connections) {
        var res=new HashSet<String>();
        for (var conn:connections){
            res.add(conn[0]);
            res.add(conn[1]);
        }
        return new ArrayList<>(res);
    }

    private static Map<String, Set<String>> makeNeighbors(List<String[]> connections) {
        Map<String, Set<String>> neighbors=new HashMap<>();
        for (var conn:connections){
            neighbors.putIfAbsent(conn[0],new HashSet<>());
            neighbors.putIfAbsent(conn[1],new HashSet<>());

            neighbors.get(conn[0]).add(conn[1]);
            neighbors.get(conn[1]).add(conn[0]);
        }
        return neighbors;
    }

    static List<Set<String>> getCombinationsOfXSet(List<String> input,int x){
        List<Set<String>> res=new ArrayList<>();
        getCombinationsOfThreeHelper(input,0,new ArrayList<>(),res,x);
        return res;
    }

    private static void getCombinationsOfThreeHelper(List<String> network, int i, List<String> curr, List<Set<String>> res,int x) {
        if(curr.size()==x){
            res.add(new HashSet<>(curr));
            return;
        }
        if(i==network.size())
            return;

        //include
        curr.add(network.get(i));
        getCombinationsOfThreeHelper(network,i+1,curr,res,x);
        curr.removeLast();

        //exclude
        getCombinationsOfThreeHelper(network,i+1,curr,res,x);
    }

//    ------------ code for a different solution ------------
    private static List<Set<String>> makeNetworks(List<String[]> connections) {
        List<Set<String>> networks=new ArrayList<>();

        for (var conn:connections){
            var found=false;
            for (int i = 0; i < networks.size(); i++) {
                var network=networks.get(i);
                if(network.contains(conn[0]) || network.contains(conn[1])){
                    network.add(conn[0]);
                    network.add(conn[1]);
                    found=true;
                    break;
                }
            }
            if(!found){
                var n=new HashSet<String>();
                n.add(conn[0]);
                n.add(conn[1]);
                networks.add(n);
            }
        }

        //now fix the structure since when adding we didnt merge any sets even though there could have been common elements
        for (int i = 0; i < networks.size(); i++) {
            for (int j = i+1; j < networks.size(); j++) {
                int finalJ = j;
                var matches=networks.get(i).stream().anyMatch(string->networks.get(finalJ).contains(string));
                if(matches){
                    networks.get(i).addAll(networks.get(j));
                    networks.remove(j);
                }
            }
        }

        return networks;
    }


}
