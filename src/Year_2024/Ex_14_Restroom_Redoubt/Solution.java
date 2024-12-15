package Year_2024.Ex_14_Restroom_Redoubt;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

record Robot(Coords pos,Coords velocity){}

record Coords(int x, int y){}

class RobotBoard{
    public int width;
    public int height;
    public List<Robot> robots;
    public int changes=0;

    public RobotBoard(int width, int height, List<Robot> robots) {
        this.width = width;
        this.height = height;
        this.robots = robots;
    }

    @Override
    public String toString(){
        var res=new StringBuilder();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int finalC = c;
                int finalR = r;
                var robotsAtThisPos=robots.stream().filter(robot->robot.pos().x()== finalC &&robot.pos().y()== finalR).toList();
                if(!robotsAtThisPos.isEmpty()){
                    res.append(robotsAtThisPos.size());
                }
                else{
                    res.append('.');
                }
            }
            res.append('\n');
        }
        return res.toString();
    }

    public void move(int times){
        for (int i = 0; i < times; i++) {
            this.moveOnce();
//            System.out.println("Board after moving " +(i+1) + " times");
//            System.out.println(this);
        }
    }

    public void moveOnce() {
        for (int i = 0; i < this.robots.size(); i++) {
            var currRobot = this.robots.get(i);
            // we need to add the width again because in java for example -2%7 is equal to -2 but we want it to be 5
            var newX = (currRobot.pos().x() + currRobot.velocity().x() + this.width) % this.width;
            var newY = (currRobot.pos().y() + currRobot.velocity().y() +this.height) % this.height;
            var newPosition = new Coords(newX, newY);
            var movedRobot = new Robot(newPosition, currRobot.velocity());
            this.robots.set(i, movedRobot);
        }
        changes++;
    }

    public BigInteger getSafetyFactor(){
        //ignore middleline (no <=)
        var q1Robots= this.robots.stream().filter(robot -> robot.pos().x()<this.width/2 && robot.pos().y()<this.height/2).toList();
        var q2Robots= this.robots.stream().filter(robot -> robot.pos().x()>this.width/2 && robot.pos().y()<this.height/2).toList();
        var q3Robots= this.robots.stream().filter(robot -> robot.pos().x()<this.width/2 && robot.pos().y()>this.height/2).toList();
        var q4Robots= this.robots.stream().filter(robot -> robot.pos().x()>this.width/2 && robot.pos().y()>this.height/2).toList();


        BigInteger res=BigInteger.valueOf(q1Robots.size())
                .multiply( BigInteger.valueOf(q2Robots.size()) )
                .multiply( BigInteger.valueOf(q3Robots.size() ))
                .multiply( BigInteger.valueOf(q4Robots.size()) );
        return res;
    }

    public int getHighestNumRobotsInARow(){
        var highest=0;
        for (int r = 0; r < width; r++) {
            int finalR = r;
            var robotsInThisRow=this.robots.stream().filter(robot -> robot.pos().y()== finalR).toList();
            highest=Math.max(highest,robotsInThisRow.size());
        }
        return highest;
    }

    public int getHighestNumRobotsInACol(){
        var highest=0;
        for (int c = 0; c < width; c++) {
            int finalC = c;
            var robotsInThisRow=this.robots.stream().filter(robot -> robot.pos().x()== finalC).toList();
            highest=Math.max(highest,robotsInThisRow.size());
        }
        return highest;
    }
}

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_14_Restroom_Redoubt/input.txt");
        Scanner myReader = new Scanner(input);

//        var board=new RobotBoard(11,7,new ArrayList<>());
        var board=new RobotBoard(101,103,new ArrayList<>());

        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            //each line equals a robot in the form p=6,3 v=-1,-3
//            var pIndex=line.indexOf("p");
            var vIndex=line.indexOf("v");
            var posPart=line.substring(2,vIndex).trim();
            var posCoords=posPart.split(",");
            var position=new Coords(Integer.parseInt(posCoords[0]),Integer.parseInt(posCoords[1]));

            var velocityPart=line.substring(vIndex+2).trim();
            var velocityCoords=velocityPart.split(",");
            var velocity=new Coords(Integer.parseInt(velocityCoords[0]),Integer.parseInt(velocityCoords[1]));

            var robot=new Robot(position,velocity);
            board.robots.add(robot);
        }
        myReader.close();


        board.move(100);
        System.out.println("Result part 1: "+ board.getSafetyFactor());

        //for part 2 the picture is surrounded on all sides by robots so we need to get the move with the highest number of robots in a row and col
        var highestRow=0;
        var highestCol=0;
        var second=0;
        var interations=20000;
        for (int i = 100; i <interations ; i++) {
            board.moveOnce();

            if(highestRow+highestCol<board.getHighestNumRobotsInARow()+board.getHighestNumRobotsInACol()){
                highestRow=board.getHighestNumRobotsInARow();
                highestCol=board.getHighestNumRobotsInACol();
                second=board.changes;
//                System.out.println("new board with more robots aligned found at i "+i);
//                System.out.println(board);
            }
        }
        System.out.println("Result part 2: "+ second);
    }
}
