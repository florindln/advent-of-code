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

    public int getSides(List<char[]> matrix){
        //the sides of the polygon is the amount of of corner(from math edges=corners)
        //to count corners we have 2 types concave and convex corners, ? means any element
        //  ax                              ?a
        //  xx <- concave corner            ax <- convex corner
        //this approach lets us count the corners, only looking at the element up,to the left and diagonal up-left
        // if above or left doesn't exist (out of bounds), then it counts the same as a different element
        // do this for all rotations, not only top and left

        var res=0;
        for (var node:gardenNodes){
            var corners= getCorners(matrix,node);
            res+=corners;
        }
        return res;
    }

    enum Corner{
        CONVEX,
        CONCAVE,
    }

    private static int getCorners(List<char[]> matrix, GardenNode node) {
        var r = node.id().r();
        var c = node.id().c();
        //one corner can be convex on all sides (isolated node) so we need to return a number/list here to count
        var res=new ArrayList<Corner>();

        //up-left
        var aboveWithinBounds = Solution.isWithinBounds(matrix, r - 1, c);
        var leftWithinBounds = Solution.isWithinBounds(matrix, r, c - 1);
        var aboveLeftWithinBounds = Solution.isWithinBounds(matrix, r - 1, c - 1);

        var aboveTheSame = aboveWithinBounds && matrix.get(r - 1)[c] == node.id().symbol();
        var leftTheSame = leftWithinBounds && matrix.get(r)[c - 1] == node.id().symbol();
        var aboveLeftTheSame = aboveLeftWithinBounds && matrix.get(r - 1)[c - 1] == node.id().symbol();

        if(aboveTheSame&&leftTheSame&&!aboveLeftTheSame)
            res.add( Corner.CONCAVE );
        if(!aboveTheSame&&!leftTheSame)
            res.add( Corner.CONVEX );

        //up-right
        var rightWithinBounds = Solution.isWithinBounds(matrix, r, c + 1);
        var aboveRightWithinBounds = Solution.isWithinBounds(matrix, r - 1, c + 1);

        var rightTheSame = rightWithinBounds && matrix.get(r)[c + 1] == node.id().symbol();
        var aboveRightTheSame = aboveRightWithinBounds && matrix.get(r - 1)[c + 1] == node.id().symbol();

        if (aboveTheSame && rightTheSame && !aboveRightTheSame)
            res.add( Corner.CONCAVE );
        if (!aboveTheSame && !rightTheSame)
            res.add( Corner.CONVEX );

        //down-left
        var downWithinBounds = Solution.isWithinBounds(matrix, r + 1, c);
        var downLeftWithinBounds = Solution.isWithinBounds(matrix, r + 1, c - 1);

        var downTheSame = downWithinBounds && matrix.get(r + 1)[c] == node.id().symbol();
        var downLeftTheSame = downLeftWithinBounds && matrix.get(r + 1)[c - 1] == node.id().symbol();

        if(downTheSame&&leftTheSame&&!downLeftTheSame)
            res.add( Corner.CONCAVE );
        if(!downTheSame&&!leftTheSame)
            res.add( Corner.CONVEX );

        //down-right
        var downRightWithinBounds = Solution.isWithinBounds(matrix, r + 1, c + 1);
        var downRightTheSame = downRightWithinBounds && matrix.get(r + 1)[c + 1] == node.id().symbol();

        if (downTheSame && rightTheSame && !downRightTheSame)
            res.add( Corner.CONCAVE );
        if (!downTheSame && !rightTheSame)
            res.add( Corner.CONVEX );

        return res.size();
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
        System.out.println("Result part 2: "+ getRegionsPriceWithSides(matrix,regions));


    }

    private static int getRegionsPriceWithSides(List<char[]> matrix, List<Region> regions) {
        var res=0;
        for (var region:regions){
            var sides=region.getSides(matrix);
            var area=region.getArea();
            var price=sides*area;
            res+=price;
        }
        return res;
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

    static boolean isWithinBounds(List<char[]> matrix, int r,int c){
        if(r<0||r>=matrix.size() || c<0||c>=matrix.get(0).length)
            return false;
        return true;
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
