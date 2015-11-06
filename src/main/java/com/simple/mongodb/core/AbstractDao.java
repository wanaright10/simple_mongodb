package com.simple.mongodb.core;

import com.mongodb.*;
import com.simple.mongodb.util.BeanUtil;
import com.simple.mongodb.util.EmptyUtil;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dong Wang.
 * Created on 2014/9/5 23:56.
 * <p>
 * DAO super class for crud operates
 * This can save bean, query bean, set bean value by objectId or another key value
 * Base DAO must extends this for use
 */
public abstract class AbstractDao<T> implements MongoDao<T> {
    /**
     * save an object bean to mongo db
     * if bean contains id, this will do update operate
     * else this will do insert operate
     * will return nothing by the riteConcernSafe is false
     */
    public void save(T bean) {
        save(bean, false);
    }

    /**
     * save an object bean to mongo db
     * if bean contains id, this will do update operate
     * else this will do insert operate
     * will return it's objectId value if the writeConcernSafe is true
     */
    public ObjectId save(T bean, boolean writeConcernSafe) {
        ObjectId result = null;
        DBCollection collection = getDaoCollection();
        DBObject object = BeanUtil.beanToDBObject(bean);

        if (writeConcernSafe) {
            collection.save(object, WriteConcern.SAFE);
            result = (ObjectId) object.get(BeanUtil._ID);
        } else {
            collection.save(object);
        }
        return result;
    }

    /**
     * just insert bean to mongo db
     * and return nothing by the riteConcernSafe is false
     */
    public void insert(T bean) {
        insert(bean, false);
    }

    /**
     * just insert bean to mongo db
     * and return the objectId if the riteConcernSafe is true
     */
    public ObjectId insert(T bean, boolean writeConcernSafe) {
        ObjectId result = null;
        DBCollection collection = getDaoCollection();
        DBObject object = BeanUtil.beanToDBObject(bean);

        if (writeConcernSafe) {
            collection.insert(object, WriteConcern.SAFE);
            result = (ObjectId) object.get(BeanUtil._ID);
        } else {
            collection.insert(object);
        }
        return result;
    }

    /**
     * 根据javaBean的ObjectId更新文档
     */
    public void updateBeanByObjectId(ObjectId objectId, T bean) {
        updateBeanByFieldValue(BeanUtil._ID, objectId, bean);
    }

    @Override
    public void updateFieldsByObjectId(ObjectId objectId, Map updateMap) {
        updateFieldsByFieldValue(BeanUtil._ID, objectId, updateMap);
    }

    /**
     * update all collection by query field and it's value
     * if query is bean's id (mongo db's objectId)
     * you can use save() method replaced
     */
    public void updateBeanByFieldValue(String queryField, Object queryValue, T bean) {
        DBCollection collection = getDaoCollection();

        DBObject query = new BasicDBObject();
        query.put(queryField, queryValue);

        collection.update(query, BeanUtil.beanToDBObject(bean));
    }

    @Override
    public void updateFieldsByFieldValue(String queryField, Object queryValue, Map updateMap) {
        DBCollection collection = getDaoCollection();

        DBObject query = new BasicDBObject();
        query.put(queryField, queryValue);

        if (!EmptyUtil.isNullOrEmpty(updateMap)) {
            DBObject updateDoc = new BasicDBObject();
            updateDoc.putAll(updateMap);

            collection.update(query, updateDoc);
        }
    }

    /**
     * update all collection by any query parameter map
     * if query is bean's id (mongo db's objectId)
     * you can use save() method replaced
     */
    public void updateBeanByParamMap(Map queryParam, T bean) {
        DBCollection collection = getDaoCollection();

        DBObject query = new BasicDBObject();
        if (!EmptyUtil.isNullOrEmpty(queryParam)) {
            query.putAll(queryParam);
        }

        collection.update(query, BeanUtil.beanToDBObject(bean));
    }

    @Override
    public void updateFieldsByParamMap(Map queryParam, Map updateMap) {
        DBCollection collection = getDaoCollection();

        DBObject query = new BasicDBObject();
        if (!EmptyUtil.isNullOrEmpty(queryParam)) {
            query.putAll(queryParam);
        }

        if (!EmptyUtil.isNullOrEmpty(updateMap)) {
            DBObject updateDoc = new BasicDBObject();
            updateDoc.putAll(updateMap);

            collection.update(query, updateDoc);
        }
    }

    /**
     * remove collection from mongodb
     * by objectId's value
     */
    public void removeByObjectId(ObjectId objectId) {
        removeByFieldValue(BeanUtil._ID, objectId);
    }

    /**
     * remove collection from mongodb
     * by any field and it's value
     */
    public void removeByFieldValue(String field, Object fieldValue) {
        DBCollection collection = getDaoCollection();

        DBObject query = new BasicDBObject();
        query.put(field, fieldValue);

        collection.remove(query);
    }

    /**
     * remove collection from mongodb
     * by any query parameter map
     */
    public void removeByParamMap(Map queryParam) {
        if (!EmptyUtil.isNullOrEmpty(queryParam)) {
            DBCollection collection = getDaoCollection();

            DBObject query = new BasicDBObject();
            query.putAll(queryParam);

            collection.remove(query);
        }
    }

    /**
     * set collection field value
     * by objectId's value
     * and will match one and not multi operate
     */
    public void setOneFieldByObjectId(ObjectId objectId, String setField, Object setValue) {
        setOneFieldByFieldValue(BeanUtil._ID, objectId, setField, setValue, false, false);
    }

    /**
     * set collection field value
     * by any field and it's value
     * and will match one and not multi operate
     */
    public void setOneFieldByFieldValue(String queryField, Object queryValue, String setField, Object setValue) {
        setOneFieldByFieldValue(queryField, queryValue, setField, setValue, false, false);
    }

    /**
     * set collection field value
     * by any query parameter map
     * and will match one and not multi operate
     */
    public void setOneFieldByParamMap(Map queryParam, String setField, Object setValue) {
        setOneFieldByParamMap(queryParam, setField, setValue, false, false);
    }

    /**
     * set collection field value
     * by any field and it's value
     * and will match more collections if upsert is true and can do multi operate if multi is true
     */
    public void setOneFieldByFieldValue(String queryField, Object queryValue, String setField, Object setValue, boolean upsert, boolean multi) {
        operateOneFieldByFieldValue(queryField, queryValue, setField, setValue, upsert, multi, MongoOperators.SET);
    }

    /**
     * set collection field value
     * by any query parameter map
     * and will match more collections if upsert is true and can do multi operate if multi is true
     */
    public void setOneFieldByParamMap(Map queryParam, String setField, Object setValue, boolean upsert, boolean multi) {
        operateOneFieldByParamMap(queryParam, setField, setValue, upsert, multi, MongoOperators.SET);
    }

    @Override
    public void setMultiFieldByObjectId(ObjectId objectId, Map setMap) {
        Map<String, ObjectId> queryParam = new HashMap<>();
        queryParam.put(BeanUtil._ID, objectId);

        operateMulitFieldByParamMap(queryParam, setMap, false, false, MongoOperators.SET);
    }

    @Override
    public void setMultiFieldByFieldValue(String queryField, Object queryValue, Map setMap) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put(queryField, queryValue);

        operateMulitFieldByParamMap(queryParam, setMap, false, false, MongoOperators.SET);
    }

    @Override
    public void setMultiFieldByParamMap(Map queryParam, Map setMap) {
        operateMulitFieldByParamMap(queryParam, setMap, false, false, MongoOperators.SET);
    }

    @Override
    public void setMultiFieldByFieldValue(String queryField, Object queryValue, Map setMap, boolean upsert, boolean multi) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put(queryField, queryValue);

        operateMulitFieldByParamMap(queryParam, setMap, upsert, multi, MongoOperators.SET);
    }

    @Override
    public void setMultiFieldByParamMap(Map queryParam, Map setMap, boolean upsert, boolean multi) {
        operateMulitFieldByParamMap(queryParam, setMap, upsert, multi, MongoOperators.SET);
    }

    /**
     * increase one number field value with increaseCount
     * by objectId's value
     * and will match one and not multi operate
     */
    public void increaseOneFieldNumberByObjectId(ObjectId objectId, String increaseField, Number increaseCount) {
        increaseOneFieldNumberByFieldValue(BeanUtil._ID, objectId, increaseField, increaseCount);
    }

    @Override
    public void increaseMultiFieldNumberByObjectId(ObjectId objectId, Map increaseMap) {
        Map<String, ObjectId> queryParam = new HashMap<>();
        queryParam.put(BeanUtil._ID, objectId);

        operateMulitFieldByParamMap(queryParam, increaseMap, false, false, MongoOperators.INC);
    }

    /**
     * increase one number field value with increaseCount
     * by any field and it's value
     * and will match one and not multi operate
     */
    public void increaseOneFieldNumberByFieldValue(String queryField, Object queryValue, String increaseField, Number increaseCount) {
        increaseOneFieldNumberByFieldValue(queryField, queryValue, increaseField, increaseCount, false, false);
    }

    @Override
    public void increaseMultiFieldNumberByFieldValue(String queryField, Object queryValue, Map increaseMap) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put(queryField, queryValue);

        operateMulitFieldByParamMap(queryParam, increaseMap, false, false, MongoOperators.INC);
    }

    /**
     * increase one number field value with increaseCount
     * by any query parameter map
     * and will match one and not multi operate
     */
    public void increaseOneFieldNumberByParamMap(Map queryParam, String increaseField, Number increaseCount) {
        increaseOneFieldNumberByParamMap(queryParam, increaseField, increaseCount, false, false);
    }

    @Override
    public void increaseMultiFieldNumberByParamMap(Map queryParam, Map increaseMap) {
        operateMulitFieldByParamMap(queryParam, increaseMap, false, false, MongoOperators.INC);
    }

    /**
     * increase one number field value with increaseCount
     * by any field and it's value
     * and will match more collections if upsert is true and can do multi operate if multi is true
     */
    public void increaseOneFieldNumberByFieldValue(String queryField, Object queryValue, String increaseField, Number increaseCount, boolean upsert, boolean multi) {
        operateOneFieldByFieldValue(queryField, queryValue, increaseField, increaseCount, upsert, multi, MongoOperators.INC);
    }

    @Override
    public void increaseMultiFieldNumberByFieldValue(String queryField, Object queryValue, Map increaseMap, boolean upsert, boolean multi) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put(queryField, queryValue);

        operateMulitFieldByParamMap(queryParam, increaseMap, upsert, multi, MongoOperators.INC);
    }

    /**
     * increase one number field value with increaseCount
     * by any query parameter map
     * and will match more collections if upsert is true and can do multi operate if multi is true
     */
    public void increaseOneFieldNumberByParamMap(Map queryParam, String increaseField, Number increaseCount, boolean upsert, boolean multi) {
        operateOneFieldByParamMap(queryParam, increaseField, increaseCount, upsert, multi, MongoOperators.INC);
    }

    @Override
    public void increaseMultiFieldNumberByParamMap(Map queryParam, Map increaseMap, boolean upsert, boolean multi) {
        operateMulitFieldByParamMap(queryParam, increaseMap, upsert, multi, MongoOperators.INC);
    }

    /**
     * 根据objectId操作文档
     */
    public void operateOneFieldByObjectId(ObjectId objectId, String updateField, Object updateValue, boolean upsert, boolean multi, String operateCmd) {
        operateOneFieldByFieldValue(BeanUtil._ID, objectId, updateField, updateValue, upsert, multi, operateCmd);
    }

    @Override
    public void operateMultiFieldByObjectId(ObjectId objectId, Map operateMap, boolean upsert, boolean multi, String operateCmd) {
        Map<String, ObjectId> queryParam = new HashMap<>();
        queryParam.put(BeanUtil._ID, objectId);

        operateMulitFieldByParamMap(queryParam, operateMap, upsert, multi, operateCmd);
    }

    /**
     * operate mongo db's collection by the operateCmd
     * by any field and it's value
     * and will match more collections if upsert is true and can do multi operate if multi is true
     */
    public void operateOneFieldByFieldValue(String queryField, Object queryValue, String updateField, Object updateValue, boolean upsert, boolean multi, String operateCmd) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put(queryField, queryValue);

        operateOneFieldByParamMap(queryParam, updateField, updateValue, upsert, multi, operateCmd);
    }

    @Override
    public void operateMultiFieldByFieldValue(String queryField, Object queryValue, Map operateMap, boolean upsert, boolean multi, String operateCmd) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put(queryField, queryValue);

        operateMulitFieldByParamMap(queryParam, operateMap, upsert, multi, operateCmd);
    }

    /**
     * operate mongo db's collection by the operateCmd
     * by any query parameter map
     * and will match more collections if upsert is true and can do multi operate if multi is true
     */
    public void operateOneFieldByParamMap(Map queryParam, String updateField, Object updateValue, boolean upsert, boolean multi, String operateCmd) {
        Map<String, Object> operateMap = new HashMap<>();
        operateMap.put(updateField, updateValue);

        operateMulitFieldByParamMap(queryParam, operateMap, upsert, multi, operateCmd);
    }

    @Override
    public void operateMulitFieldByParamMap(Map queryParam, Map operateMap, boolean upsert, boolean multi, String operateCmd) {
        DBCollection collection = getDaoCollection();

        DBObject query = new BasicDBObject();
        if (!EmptyUtil.isNullOrEmpty(queryParam)) {
            query.putAll(queryParam);
        }

        if (!EmptyUtil.isNullOrEmpty(operateMap)) {
            DBObject operateDoc = new BasicDBObject();
            operateDoc.putAll(operateMap);

            DBObject set = new BasicDBObject();
            set.put(operateCmd, operateDoc);

            collection.update(query, set, upsert, multi);
        }
    }

    /**
     * query full collection list by objectId
     * and build the bean form the collection object
     * and it can set list sort by this format:
     * sortField,sortWay,sortField,sortWay...
     * like _id,-1,time,1...
     */
    public List<T> queryListByObjectId(ObjectId objectId, Class<? extends T> clazz, Object... sort) {
        return queryListByFieldValue(BeanUtil._ID, objectId, clazz, sort);
    }

    /**
     * query full collection list by any field and it's value
     * if queryField is null will return all collections
     * and build the bean form the collection object
     * and it can set list sort by this format:
     * sortField,sortWay,sortField,sortWay...
     * like _id,-1,time,1...
     */
    public List<T> queryListByFieldValue(String queryField, Object queryValue, Class<? extends T> clazz, Object... sort) {
        DBObject query = null;
        if (!EmptyUtil.isNullOrEmpty(queryField)) {
            query = new BasicDBObject();
            query.put(queryField, queryValue);
        }
        return queryListByCursor(query, clazz, sort);
    }

    /**
     * query full collection list by any query parameter map
     * if queryField is null will return all collections
     * and build the bean form the collection object
     * and it can set list sort by this format:
     * sortField,sortWay,sortField,sortWay...
     * like _id,-1,time,1...
     */
    public List<T> queryListByParamMap(Map queryParam, Class<? extends T> clazz, Object... sort) {
        DBObject query = null;
        if (!EmptyUtil.isNullOrEmpty(queryParam)) {
            query = new BasicDBObject();
            query.putAll(queryParam);
        }
        return queryListByCursor(query, clazz, sort);
    }

    /**
     * query full collection list by objectId
     * and add pagination function
     * and build the bean form the collection object
     * and it can set list sort by this format:
     * sortField,sortWay,sortField,sortWay...
     * like _id,-1,time,1...
     */
    public List<T> queryListByObjectIdForPage(ObjectId objectId, Class<? extends T> clazz, int page, int pageSize, Object... sort) {
        return queryListByFieldValueForPage(BeanUtil._ID, objectId, clazz, page, pageSize);
    }

    /**
     * query full collection list by any field and it's value
     * if queryField is null will return all collections
     * and add pagination function
     * and build the bean form the collection object
     * and it can set list sort by this format:
     * sortField,sortWay,sortField,sortWay...
     * like _id,-1,time,1...
     */
    public List<T> queryListByFieldValueForPage(String queryField, Object queryValue, Class<? extends T> clazz, int page, int pageSize, Object... sort) {
        DBObject query = null;
        if (!EmptyUtil.isNullOrEmpty(queryField)) {
            query = new BasicDBObject();
            query.put(queryField, queryValue);
        }
        return queryListByCursorForPage(query, clazz, page, pageSize, sort);
    }

    /**
     * query full collection list by query parameter map
     * if queryField is null will return all collections
     * and add pagination function
     * and build the bean form the collection object
     * and it can set list sort by this format:
     * sortField,sortWay,sortField,sortWay...
     * like _id,-1,time,1...
     */
    public List<T> queryListByParamMapForPage(Map queryParam, Class<? extends T> clazz, int page, int pageSize, Object... sort) {
        DBObject query = null;
        if (!EmptyUtil.isNullOrEmpty(queryParam)) {
            query = new BasicDBObject();
            query.putAll(queryParam);
        }
        return queryListByCursorForPage(query, clazz, page, pageSize, sort);
    }

    /**
     * query collection count witch match the objectId
     */
    public int queryCountByObjectId(ObjectId objectId) {
        return queryCountByFieldValue(BeanUtil._ID, objectId);
    }

    /**
     * query collection count witch match the any query field and it's value
     */
    public int queryCountByFieldValue(String queryField, Object queryValue) {
        DBCollection collection = getDaoCollection();

        DBObject query = new BasicDBObject();
        query.put(queryField, queryValue);
        return (int) collection.count(query);
    }

    /**
     * query collection count witch match the any query parameters map
     */
    public int queryCountByParamMap(Map queryParam) {
        DBCollection collection = getDaoCollection();

        DBObject query = new BasicDBObject();
        if (!EmptyUtil.isNullOrEmpty(queryParam)) {
            query.putAll(queryParam);
        }
        return (int) collection.count(query);
    }

    /**
     * query one collection by mongo db objectId
     * and build to bean
     */
    public T queryOneByObjectId(ObjectId objectId, Class<? extends T> clazz) {
        return queryOneByFieldValue(BeanUtil._ID, objectId, clazz);
    }

    /**
     * query one collection by field and it's value
     * and build to bean
     */
    public T queryOneByFieldValue(String queryField, Object queryValue, Class<? extends T> clazz) {
        DBCollection collection = getDaoCollection();

        DBObject query = new BasicDBObject();
        query.put(queryField, queryValue);

        DBObject object = collection.findOne(query);
        return BeanUtil.dbObjectToBean(object, clazz);
    }

    /**
     * query one collection by query map
     * and build to bean
     */
    public T queryOneByParamMap(Map queryParam, Class<? extends T> clazz) {
        DBCollection collection = getDaoCollection();

        DBObject query = new BasicDBObject();
        if (!EmptyUtil.isNullOrEmpty(queryParam)) {
            query.putAll(queryParam);
        }

        DBObject object = collection.findOne(query);
        return BeanUtil.dbObjectToBean(object, clazz);
    }

    /**
     * 根据objectId查询文档，指定了返回字段
     */
    public DBObject queryOneByObjectIdWithFilter(ObjectId objectId, List<String> requiredKeys) {
        return queryOneByFieldValueWithKeyFilter(BeanUtil._ID, objectId, requiredKeys);
    }

    /**
     * 根据key，value查询文档，指定了返回字段
     */
    public DBObject queryOneByFieldValueWithKeyFilter(String queryField, Object queryValue, List<String> requiredKeys) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put(queryField, queryValue);

        return queryOneByParamMapWithKeyFilter(queryParam, requiredKeys);
    }

    /**
     * 根据map条件查询文档，指定了返回字段
     */
    public DBObject queryOneByParamMapWithKeyFilter(Map queryParam, List<String> requiredKeys) {
        DBCollection collection = getDaoCollection();
        DBObject result;

        DBObject query = new BasicDBObject();
        if (!EmptyUtil.isNullOrEmpty(queryParam)) {
            query.putAll(queryParam);
        }

        final DBObject needDoc = new BasicDBObject();
        if (requiredKeys.size() > 0) {
            requiredKeys.parallelStream().forEach(key -> needDoc.put(key, 1));
            result = collection.findOne(query, needDoc);
        } else {
            result = collection.findOne(query);
        }
        return result;
    }

    /**
     * 根据objectId查询文档，指定了返回字段
     */
    public List<DBObject> queryListByObjectIdWithFilter(ObjectId objectId, List<String> requiredKeys) {
        return queryListByFieldValueWithKeyFilter(BeanUtil._ID, objectId, requiredKeys);
    }

    /**
     * 根据key，value查询文档，指定了返回字段
     */
    public List<DBObject> queryListByFieldValueWithKeyFilter(String queryField, Object queryValue, List<String> requiredKeys) {
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put(queryField, queryValue);

        return queryListByParamMapWithKeyFilter(queryParam, requiredKeys);
    }

    /**
     * 根据map条件查询文档，指定了返回字段
     */
    public List<DBObject> queryListByParamMapWithKeyFilter(Map queryParam, List<String> requiredKeys) {
        DBCollection collection = getDaoCollection();

        DBObject query = null;
        if (!EmptyUtil.isNullOrEmpty(queryParam)) {
            query = new BasicDBObject();
            query.putAll(queryParam);
        }

        final DBObject needDoc = new BasicDBObject();
        DBCursor cursor;
        if (requiredKeys.size() > 0) {
            requiredKeys.parallelStream().forEach(key -> needDoc.put(key, 1));
            cursor = collection.find(query, needDoc);
        } else {
            cursor = collection.find(query);
        }

        return cursor.toArray();
    }

    /**
     * children class must overwrite this method for offer it's collection name
     */
    public abstract DBCollection getDaoCollection();

    /**
     * query collection by DBCursor
     * add pagination function
     */
    private List<T> queryListByCursorForPage(DBObject query, Class<? extends T> clazz, int page, int pageSize, Object... sort) {
        List<T> result = new ArrayList<>();
        DBCollection collection = getDaoCollection();

        DBCursor cursor;
        if (sort.length > 0) {
            DBObject sortDoc = new BasicDBObject();
            for (int i = 0; i < sort.length; i = i + 2) {
                sortDoc.put(String.valueOf(sort[i]), Integer.parseInt(String.valueOf(sort[i + 1])));
            }

            cursor = collection.find(query).sort(sortDoc).skip((page - 1) * pageSize).limit(pageSize);
        } else {
            cursor = collection.find(query).skip((page - 1) * pageSize).limit(pageSize);
        }

        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            T instance = BeanUtil.dbObjectToBean(object, clazz);
            result.add(instance);
        }
        return result;
    }

    /**
     * query collection by DBCursor
     */
    private List<T> queryListByCursor(DBObject query, Class<? extends T> clazz, Object... sort) {
        List<T> result = new ArrayList<>();
        DBCollection collection = getDaoCollection();

        DBCursor cursor;
        if (sort.length > 0) {
            DBObject sortDoc = new BasicDBObject();
            for (int i = 0; i < sort.length; i = i + 2) {
                sortDoc.put(String.valueOf(sort[i]), Integer.parseInt(String.valueOf(sort[i + 1])));
            }

            cursor = collection.find(query).sort(sortDoc);
        } else {
            cursor = collection.find(query);
        }

        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            T instance = BeanUtil.dbObjectToBean(object, clazz);
            result.add(instance);
        }
        return result;
    }
}
