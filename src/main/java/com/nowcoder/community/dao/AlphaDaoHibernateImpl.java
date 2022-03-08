package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author LQZ
 * @date 2022-03-06 14:34
 */

@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao {

    @Override
    public String select() {
        return "Hibernate";
    }
}
