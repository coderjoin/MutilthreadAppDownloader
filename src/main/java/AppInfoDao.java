import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaoying on 2018/4/16.
 */
public class AppInfoDao {

    public static List<ApkDownloadInfo> retrieve(String tableName) {
        List<ApkDownloadInfo> list = new ArrayList<ApkDownloadInfo>();
        Connection con = null;
        con = DBHelper.getConnection();
        PreparedStatement sta = null;
        ResultSet rs = null;

        try {
            String sql = "select * from " + tableName+" where apkDownloaded=0 ORDER BY id DESC LIMIT 20000";
            sta = con.prepareStatement(sql);
            rs = sta.executeQuery();

            while(rs.next()) {
                ApkDownloadInfo appInfo = new ApkDownloadInfo();

                //appInfo.setPkgName(rs.getString("pkgName"));
                appInfo.setPkgName(rs.getString("id"));
                appInfo.setDownloadUrl(rs.getString("appDownUrl"));


                list.add(appInfo);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DBHelper.close(rs,sta, con);
        }
        System.out.println("还剩"+list.size());
        return list;
    }

    public static void updateIsDownloaded(String pkgName,String tableName){
        String sql = "update "+tableName+" set apkDownloaded=1 where id='"+pkgName+"'";
        System.out.println(sql);
        try {
            DBHelper.executeSQL(sql);
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }
}
