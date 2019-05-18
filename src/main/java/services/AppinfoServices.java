package services;

import entity.Appinfo;

import java.util.List;

/**
 * @author qiaoying
 * @date 2019/5/18 15:01
 */
public interface AppinfoServices {

    List<Appinfo> getAllAppinfo(String tableName);

    int updateDownload(String id,String tableName);
}
