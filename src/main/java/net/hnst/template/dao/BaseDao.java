package net.hnst.template.dao;


import java.util.List;

public interface BaseDao<T> {

    int deleteByPrimaryKey(String id);


    int insert(T record);


    int insertSelective(T record);


    int updateByPrimaryKeySelective(T record);


    int updateByPrimaryKey(T record);


    T selectByPrimaryKey(String id);


    List<T> selectList(T record);


    List<T> select_page(T record);


    int insertByBatch(List<T> list);


    int deleteByBatch(List<String> list);
}
