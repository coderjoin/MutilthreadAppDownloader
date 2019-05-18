package dao;

import entity.SearchKeywords;
import entity.SearchKeywordsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SearchKeywordsMapper {
    int countByExample(SearchKeywordsExample example);

    int deleteByExample(SearchKeywordsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SearchKeywords record);

    int insertSelective(SearchKeywords record);

    List<SearchKeywords> selectByExample(SearchKeywordsExample example);

    SearchKeywords selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SearchKeywords record, @Param("example") SearchKeywordsExample example);

    int updateByExample(@Param("record") SearchKeywords record, @Param("example") SearchKeywordsExample example);

    int updateByPrimaryKeySelective(SearchKeywords record);

    int updateByPrimaryKey(SearchKeywords record);
}