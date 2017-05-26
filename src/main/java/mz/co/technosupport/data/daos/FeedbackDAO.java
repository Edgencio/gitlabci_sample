package mz.co.technosupport.data.daos;

import mz.co.technosupport.data.model.Feedback;

/**
 * @author Americo Chaquisse
 */
public class FeedbackDAO extends DAO<Feedback>{
    @Override
    public Class<Feedback> getEntityClass() {
        return Feedback.class;
    }
}
