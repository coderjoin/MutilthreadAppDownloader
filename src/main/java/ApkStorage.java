import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.springframework.stereotype.Component;
import services.AppinfoServices;
import services.impl.AppinfoServicesImpl;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by qiaoying on 2017/7/26.
 */
@Component
public class ApkStorage {


    @Resource
    private AppinfoServices appinfoServices;
    private final static String APK_STORAGE_ROOT = "/mnt/apk/huawei/20190515_all";//"F:/huawei";
    //private final static String APK_STORAGE_ROOT = "F:/huawei";
    public  void saveApk(String id,String appDownUrl) throws IOException {

            URL url = new URL(appDownUrl);

            HttpURLConnection httpUrl = (HttpURLConnection)url.openConnection();
//            应用宝参数
            httpUrl.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");

            httpUrl.setRequestProperty("Content-Type", "image/gif");
            httpUrl.setRequestProperty("Content-Language", "zh-cn");
            httpUrl.setRequestProperty("Accept-Ranges", "bytes");
            httpUrl.setRequestProperty("Cache-Control", "max-age=0");

            //百度参数
//            httpUrl.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36");
//
//            httpUrl.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//            httpUrl.setRequestProperty("Accept-Encoding", "gzip, deflate");
//            httpUrl.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
//            httpUrl.setRequestProperty("Connection", "keep-alive");
//            httpUrl.setRequestProperty("Cookie", "BAIDUID=6A3C47C4A1CD8DCC7B1F227A4B400D3C:FG=1; BIDUPSID=6A3C47C4A1CD8DCC7B1F227A4B400D3C; PSTM=1535950200; MCITY=-333%3A289%3A; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; delPer=0; H_PS_PSSID=1437_21125");
//            httpUrl.setRequestProperty("Host", "gdown.baidu.com");
//            httpUrl.setRequestProperty("Upgrade-Insecure-Requests", "1");

              //          qihoo参数
//            httpUrl.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
//
//            httpUrl.setRequestProperty("Content-Type", "image/gif");
//            httpUrl.setRequestProperty("Content-Language", "zh-cn");
//            httpUrl.setRequestProperty("Accept-Ranges", "bytes");
//            httpUrl.setRequestProperty("Cache-Control", "max-age=0");
            int state = httpUrl.getResponseCode();
            if (state == 200) {
                httpUrl.connect();
                //System.out.println(httpUrl.getContent());
                BufferedInputStream apkInputStream = new BufferedInputStream(httpUrl.getInputStream());
                //System.out.println(apkInputStream.available());
                ByteOutputStream bufferOutputStream = new ByteOutputStream();

//            int bisLength = apkInputStream.available();
//            System.out.println(bisLength);

                byte[] buffer = new byte[512];
                int size = 0;
                while ((size = apkInputStream.read(buffer)) != -1) {
                    bufferOutputStream.write(buffer,0, size);
                }

                byte[] temp = bufferOutputStream.toByteArray();
                String apkStoreDir = APK_STORAGE_ROOT;
                File rootDir = new File(apkStoreDir);
                if (!rootDir.exists())
                    rootDir.mkdirs();

                File apkFile = new File(rootDir, id+".apk");
                if (! apkFile.exists()) {
                    FileOutputStream apkOutputStream = new FileOutputStream(apkFile);
                    apkOutputStream.write(temp);
                    apkOutputStream.close();
                }

                bufferOutputStream.close();
                apkInputStream.close();
                httpUrl.disconnect();

                appinfoServices.updateDownload(id, "apps_huawei_20190515_all");
                System.out.println("app:" + id + "has downloaded");
                //AppInfoDao.updateIsDownloaded(pkgName,"apps_huawei_20190515_all");

            }else {
                System.out.println("false");
            }

    }




}
