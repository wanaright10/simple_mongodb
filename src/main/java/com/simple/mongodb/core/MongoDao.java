package com.simple.mongodb.core;

import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Created by Dong Wang.
 * Created on 2014/9/11 21:35.
 * <p>
 * the interface for mongo database curd operate
 * must support base insert save query remove cmd
 */
public interface MongoDao<T> {
    public void save(T bean);

    public ObjectId save(T bean, boolean writeConcernSafe);

    public void insert(T bean);

    public ObjectId insert(T bean, boolean writeConcernSafe);

    public void updateBeanByObjectId(ObjectId objectId, T bean);

    public void updateBeanByFieldValue(String queryField, Object queryValue, T bean);

    public void updateBeanByParamMap(Map queryParam, T bean);

    public void updateFieldsByObjectId(ObjectId objectId, Map updateMap);

    public void updateFieldsByFieldValue(String queryField, Object queryValue, Map updateMap);

    public void updateFieldsByParamMap(Map queryParam, Map updateMap);

    public void removeByObjectId(ObjectId objectId);

    public void removeByFieldValue(String field, Object fieldValue);

    public void removeByParamMap(Map queryParam);

    public void setOneFieldByObjectId(ObjectId objectId, String setField, Object setValue);

    public void setOneFieldByFieldValue(String queryField, Object queryValue, String setField, Object setValue);

    public void setOneFieldByParamMap(Map queryParam, String setField, Object setValue);

    public void setOneFieldByFieldValue(String queryField, Object queryValue, String setField, Object setValue, boolean upsert, boolean multi);

    public void setOneFieldByParamMap(Map queryParam, String setField, Object setValue, boolean upsert, boolean multi);

    public void setMultiFieldByObjectId(ObjectId objectId, Map setMap);

    public void setMultiFieldByFieldValue(String queryField, Object queryValue, Map setMap);

    public void setMultiFieldByParamMap(Map queryParam, Map setMap);

    public void setMultiFieldByFieldValue(String queryField, Object queryValue, Map setMap, boolean upsert, boolean multi);

    public void setMultiFieldByParamMap(Map queryParam, Map setMap, boolean upsert, boolean multi);

    public void increaseOneFieldNumberByObjectId(ObjectId objectId, String increaseField, Number increaseCount);

    public void increaseMultiFieldNumberByObjectId(ObjectId objectId, Map increaseMap);

    public void increaseOneFieldNumberByFieldValue(String queryField, Object queryValue, String increaseField, Number increaseCount);

    public void increaseMultiFieldNumberByFieldValue(String queryField, Object queryValue, Map increaseMap);

    public void increaseOneFieldNumberByParamMap(Map queryParam, String increaseField, Number increaseCount);

    public void increaseMultiFieldNumberByParamMap(Map queryParam, Map increaseMap);

    public void increaseOneFieldNumberByFieldValue(String queryField, Object queryValue, String increaseField, Number increaseCount, boolean upsert, boolean multi);

    public void increaseMultiFieldNumberByFieldValue(String queryField, Object queryValue, Map increaseMap, boolean upsert, boolean multi);

    public void increaseOneFieldNumberByParamMap(Map queryParam, String increaseField, Number increaseCount, boolean upsert, boolean multi);

    public void increaseMultiFieldNumberByParamMap(Map queryParam, Map increaseMap, boolean upsert, boolean multi);

    public void operateOneFieldByObjectId(ObjectId objectId, String updateField, Object updateValue, boolean upsert, boolean multi, String operateCmd);

    public void operateMultiFieldByObjectId(ObjectId objectId, Map operateMap, boolean upsert, boolean multi, String operateCmd);

    public void operateOneFieldByFieldValue(String queryField, Object queryValue, String updateField, Object updateValue, boolean upsert, boolean multi, String operateCmd);

    public void operateMultiFieldByFieldValue(String queryField, Object queryValue, Map operateMap, boolean upsert, boolean multi, String operateCmd);

    public void operateOneFieldByParamMap(Map queryParam, String updateField, Object updateValue, boolean upsert, boolean multi, String operateCmd);

    public void operateMulitFieldByParamMap(Map queryParam, Map operateMap, boolean upsert, boolean multi, String operateCmd);

    public List<T> queryListByObjectId(ObjectId objectId, Class<? extends T> clazz, Object... sort);

    public List<T> queryListByFieldValue(String queryField, Object queryValue, Class<? extends T> clazz, Object... sort);

    public List<T> queryListByParamMap(Map queryParam, Class<? extends T> clazz, Object... sort);

    public List<T> queryListByObjectIdForPage(ObjectId objectId, Class<? extends T> clazz, int page, int pageSize, Object... sort);

    public List<T> queryListByFieldValueForPage(String queryField, Object queryValue, Class<? extends T> clazz, int page, int pageSize, Object... sort);

    public List<T> queryListByParamMapForPage(Map queryParam, Class<? extends T> clazz, int page, int pageSize, Object... sort);

    public int queryCountByObjectId(ObjectId objectId);

    public int queryCountByFieldValue(String queryField, Object queryValue);

    public int queryCountByParamMap(Map queryParam);

    public T queryOneByObjectId(ObjectId objectId, Class<? extends T> clazz);

    public T queryOneByFieldValue(String queryField, Object queryValue, Class<? extends T> clazz);

    public T queryOneByParamMap(Map queryParam, Class<? extends T> clazz);

    public DBObject queryOneByObjectIdWithFilter(ObjectId objectId, List<String> requiredKeys);

    public DBObject queryOneByFieldValueWithKeyFilter(String queryField, Object queryValue, List<String> requiredKeys);

    public DBObject queryOneByParamMapWithKeyFilter(Map queryParam, List<String> requiredKeys);

    public List<DBObject> queryListByObjectIdWithFilter(ObjectId objectId, List<String> requiredKeys);

    public List<DBObject> queryListByFieldValueWithKeyFilter(String queryField, Object queryValue, List<String> requiredKeys);

    public List<DBObject> queryListByParamMapWithKeyFilter(Map queryParam, List<String> requiredKeys);
}
