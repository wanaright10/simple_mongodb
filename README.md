# simple_mongodb (JDK8支持)
封装一层 Mongo-java-driver,让查询更加简单
# 用法
比如创建一个 查询 user 表的 userDao:
```java
public class UserDao extends AbstractDao<User> {
    //这里可以用 Spring 依赖注入
    private DBCollection userCollection;

    @Override
    public DBCollection getDaoCollection() {
        return userCollection;
    }
}
```

其中userCollection可以选择依赖注入:
```java
userCollection = MongoDBFactory.getCollection("db_name", "collection_name");
```
这样你的 userDao 就具有很多查询方法了:

比如 queryOne queryList 等,可以分页,自动转换bean,不用担心类型问题
#其他功能
annotation包 下面是一些注解,写在 javabean 上面,插入数据库之前会进行数据验证

MongoOperators类 包含了 mongodb 自带的大部分常用操作常量 比如$set $in 等

BeanUtil类 包含了一些 bean 和 mongodb 的 DBObject 之间的转换

EmptyUtil类 包含了对任意对象的空验证 包括字符串 数组 object 数字等
