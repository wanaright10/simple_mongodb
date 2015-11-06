package com.simple.mongodb.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.simple.mongodb.factory.CommonLog;
import com.simple.mongodb.serializer.ObjectIdDeSerializer;
import com.simple.mongodb.serializer.ObjectIdSerializer;
import org.bson.types.ObjectId;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Dong Wang.
 * Created on 14-4-2 上午10:21.
 * <p>
 * the bean util for cover bean to DBObject or JSONObject
 * and cover back
 */
public interface BeanUtil {
    //the factory for validator
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    SerializerFeature[] config = new SerializerFeature[]{
            SerializerFeature.WriteNullBooleanAsFalse,//boolean为null时输出false
            SerializerFeature.WriteMapNullValue, //输出空置的字段
            SerializerFeature.WriteNonStringKeyAsString,//如果key不为String 则转换为String 比如Map的key为Integer
            SerializerFeature.WriteNullListAsEmpty,//list为null时输出[]
            SerializerFeature.WriteNullNumberAsZero,//number为null时输出0
            SerializerFeature.WriteNullStringAsEmpty//String为null时输出""
    };
    SerializerFeature[] config_useClassName = new SerializerFeature[]{
            SerializerFeature.WriteNullBooleanAsFalse,//boolean为null时输出false
            SerializerFeature.WriteMapNullValue, //输出空置的字段
            SerializerFeature.WriteNonStringKeyAsString,//如果key不为String 则转换为String 比如Map的key为Integer
            SerializerFeature.WriteNullListAsEmpty,//list为null时输出[]
            SerializerFeature.WriteNullNumberAsZero,//number为null时输出0
            SerializerFeature.WriteNullStringAsEmpty,//String为null时输出""
            SerializerFeature.WriteClassName//输出class的名称
    };
    SerializeConfig serializeConfig = initObjectIdSerializerConfig();

    String _ID = "_id";
    String OBJECT_ID = "objectId";
    String GET_OBJECT_ID_METHOD_NAME = "getObjectId";

    /**
     * FastJSON无法序列化MongoDB的ObjectId，需要此自定义序列化类
     */
    static SerializeConfig initObjectIdSerializerConfig() {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(ObjectId.class, new ObjectIdSerializer());
        ParserConfig.getGlobalInstance().putDeserializer(ObjectId.class, new ObjectIdDeSerializer());
        return serializeConfig;
    }

    /**
     * parse one bean to json text, and this json contains all the bean's fields whatever is null or empty
     * useClassName :
     * if true the json text will contains the bean's class name
     * it is use for return back to bean
     */
    static String objectToJsonString(Object object, boolean useClassName) {
        String jsonText;
        if (useClassName) {
            jsonText = JSONObject.toJSONString(object, serializeConfig, config_useClassName);
        } else {
            jsonText = objectToJsonString(object);
        }
        return jsonText;
    }

    /**
     * parse one bean to json text, and this json contains all the bean's fields whatever is null or empty
     */
    static String objectToJsonString(Object object) {
        return JSONObject.toJSONString(object, serializeConfig, config);
    }

    /**
     * parse a BasicDBList to a List Object
     * clazz :
     * the List object's class type
     */
    static <T> List<T> dbObjectToList(BasicDBList basicDBList, Class<T> clazz) {
        List<T> result = new ArrayList<>();

        List<DBObject> list = basicDBList.parallelStream().map(paramInBasicDBList -> {
            DBObject dbObject = (DBObject) paramInBasicDBList;

            if (dbObject.containsField(_ID)) {
                Object value = dbObject.removeField(_ID);
                dbObject.put(OBJECT_ID, value);
            }
            return dbObject;
        }).collect(Collectors.toList());

        if (!EmptyUtil.isNullOrEmpty(list)) {
            String jsonText = objectToJsonString(list);
            result = JSON.parseArray(jsonText, clazz);
        }
        return result;
    }

    /**
     * parse one mongo DBObject to a bean
     * clazz :
     * the bean's class type
     */
    static <T> T dbObjectToBean(DBObject dbObject, Class<T> clazz) {
        T result = null;
        if (dbObject != null) {
            turnKeyFrom_idToObjectId(dbObject);

            String jsonText = objectToJsonString(dbObject);
            result = JSON.parseObject(jsonText, clazz);
        }
        return result;
    }

    /**
     * 将DBObject中的_id属性名变更为objectId
     */
    static void turnKeyFrom_idToObjectId(DBObject dbObject) {
        if (dbObject.containsField(_ID)) {
            Object value = dbObject.removeField(_ID);
            dbObject.put(OBJECT_ID, value);
        }
    }

    /**
     * 将JSONObject中的objectId属性名变更为_id，并且类型为ObjectId
     */
    static void turnKeyFromObjectIdTo_id(JSONObject jsonObject, Object bean) {
        if (jsonObject.containsKey(OBJECT_ID)) {
            jsonObject.remove(OBJECT_ID);


            Object objectId = null;
            try {
                Method getObjectIdMethod = bean.getClass().getDeclaredMethod(GET_OBJECT_ID_METHOD_NAME);
                objectId = getObjectIdMethod.invoke(bean);
            } catch (Exception e) {
                CommonLog.log.error("java bean no add @Data or ObjectId name not objectId!");
                CommonLog.log.error(e.getMessage());
            }

            if (objectId != null) {
                jsonObject.put(_ID, objectId);
            }
        }
    }

    /**
     * parse one bean to mongo DBObject and this DBObject will not contains bean's id
     * if want to contains the id use beanToDBObject(bean,true) method instead
     */
    static DBObject beanToDBObject(Object bean) {
        DBObject object = null;
        if (isValidBean(bean)) {

            object = new BasicDBObject();
            String jsonText = objectToJsonString(bean, true);
            JSONObject jsonObject = JSON.parseObject(jsonText);

            turnKeyFromObjectIdTo_id(jsonObject, bean);

            object.putAll(jsonObject);
        }
        return object;
    }

    /**
     * check bean is valid object
     * the Bean Validation
     */
    static boolean isValidBean(Object bean) {
        Validator validator = validatorFactory.getValidator();
        Collection<ConstraintViolation<Object>> errorList = validator.validate(bean);

        if (!errorList.isEmpty()) {
            String errorMessage = "";
            for (ConstraintViolation<Object> error : errorList) {
                errorMessage += ("this value : " + error.getInvalidValue() + ", bean to DBObject validation error!the message : " + error.getMessage() + "\n");
            }
            throw new RuntimeException(errorMessage);
        }
        return true;
    }

    /**
     * list 转换为可序列化的 BasicDBList
     */
    static BasicDBList listToBasicDBList(List<?> list) {
        return list.stream().map(BeanUtil::beanToDBObject).collect(Collectors.toCollection(BasicDBList::new));
    }
}
