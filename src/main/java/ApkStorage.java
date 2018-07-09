import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by qiaoying on 2017/7/26.
 */
public class ApkStorage {
    //private final static String APK_STORAGE_ROOT = "/mnt/apk2/qihoo_all_20180423_DESC";//"F:/huawei";
    private final static String APK_STORAGE_ROOT = "F:/huawei";
    public static void saveApk(String pkgName,String appDownUrl) throws IOException {
        URL url = new URL(appDownUrl);
        HttpURLConnection httpUrl = (HttpURLConnection)url.openConnection();

        httpUrl.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");

        httpUrl.setRequestProperty("Content-Type", "image/gif");
        httpUrl.setRequestProperty("Content-Language", "zh-cn");
        httpUrl.setRequestProperty("Accept-Ranges", "bytes");
        httpUrl.setRequestProperty("Cache-Control", "max-age=0");
        int state = httpUrl.getResponseCode();
        if (state == 200) {
            httpUrl.connect();
            System.out.println(httpUrl.getContent());
            BufferedInputStream apkInputStream = new BufferedInputStream(httpUrl.getInputStream());
            System.out.println(apkInputStream.available());
            ByteOutputStream bufferOutputStream = new ByteOutputStream();

//            int bisLength = apkInputStream.available();
//            System.out.println(bisLength);

            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = apkInputStream.read(buffer)) != -1) {
                bufferOutputStream.write(buffer,0, size);
            }

            byte[] temp = bufferOutputStream.toByteArray();
            String apkStoreDir = APK_STORAGE_ROOT;
            File rootDir = new File(apkStoreDir);
            if (!rootDir.exists())
                rootDir.mkdirs();

            File apkFile = new File(rootDir, pkgName+".apk");
            if (! apkFile.exists()) {
                FileOutputStream apkOutputStream = new FileOutputStream(apkFile);
                apkOutputStream.write(temp);
                apkOutputStream.close();
            }

            bufferOutputStream.close();
            apkInputStream.close();
            httpUrl.disconnect();


        }else {
            System.out.println("false");
        }

    }

}
