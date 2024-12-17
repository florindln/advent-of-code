package Year_2024.Ex_12_Garden_Groups;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


record GardenNode(GardenNodeId id,List<GardenNode> neighbors){
    public int getPerimeter(){
        return 4- this.neighbors.size();
    }
}
record GardenNodeId(int r,int c,char symbol){}
record Region(List<GardenNode> gardenNodes){
    public int getArea(){
        return this.gardenNodes.size();
    }
    public int getPerimeter(){
        return this.gardenNodes.stream().mapToInt(GardenNode::getPerimeter).sum();
    }
}

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_12_Garden_Groups/input.txt");
        Scanner myReader = new Scanner(input);

        List<char[]> matrix=new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            matrix.add(line.toCharArray());
        }
        myReader.close();

//        for (var arr:matrix)
//            System.out.println(arr);

        List<Region> regions = getRegions(matrix);

//        for (var region:regions)
//            System.out.println(region);

        System.out.println("Result part 1: "+ getRegionsPrice(regions));

    }

    private static int getRegionsPrice(List<Region> regions) {
        var res=0;
        for (var region:regions){
            var area=region.getArea();
            var perimeter=region.getPerimeter();
            var price=area*perimeter;
            res+=price;
        }
        return res;
    }

    private static List<Region> getRegions(List<char[]> matrix) {
        //we put the garden matrix in a list of regions
        List<Region> regions=new ArrayList<>();
        var visited=new boolean[matrix.size()][matrix.get(0).length];
        for (int r = 0; r < matrix.size(); r++) {
            for (int c = 0; c < matrix.get(0).length; c++) {
                if(!visited[r][c]){
                    var region=new Region(new ArrayList<>());
                    expandAndPopulateRegion(matrix,visited,r,c, matrix.get(r)[c],region);
                    regions.add(region);
                }
            }
        }
        return regions;
    }

    private static void expandAndPopulateRegion(List<char[]> matrix, boolean[][] visited, int r, int c, char neededSymbol, Region region) {
//        if(r<0||r>=matrix.size() || c<0||c>=matrix.get(0).length || visited[r][c])
//            return;

        var gardenNodes = new ArrayList<GardenNode>();

        var firstNode = new GardenNode(new GardenNodeId(r, c, neededSymbol), new ArrayList<>());
        Queue<GardenNode> q = new LinkedList<>();
        q.add(firstNode);
        visited[r][c] = true;
        while (!q.isEmpty()) {
            var curr = q.poll();
            var currRow = curr.id().r();
            var currCol = curr.id().c();
            var neighbors = new ArrayList<GardenNode>();
            var upRow = currRow - 1;
            if (upRow >= 0 && matrix.get(upRow)[currCol] == neededSymbol) {
                var upNeighbor = new GardenNode(new GardenNodeId(upRow, currCol, neededSymbol), new ArrayList<>());
                neighbors.add(upNeighbor);
                if (!visited[upRow][currCol]){
//                if (!q.contains(upNeighbor))
                    visited[upRow][currCol]=true;
                    q.add(upNeighbor);
                }
            }
            var downRow = currRow + 1;
            if (downRow < matrix.size() && matrix.get(downRow)[currCol] == neededSymbol) {
                var downNeighbor = new GardenNode(new GardenNodeId(downRow, currCol, neededSymbol), new ArrayList<>());
                neighbors.add(downNeighbor);
                if (!visited[downRow][currCol]){
//                if(!q.contains(downNeighbor))
                    visited[downRow][currCol]=true;
                    q.add(downNeighbor);
                }
            }
            var leftCol = currCol - 1;
            if (leftCol >= 0 && matrix.get(currRow)[leftCol] == neededSymbol) {
                var leftNeighbor = new GardenNode(new GardenNodeId(currRow, leftCol, neededSymbol), new ArrayList<>());
                neighbors.add(leftNeighbor);
                if (!visited[currRow][leftCol]){
//                if(!q.contains(leftNeighbor))
                    visited[currRow][leftCol]=true;
                    q.add(leftNeighbor);
                }
            }
            var rightCol = currCol + 1;
            if (rightCol < matrix.get(0).length && matrix.get(currRow)[rightCol] == neededSymbol) {
                var rightNeighbor = new GardenNode(new GardenNodeId(currRow, rightCol, neededSymbol), new ArrayList<>());
                neighbors.add(rightNeighbor);
                if (!visited[currRow][rightCol]){
//                if(!q.contains(rightNeighbor))
                    visited[currRow][rightCol]=true;
                    q.add(rightNeighbor);
                }
            }
            curr.neighbors().addAll(neighbors);
            gardenNodes.add(curr);
        }
        region.gardenNodes().addAll(gardenNodes);
    }

}
