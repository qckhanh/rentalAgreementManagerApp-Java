package org.rmit.database;

import java.util.List;

public interface ImagesDAO<T> {
    List<byte[]> getImageByID(int id);
}
