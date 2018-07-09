import java.util.List;

/**
 * Created by qiaoying on 2018/4/25.
 */
public class Test {
    public static void main(String[] args){
//        List<String> list = null;
//        try {
//             list = Files.readAllLines(Paths.get("last_communicate1.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(list.size());
        List<ApkDownloadInfo> list = AppInfoDao.retrieve("testForMutilProcessor");
//
        Worker.create(new ApkStorage()).thread(2).startUrl(list).run();
//        Worker worker = new Worker();
//        worker.thread(5).startUrl(list).run();
    }
}
