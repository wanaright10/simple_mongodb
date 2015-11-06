package com.simple.mongodb.factory;

import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * Created by Dong Wang.
 * Created on 14-4-1 下午4:42.
 * <p>
 * the mongodb connect factory and support mongo connect client from connect pool
 * this will support the method for create DBCollection which you want to use
 */
public interface MongoDBFactory {

    static DB getDB(String dbName) {
        return SingletonMongo.INSTANCE.getDBByName(dbName);
    }

    static DBCollection getCollection(String dbName, String collection) {
        return getDB(dbName).getCollection(collection);
    }
}
