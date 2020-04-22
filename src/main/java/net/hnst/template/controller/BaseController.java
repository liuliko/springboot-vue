package net.hnst.template.controller;

import net.hnst.template.api.pagination.AdvancedPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {

    /**
     * 必须封装后返回到前段
     *
     * @param o
     * @return
     */
    protected Object getMessage(Object o) {
        Map map = new HashMap();
        map.put("msg", 20000);
        map.put("data", o);
        return map;
    }

    protected Object getDataTableResultMap(List list, AdvancedPage advancedPage) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("aaData", list);
        resultMap.put("sEcho", advancedPage.getsEcho());
        resultMap.put("iTotalRecords", advancedPage.getiTotalRecords());
        resultMap.put("iTotalDisplayRecords", advancedPage.getiTotalDisplayRecords());
        resultMap.put("message", advancedPage.getMessage());
        resultMap.put("iDisplayLength", advancedPage.getiDisplayLength());
        resultMap.put("iDisplayStart", advancedPage.getiDisplayStart());
        return getMessage(resultMap);
    }
}
