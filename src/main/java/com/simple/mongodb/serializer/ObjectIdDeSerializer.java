package com.simple.mongodb.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;

/**
 * Created by Dong Wang.
 * Created on 15/1/20 16:03
 */
public class ObjectIdDeSerializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();

        T result = null;
        if (lexer.token() == JSONToken.LITERAL_STRING) {
            String objectIdString = lexer.stringVal();
            result = (T) new ObjectId(objectIdString);

            lexer.nextToken();
        } else if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken();
        }
        return result;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
