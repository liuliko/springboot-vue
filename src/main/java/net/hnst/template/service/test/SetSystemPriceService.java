package net.hnst.template.service.test;

import java.util.List;
import net.hnst.template.api.test.SetSystemPrice;
import org.springframework.transaction.annotation.Transactional;

public interface SetSystemPriceService  {

    @Transactional
    int deleteByPrimaryKey(String id);

    @Transactional
    int insert(SetSystemPrice record);

    @Transactional
    int insertSelective(SetSystemPrice record);

    @Transactional
    int updateByPrimaryKeySelective(SetSystemPrice record);

    @Transactional
    int updateByPrimaryKey(SetSystemPrice record);

    SetSystemPrice selectByPrimaryKey(String id);

    List<SetSystemPrice> selectList(SetSystemPrice record);

    List<SetSystemPrice> selectPage(SetSystemPrice record);

    @Transactional
    int insertByBatch(List<SetSystemPrice> list);

    @Transactional
    int deleteByBatch(List<String> list);
}