package Year_2025.Ex_11_Reactor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution {
//    record Device(String device,List<String> outputs){    }

    public static Map<String,List<String>> getInputData(String path) throws FileNotFoundException {
        File input = new File(path);
        Scanner myReader = new Scanner(input);

        Map<String,List<String>> deviceToOutputMap=new HashMap<>();
//        List<Device> devices=new ArrayList<>();
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            var partsbyDots=line.split(":");
            var outputs=partsbyDots[1].split(" ");

            deviceToOutputMap.put(partsbyDots[0],Arrays.stream(outputs).filter(o->!o.isEmpty()).toList());
//            devices.add(new Device(partsbyDots[0], Arrays.stream(outputs).filter(o->!o.isEmpty()).toList()));
        }

        myReader.close();
        return deviceToOutputMap;
    }

    static long solutionPart1(Map<String,List<String>> deviceToOutputMap){
        var res=dfs(deviceToOutputMap, "you","out",new HashMap<>());
        return res;
    }

    static long dfs(Map<String, List<String>> deviceToOutputMap,String begin,String end,Map<String,Long> memo) {
        if(begin.equals(end))
            return 1L;

        if (memo.containsKey(begin))
            return memo.get(begin);

        var outputs=deviceToOutputMap.get(begin);
        var res=0L;

        if (outputs==null)
            return 0L;
        for (var output:outputs){
            res+=dfs(deviceToOutputMap,output,end,memo);
        }
        memo.put(begin,res);
        return res;
    }

    static long solutionPart2(Map<String,List<String>> deviceToOutputMap){
//        var res=dfs2(deviceToOutputMap, "svr",false,false,new HashMap<String,Long>());

        var svrToFft=dfs(deviceToOutputMap,"svr","fft",new HashMap<>());
        var fftToDac=dfs(deviceToOutputMap,"fft","dac",new HashMap<>());
        var dacToOut=dfs(deviceToOutputMap,"dac","out",new HashMap<>());
        return svrToFft*fftToDac*dacToOut;
    }

    static long dfs2(Map<String, List<String>> deviceToOutputMap, String begin, boolean fft,boolean dac,Map<String,Long> memo) {
        if(begin.equals("out")){
            if (fft && dac)
                return 1L;
            else
                return 0L;
        }
        if (begin.equals("dac"))
            dac=true;
        if (begin.equals("fft"))
            fft=true;

//        if (memo.containsKey(begin))
//            return memo.get(begin);


        var outputs=deviceToOutputMap.get(begin);
        var res=0L;
        for (var output:outputs){
            res+=dfs2(deviceToOutputMap,output,fft,dac,memo);
        }

//        memo.put(begin,res);
//        visited.remove(begin);
        return res;
    }

}
