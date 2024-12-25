package Year_2024.Ex_25_Code_Chronicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws FileNotFoundException {
        File input = new File("src/Year_2024/Ex_25_Code_Chronicle/input.txt");
        Scanner myReader = new Scanner(input);

        List<int[]> locks=new ArrayList<>();
        List<int[]> keys=new ArrayList<>();


        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();

            //lock
            if(line.contains("#")){
                var lock= getLockOrKey(myReader);
                locks.add(lock);
            }else{
                var key=getLockOrKey(myReader);
                for (int i = 0; i < key.length; i++) {
                    key[i]--;
                }
                keys.add(key);
            }
        }
        myReader.close();

//        for (var lock:locks)
//            System.out.println(Arrays.toString(lock));
//        System.out.println();
//        for (var key:keys)
//            System.out.println(Arrays.toString(key));

        System.out.println("Result part 1: "+ getUniqueLockKeyCombos(locks,keys));
    }

    private static int getUniqueLockKeyCombos(List<int[]> locks, List<int[]> keys) {
        var res=0;
        for (var lock:locks){
            for (var key:keys){
                if(keyFitsInLock(lock,key))
                    res++;
            }
        }
        return res;
    }

    private static boolean keyFitsInLock(int[] lock, int[] key) {
        for (int i = 0; i < lock.length; i++) {
            if(lock[i]+key[i]>5)
                return false;
        }
        return true;
    }

    private static int[] getLockOrKey(Scanner myReader) {
        String line;
        line= myReader.nextLine();
        List<char[]> matrix=new ArrayList<>();
        while (!line.isEmpty()){
            matrix.add(line.toCharArray());
            if(myReader.hasNextLine())
                line= myReader.nextLine();
            else
                break;
        }
        var lock=new int[5];
        for (int i = 0; i < 5; i++) {
            lock[i]= getColHeightBlocks(matrix,i);
        }
        return lock;
    }

    private static int getColHeightBlocks(List<char[]> matrix, int c) {
        var blockHeight=0;
        for (int r = 0; r < matrix.size(); r++) {
            if(matrix.get(r)[c]=='#')
                blockHeight++;
        }
        return blockHeight;
    }

}
