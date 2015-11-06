# simple_mongodb
封装一层 Mongo-java-driver,让查询更加简单
# 用法
比如创建一个 查询 user 表的 userDao:
public class UserDao extends AbstractDao<User> {
    //这里可以用 Spring 依赖注入
    private DBCollection userCollection;

    @Override
    public DBCollection getDaoCollection() {
        return userCollection;
    }
}

其中userCollection可以选择依赖注入:
userCollection = MongoDBFactory.getCollection("db_name", "collection_name");
