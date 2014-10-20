package org.gradle;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.setPort;
import static spark.SparkBase.staticFileLocation;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class HelloSpark {

	public static User NotExistUser = new User("NotExist", "NotExist@email.net");

	public static void main(String[] args) throws SQLException {

		// this uses h2 by default but change to match your database
		String databaseUrl = "jdbc:h2:mem:account";
		// create a connection source to our database
		ConnectionSource connectionSource = new JdbcConnectionSource(
				databaseUrl);

		// instantiate the dao
		Dao<User, String> userDao = DaoManager.createDao(connectionSource,
				User.class);

		// if you need to create the 'users' table make this call
		TableUtils.createTableIfNotExists(connectionSource, User.class);

		initTestData(userDao);

		staticFileLocation("/public");

		setPort(8000);

		get("/hello", (request, respone) -> "Hello World at " + new Date());

		get("/hellojson", (req, res) -> {
			return new MyMessage("hello date : " + new Date(), 100);
		}, new JsonTransformer());

		get("/users/:id", (req, resp) -> {
			User user = null;
			try {
				user = userDao.queryForId(req.params(":id"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return user == null ? NotExistUser : user;
		}, new JsonTransformer());
		
		post("/user",(req,resp)->{
			String username = req.queryParams("name");
			String email = req.queryParams("email");
			User user = new User(username,email);
			//System.out.println(user);
			try {
				user = userDao.createIfNotExists(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return user;
		},new JsonTransformer());
		
		get("/username/:name", (req, resp) -> {
			User user = null;
			try {
				List<User> list = userDao.queryForEq("username", req.params(":name"));
				user = list.size()>0?list.get(0):null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return user == null ? NotExistUser : user;
		}, new JsonTransformer());

	}

	private static void initTestData(Dao<User, String> userDao)
			throws SQLException {
		List<User> users = Lists.newArrayList(new User("user1",
				"user1@test.net"), new User("user2", "user2@test.net"),
				new User("user3", "user3@test.net"));

		for (User user : users) {
			userDao.createIfNotExists(user);
		}

	}
}
