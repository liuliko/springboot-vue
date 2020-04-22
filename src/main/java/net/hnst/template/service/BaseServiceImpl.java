package net.hnst.template.service;

import net.hnst.template.api.IdEnable;
import net.hnst.template.dao.BaseDao;
import org.springframework.util.StringUtils;

import java.util.*;

public abstract class BaseServiceImpl<T> {

    protected BaseDao<T> targetDao;

    private void checkId(T record) {
        if (record instanceof IdEnable) {
            IdEnable idEnable = (IdEnable) record;
            if (StringUtils.isEmpty(idEnable.getId()))
                idEnable.setId(UUID.randomUUID().toString());
        }
    }

    public int deleteByPrimaryKey(String id) {
        return targetDao.deleteByPrimaryKey(id);
    }

    public int insert(T record) {
        checkId(record);
        return targetDao.insert(record);
    }

    public int insertSelective(T record) {
        checkId(record);
        return targetDao.insertSelective(record);
    }

    public int updateByPrimaryKeySelective(T record) {
        return targetDao.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(T record) {
        return targetDao.updateByPrimaryKey(record);
    }

    public T selectByPrimaryKey(String id) {
        return targetDao.selectByPrimaryKey(id);
    }

    public List<T> selectPage(T record) {
        return targetDao.select_page(record);

    }

    public List<T> selectList(T record) {
        return targetDao.selectList(record);
    }

    public int insertByBatch(List<T> list) {
        /**
         * 在数据多的情况下需要进行数据分割
         * 一次插入至多100条数据
         */
        List<T> temps = new ArrayList<>();
        for (T t : list) {
            checkId(t);
            temps.add(t);
            if (temps.size() > 99) {
                targetDao.insertByBatch(temps);
                temps.clear();
            }
        }
        if (temps.size() > 0)
            targetDao.insertByBatch(temps);
        return list.size();
    }

    public int deleteByBatch(List<String> list) {
        return targetDao.deleteByBatch(list);
    }


    public static final String FLAG = "flag", OK = "ok", FAIL = "fail", DATA = "data", MESSAGE = "message";

    public static Map getOkMapWithData(Object data) {
        Map<Object, Object> map = new HashMap<>();
        map.put(FLAG, OK);
        map.put(DATA, data);
        return map;
    }

    public static Map getFailMapWithMessage(String message) {
        Map<Object, Object> map = new HashMap<>();
        map.put(FLAG, FAIL);
        map.put(MESSAGE, message);
        return map;
    }

}
