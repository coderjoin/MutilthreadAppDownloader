import entity.Appinfo;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import services.AppinfoServices;

import javax.annotation.Resource;
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

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("mybaties.xml");

        AppinfoServices appinfoServices = context.getBean(AppinfoServices.class);
        List<Appinfo> list = appinfoServices.getAllAppinfo("apps_huawei_20190515_all");

//        RequestsFactory requestsFactory = context.getBean(RequestsFactory.class);
//        List<ApkDownloadInfo> list = AppInfoDao.retrieve("apps_huawei_20190515_all");
////
        ApkStorage apkStorage = context.getBean(ApkStorage.class);
        Worker.create(apkStorage).thread(30).startUrl(list).run();
//        Worker worker = new Worker();
//        worker.thread(5).startUrl(list).run();
    }
}
