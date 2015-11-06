package com.simple.mongodb.core;

import com.mongodb.QueryOperators;
import lombok.experimental.UtilityClass;

/**
 * Created by Dong Wang.
 * Created on 15/1/27 17:49
 */
@UtilityClass
public class MongoOperators extends QueryOperators {
    public final String SET = "$set";
    public final String INC = "$inc";
    public final String MUL = "$mul";
    public final String CURRENT_DATE = "$currentDate";
    public final String RENAME = "$rename";
    public final String SET_ON_INSERT = "$setOnInsert";
    public final String UN_SET = "$unset";
    public final String ADD_TO_SET = "$addToSet";
    public final String POP = "$pop";
    public final String PULL_ALL = "$pullAll";
    public final String PULL = "$pull";
    public final String PUSH_ALL = "$pushAll";
    public final String PUSH = "$push";
    public final String EACH = "$each";
    public final String POSITION = "$position";
    public final String SLICE = "$slice";
    public final String SORT = "$sort";
    public final String BIT = "$bit";
    public final String ISOLATED = "$isolated";
    public final String GEO_NEAR = "$geoNear";
    public final String GROUP = "$group";
    public final String LIMIT = "$limit";
    public final String MATCH = "$match";
    public final String OUT = "$out";
    public final String PROJECT = "$project";

    public final String GEO_WITH_IN = "$geoWithin";
    public final String GEO_INTERSECTS = "$geoIntersects";
    public final String GEOMETRY = "$geometry";
    public final String MIN_DISTANCE = "$minDistance";
    public final String MAX_DISTANCE = "$maxDistance";

    public final String SUM = "$sum";
    public final String SKIP = "$skip";
    public final String UN_WIND = "$unwind";

    public final int SORT_ASC = 1;
    public final int SORT_DESC = -1;
}
