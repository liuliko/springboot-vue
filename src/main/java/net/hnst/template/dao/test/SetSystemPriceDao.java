package net.hnst.template.dao.test;

import java.util.List;
import net.hnst.template.api.test.SetSystemPrice;
import net.hnst.template.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface SetSystemPriceDao extends BaseDao<SetSystemPrice>  {

    int deleteByPrimaryKey(String id);


    int insert(SetSystemPrice record);


    int insertSelective(SetSystemPrice record);


    int updateByPrimaryKeySelective(SetSystemPrice record);


    int updateByPrimaryKey(SetSystemPrice record);


    SetSystemPrice selectByPrimaryKey(String id);


    List<SetSystemPrice> selectList(SetSystemPrice record);


    List<SetSystemPrice> select_page(SetSystemPrice record);


    int insertByBatch(List<SetSystemPrice> list);


    int deleteByBatch(List<String> list);
}