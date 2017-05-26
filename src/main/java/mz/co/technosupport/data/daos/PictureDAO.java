package mz.co.technosupport.data.daos;

import mz.co.technosupport.data.model.Picture;

/**
 * @author Americo Chaquisse
 */
public class PictureDAO extends DAO<Picture>{
    @Override
    public Class<Picture> getEntityClass() {
        return Picture.class;
    }
}
