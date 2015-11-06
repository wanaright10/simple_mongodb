package com.simple.mongodb.factory;

import com.mongodb.*;
import com.simple.mongodb.util.Configure;
import com.simple.mongodb.util.EmptyUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dong Wang.
 * Created on 14/12/30 下午5:30
 * <p>
 * Mongo单例类
 */
public enum SingletonMongo {
    INSTANCE;
    private Mongo mongo;

    private static final String SOURCE = "mongodb_source";
    private static final String USERNAME = "mongodb_username";
    private static final String PASSWORD = "mongodb_password";
    private static final String REPLICA_SET = "mongodb_replica_set";
    private static final String MONGO_ADDRESS = "mongodb_address";
    private static final String REPLICA_SET_NAME = "mongodb_replica_set_name";
    //-------
    private String mongoAddress;
    private String replicaSet;    //use replicaSet or not
    private MongoClientOptions.Builder mongoOptionsBuilder;
    private List<MongoCredential> credentialsList;

    SingletonMongo() {
        initParamFromProperties();

        try {
            if (replicaSet.equals("true")) {
                mongo = initReplicaSetMongo();
            } else {
                mongo = initSingleMongo();
            }

            CommonLog.log.info("mongo initialed ：" + mongoAddress);
        } catch (Exception e) {
            CommonLog.log.error(e.getMessage());
        }
    }

    private Mongo initReplicaSetMongo() throws Exception {
        String db_replicaSetName = Configure.getProperty(REPLICA_SET_NAME);

        List<ServerAddress> serverAddresses = new ArrayList<>();
        String[] mongodbAddresses = mongoAddress.split(",");
        for (String serverAddress : mongodbAddresses) {
            String[] ipAndPort = serverAddress.split(":");
            serverAddresses.add(new ServerAddress(ipAndPort[0], Integer.parseInt(ipAndPort[1])));
        }

        MongoClientOptions option = mongoOptionsBuilder
                .readPreference(ReadPreference.secondaryPreferred())
                .requiredReplicaSetName(db_replicaSetName)
                .build();

        Mongo result;
        if (credentialsList != null) {
            result = new MongoClient(serverAddresses, credentialsList, option);
        } else {
            result = new MongoClient(serverAddresses, option);
        }
        return result;
    }

    private Mongo initSingleMongo() throws Exception {
        String[] ipAndPort = mongoAddress.split(":");

        MongoClientOptions option = mongoOptionsBuilder.build();

        Mongo result;
        if (credentialsList != null) {
            result = new MongoClient(new ServerAddress(ipAndPort[0], Integer.parseInt(ipAndPort[1])), credentialsList, option);
        } else {
            result = new MongoClient(new ServerAddress(ipAndPort[0], Integer.parseInt(ipAndPort[1])), option);
        }
        return result;
    }

    private void initParamFromProperties() {
        mongoAddress = Configure.getProperty(MONGO_ADDRESS);
        replicaSet = Configure.getProperty(REPLICA_SET);
        String source = Configure.getProperty(SOURCE);
        String username = Configure.getProperty(USERNAME);
        String password = Configure.getProperty(PASSWORD);

        //the setting connect mongodb
        mongoOptionsBuilder = new MongoClientOptions.Builder()
                .connectionsPerHost(1000)
                .socketKeepAlive(true)
                .connectTimeout(15000)
                .maxWaitTime(5000)
                .socketTimeout(10000)
                .threadsAllowedToBlockForConnectionMultiplier(5000);

        //set credential
        if (!EmptyUtil.isNullOrEmpty(password) && !EmptyUtil.isNullOrEmpty(username)) {
            MongoCredential mongoCredential = MongoCredential.createMongoCRCredential(username, source, password.toCharArray());
            credentialsList = Collections.singletonList(mongoCredential);
        } else {
            credentialsList = null;
        }
    }

    public DB getDBByName(String dbName) {
        return mongo.getDB(dbName);
    }
}
