package com.yemao.repository;

import com.yemao.entity.PayOrder;
import org.springframework.stereotype.Repository;

@Repository
public class AppOrderDao extends AbstractRepository<PayOrder> {


    @Override
    public PayOrder findById(String id) {
        PayOrder result = super.findById(id);
        if (result.getDel() != 1){
            return null;
        }
        return result;
    }
}
