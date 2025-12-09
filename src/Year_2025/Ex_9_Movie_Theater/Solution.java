package Year_2025.Ex_9_Movie_Theater;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
    record TileCoord(int row, int col){}

    public static List<TileCoord> getInputData(String path) throws FileNotFoundException {
        File input = new File(path);
        Scanner myReader = new Scanner(input);

        List<TileCoord> tileCoords =new ArrayList<>();

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            var split=line.split(",");
            tileCoords.add(new TileCoord(Integer.parseInt(split[1]),Integer.parseInt(split[0])));
        }

        myReader.close();

        return tileCoords;
    }

    static long solutionPart1(List<TileCoord> tileCoords){
        long maxArea=0;
        for (int i = 0; i < tileCoords.size(); i++) {
            for (int j = i+1; j < tileCoords.size(); j++) {
                var area=calculateArea(tileCoords.get(i),tileCoords.get(j));
                maxArea=Math.max(maxArea,area);
            }
        }
        return maxArea;
    }

    private static long calculateArea(TileCoord tileCoord1, TileCoord tileCoord2) {
        var length=Math.abs(tileCoord1.col-tileCoord2.col)+1;
        var height=Math.abs(tileCoord1.row-tileCoord2.row)+1;
        return (long) length *height;
    }

    record Rectangle(TileCoord min, TileCoord max) {
        boolean isOverlap(Rectangle other) {
            return min.row() < other.max.row() && max.row() > other.min.row() && min.col() < other.max.col() && max.col() > other.min.col();
        }
        long area() {
            return (max.row() - min.row() + 1) * (max.col() - min.col() + 1);
        }
        static Rectangle fromTileCoords(TileCoord a, TileCoord b) {
            return new Rectangle(new TileCoord(Math.min(a.row(), b.row()), Math.min(a.col(), b.col())), new TileCoord(Math.max(a.row(), b.row()), Math.max(a.col(), b.col())));
        }
    }

    static long solutionPart2(List<TileCoord> redTiles) {
        // 1. Define the Boundary/Perimeter Lines
        // These are the segments that form the outside of the shape defined by the tiles.
        List<Rectangle> boundaryLines = createBoundaryLinesImperative(redTiles);

        // 2. Generate all Candidate Rectangles
        // These are all possible rectangles formed by pairing any two red tiles.
        List<Rectangle> candidateRectangles = generateCandidateRectanglesImperative(redTiles);

        // 3. Filter Candidates and Find the Maximum Area
        return findMaxNonOverlappingArea(candidateRectangles, boundaryLines);
    }

// --- Helper Functions using Traditional Loops ---

    /**
     * Creates the list of rectangles representing the closed path (perimeter)
     * connecting the red tiles in sequence.
     */
    private static List<Rectangle> createBoundaryLinesImperative(List<TileCoord> redTiles) {
        List<Rectangle> lines = new ArrayList<>();
        int size = redTiles.size();

        for (int i = 0; i < size; i++) {
            TileCoord start = redTiles.get(i);
            // Use the modulo operator (%) to connect the last tile (i=size-1)
            // back to the first tile (index 0).
            TileCoord end = redTiles.get((i + 1) % size);

            lines.add(Rectangle.fromTileCoords(start, end));
        }

        return lines;
    }

    /**
     * Creates a list of all unique rectangles whose corners are defined
     * by every possible pair of coordinates from the red tiles list.
     */
    private static List<Rectangle> generateCandidateRectanglesImperative(List<TileCoord> redTiles) {
        List<Rectangle> candidates = new ArrayList<>();
        int size = redTiles.size();

        // Outer loop: Iterate through the first coordinate (i)
        for (int i = 0; i < size; i++) {
            // Inner loop: Iterate through the second coordinate (j), starting AFTER i
            // This prevents duplicate pairs (a,b) vs (b,a) and self-pairing (a,a).
            for (int j = i + 1; j < size; j++) {
                TileCoord a = redTiles.get(i);
                TileCoord b = redTiles.get(j);
                candidates.add(Rectangle.fromTileCoords(a, b));
            }
        }

        return candidates;
    }

    /**
     * Finds the maximum area among the candidates that do not overlap with any boundary line.
     */
    private static long findMaxNonOverlappingArea(
            List<Rectangle> candidateRectangles,
            List<Rectangle> boundaryLines)
    {
        long maxArea = -1; // Initialize to an impossible value

        // Iterate through every candidate rectangle
        for (Rectangle candidate : candidateRectangles) {
            boolean overlapsWithBoundary = false;

            // Check if the candidate overlaps with ANY boundary line
            for (Rectangle boundary : boundaryLines) {
                if (candidate.isOverlap(boundary)) {
                    overlapsWithBoundary = true;
                    break; // Found an overlap, no need to check other boundary lines
                }
            }

            // If the candidate did NOT overlap, it's valid.
            if (!overlapsWithBoundary) {
                long currentArea = candidate.area();
                if (currentArea > maxArea) {
                    maxArea = currentArea;
                }
            }
        }

        // If maxArea is still -1, it means no valid rectangle was found.
        if (maxArea == -1) {
            throw new IllegalStateException("No valid non-overlapping rectangle found.");
        }

        return maxArea;
    }

//    static long solutionPart2(List<TileCoord> redTiles){
//        //first we need to get coords of all the possible green
//        //a tile is green if it is between 2 red tiles on the same row/col
//        //a tile is green if it is between 2 green tiles on the same row/col
//        var edgeGreenTiles=new ArrayList<TileCoord>();
//        for (int i = 0; i < redTiles.size(); i++) {
//            for (int j = i + 1; j < redTiles.size(); j++) {
//                var tile1=redTiles.get(i);
//                var tile2=redTiles.get(j);
//
//                fillEdgeGreenTiles(edgeGreenTiles,tile1,tile2);
//            }
//        }
//
//        edgeGreenTiles.sort((a,b)->a.col-b.col);
//        edgeGreenTiles.sort((a,b)->a.row-b.row);
//
//        //group by row
//        Map<Integer, List<Integer>> groupedByRow = edgeGreenTiles.stream()
//                .collect(Collectors.groupingBy(
//                        TileCoord::row,
//                        Collectors.mapping(TileCoord::col, Collectors.toList())
//                ));
//
//        List<TileCoord> missingCoordinatesCols = new ArrayList<>();
//
//        //iterate through each row to find missing cols
//        for (Map.Entry<Integer, List<Integer>> entry : groupedByRow.entrySet()) {
//            int currentRow = entry.getKey();
//            List<Integer> cols = entry.getValue();
//
//            Collections.sort(cols);
//            groupedByRow.put(currentRow,List.of(cols.get(0),cols.get(cols.size()-1)));
//        }
//
//        //now do the same for rows with grouping by cols
//        List<TileCoord> missingCoordinatesRows = new ArrayList<>();
//
//        edgeGreenTiles.sort((a,b)->a.row-b.row);
//        edgeGreenTiles.sort((a,b)->a.col-b.col);
//
//        Map<Integer, List<Integer>> groupedByCol = edgeGreenTiles.stream()
//                .collect(Collectors.groupingBy(
//                        TileCoord::col,
//                        Collectors.mapping(TileCoord::row, Collectors.toList())
//                ));
//
//        //iterate through each col to find missing rows
//        for (Map.Entry<Integer, List<Integer>> entry : groupedByCol.entrySet()) {
//            int currentCol = entry.getKey();
//            List<Integer> rows = entry.getValue();
//
//            Collections.sort(rows);
//            groupedByCol.put(currentCol,List.of(rows.get(0),rows.get(rows.size()-1)));
//        }
//
//        //solve just like the first one but perform the check before setting the new maxArea
//        long maxArea=0;
//        for (int i = 0; i < redTiles.size(); i++) {
//            for (int j = i+1; j < redTiles.size(); j++) {
//                var corner1=redTiles.get(i);
//                var corner2=redTiles.get(j);
//                var area=calculateArea(corner1,corner2);
//                //check
//                if(hasOnlyGreenOrRedInside(groupedByRow,groupedByCol,corner1,corner2)){
//                    maxArea=Math.max(maxArea,area);
//                }
//            }
//        }
//        return maxArea;
//    }
//
//    private static boolean hasOnlyGreenOrRedInside(Map<Integer, List<Integer>> groupedByRow, Map<Integer, List<Integer>> groupedByCol, TileCoord corner1, TileCoord corner2) {
////        if all 4 corners defined by the rectangle exist inside greenTiles either of the maps then the rectange is inside our polygon
//
//        var lowerCol=Math.min(corner1.col,corner2.col);
//        var higherCol=Math.max(corner1.col,corner2.col);
//        var lowerRow=Math.min(corner1.row,corner2.row);
//        var higherRow=Math.max(corner1.row,corner2.row);
//
//        var topLeftCorner=new TileCoord(lowerRow,lowerCol);
//        var topRightCorner=new TileCoord(lowerRow,higherCol);
//        var bottomLeftCorner=new TileCoord(higherRow,lowerCol);
//        var bottomRightCorner=new TileCoord(higherRow,higherCol);
//
//        var hasTopLeftCorner=(groupedByRow.get(topLeftCorner.row).get(0)<= topLeftCorner.col && topLeftCorner.col <= groupedByRow.get(topLeftCorner.row).get(1))
//                && (groupedByCol.get(topLeftCorner.col).get(0) <= topLeftCorner.row && topLeftCorner.row <= groupedByCol.get(topLeftCorner.col).get(1));
//
//        var hasTopRightCorner=(groupedByRow.get(topRightCorner.row).get(0)<= topRightCorner.col && topRightCorner.col <= groupedByRow.get(topRightCorner.row).get(1))
//                && (groupedByCol.get(topRightCorner.col).get(0) <= topRightCorner.row && topRightCorner.row <= groupedByCol.get(topRightCorner.col).get(1));
//
//        var hasBottomLeftCorner=(groupedByRow.get(bottomLeftCorner.row).get(0)<= bottomLeftCorner.col && bottomLeftCorner.col <= groupedByRow.get(bottomLeftCorner.row).get(1))
//                && (groupedByCol.get(bottomLeftCorner.col).get(0) <= bottomLeftCorner.row && bottomLeftCorner.row <= groupedByCol.get(bottomLeftCorner.col).get(1));
//
//        var hasBottomRightCorner=(groupedByRow.get(bottomRightCorner.row).get(0)<= bottomRightCorner.col && bottomRightCorner.col <= groupedByRow.get(bottomRightCorner.row).get(1))
//                && (groupedByCol.get(bottomRightCorner.col).get(0) <= bottomRightCorner.row && bottomRightCorner.row <= groupedByCol.get(bottomRightCorner.col).get(1));
//
//        if (hasTopLeftCorner&&hasTopRightCorner&&hasBottomLeftCorner&&hasBottomRightCorner)
//            return true;
//        else
//            return false;
//    }
//
//    private static void fillEdgeGreenTiles(List<TileCoord> greenTiles, TileCoord tile1, TileCoord tile2) {
//        //fill cols
//        if (tile1.row==tile2.row){
//            var lowerCol=Math.min(tile1.col,tile2.col);
//            var higherCol=Math.max(tile1.col,tile2.col);
//            for (int i = lowerCol; i < higherCol; i++) {
//                greenTiles.add(new TileCoord(tile1.row,i));
//            }
//        }
//        //fill rows
//        if (tile1.col==tile2.col){
//            var lowerRow=Math.min(tile1.row,tile2.row);
//            var higherRow=Math.max(tile1.row,tile2.row);
//            for (int i = lowerRow; i < higherRow; i++) {
//                greenTiles.add(new TileCoord(i, tile1.col));
//            }
//        }
//
//        //nothing otherwise
//    }

}
