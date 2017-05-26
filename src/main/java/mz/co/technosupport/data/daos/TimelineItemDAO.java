/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.daos;

import mz.co.technosupport.data.model.TimelineItem;

import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author zJohn
 */
@ApplicationScoped
public class TimelineItemDAO extends DAO<TimelineItem> {
    
    @Override
    public Class<TimelineItem> getEntityClass() {
        return TimelineItem.class;
    }
}
