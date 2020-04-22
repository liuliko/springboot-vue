package net.hnst.template.service.test.impl;

import net.hnst.template.api.test.SetSystemPrice;
import net.hnst.template.dao.test.SetSystemPriceDao;
import net.hnst.template.service.BaseServiceImpl;
import net.hnst.template.service.test.SetSystemPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class SetSystemPriceServiceImpl extends BaseServiceImpl<SetSystemPrice> implements SetSystemPriceService {

    @Autowired
    public void setTargetDao(SetSystemPriceDao setSystemPricedao){
            super.targetDao=setSystemPricedao;
    };

}