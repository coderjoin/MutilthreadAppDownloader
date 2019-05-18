package dao;

import entity.Appinfo;
import entity.AppinfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppinfoMapper {
    int countByExample(AppinfoExample example);

    int deleteByExample(AppinfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(Appinfo record);

    int insertSelective(Appinfo record);

    List<Appinfo> selectByExampleWithBLOBs(AppinfoExample example);

    List<Appinfo> selectByExample(AppinfoExample example);

    Appinfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Appinfo record, @Param("example") AppinfoExample example);

    int updateByExampleWithBLOBs(@Param("record") Appinfo record, @Param("example") AppinfoExample example);

    int updateByExample(@Param("record") Appinfo record, @Param("example") AppinfoExample example);

    int updateByPrimaryKeySelective(Appinfo record);

    int updateByPrimaryKeyWithBLOBs(Appinfo record);

    int updateByPrimaryKey(Appinfo record);

    //add
    List<Appinfo> getAllAppinfo(@Param("tableName") String tableName);

    int updateDownload(@Param("id") String id, @Param("tableName")String tableName);

}