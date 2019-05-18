package services.impl;

import dao.AppinfoMapper;
import entity.Appinfo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import services.AppinfoServices;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qiaoying
 * @date 2019/5/18 15:01
 */
@Component
public class AppinfoServicesImpl implements AppinfoServices {

    @Resource
    private AppinfoMapper appinfoMapper;

    @Override
    public List<Appinfo> getAllAppinfo(String tableName) {
        return appinfoMapper.getAllAppinfo(tableName);
    }

    @Override
    public int updateDownload(String id, String tableName) {
        return appinfoMapper.updateDownload(id, tableName);
    }
}
