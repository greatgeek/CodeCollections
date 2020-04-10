
import java.io.*;
import java.util.*;

public class Main {

    /**
     * Contruct map from file
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public static HashMap<Integer, List<Integer>> contructMap(String fileName) throws IOException {
        FileInputStream fin = new FileInputStream(new File(fileName));
        Scanner sc = new Scanner(fin);
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        while (sc.hasNext()) {
            String[] strArr = sc.nextLine().split(",");
            int a = Integer.parseInt(strArr[0]);
            int b = Integer.parseInt(strArr[1]);
            if (!map.containsKey(a)) {
                List<Integer> list = new LinkedList<>();
                list.add(b);
                map.put(a, list);
            } else {
                map.get(a).add(b);
            }

            if(!map.containsKey(b)){
                List<Integer> list = new LinkedList<>();
                map.put(b,list);
            }
        }

        fin.close();
        sc.close();

        for (Integer key : map.keySet()) { // 给邻接表升序排列
            Collections.sort(map.get(key));
        }
        return map;
    }

    /**
     * 找出环
     */
    public static List<List<Integer>> loop = new LinkedList<>();
    public static void findLoop(HashMap<Integer, List<Integer>> map, List<Integer> list, Integer source, Integer cur) {
        list.add(cur);
        for (Integer node : map.get(cur)) {
            if(node.compareTo(source)==0 && list.size()>=3 && list.size()<=7){
                loop.add(new LinkedList<>(list));
            }
            if(list.size()<7 && !list.contains(node) && node.compareTo(source)>0){ // 搜索过程中id 比起始节点小或者已包含都跳过
                findLoop(map, list, source, node);
            }
        }

        if (list.size() > 0) {
            list.remove(list.size() - 1);
        }
    }


    /**
     * 将结果写入文件
     *
     * @param loop
     * @throws IOException
     */
    public static void writeIntoFile(List<List<Integer>> loop) throws IOException {
        File file = new File("/projects/student/result.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(loop.size() + "\n");
        for (List<Integer> list : loop) {
            String line = list.toString();
            line = line.substring(1, line.length() - 1);
            line = line.replace(" ", "");
            fileWriter.write(line + "\n");
        }
        fileWriter.close();
    }


    public static void main(String[] args) throws IOException {

        long timeBegin = System.currentTimeMillis();
        HashMap<Integer, List<Integer>> map = contructMap("/data/test_data.txt");

        List<Integer> idList = new LinkedList<>(map.keySet());
        Collections.sort(idList);
        for (Integer id : idList) { // 按照升序后的ID进行搜索
            findLoop(map, new LinkedList<>(), id, id);
        }

        Collections.sort(loop, new Comparator<List<Integer>>() { // 按照输出格式排序
            @Override
            public int compare(List<Integer> l1, List<Integer> l2) {
                if (l1.size() == l2.size()) {
                    for (int i = 0; i < l1.size(); i++) {
                        if (l1.get(i).compareTo(l2.get(i)) != 0) {
                            return l1.get(i).compareTo(l2.get(i));
                        }
                    }
                }
                return l1.size() - l2.size();
            }
        });

        writeIntoFile(loop);// 将结果写入文件
        long timeEnd = System.currentTimeMillis();
        System.out.println("执行完成:" + (timeEnd - timeBegin) + "ms");
    }
}
