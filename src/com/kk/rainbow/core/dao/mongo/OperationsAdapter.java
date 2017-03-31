package com.kk.rainbow.core.dao.mongo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.Mongo;

public class OperationsAdapter extends MongoTemplate {
	public OperationsAdapter(Mongo mongo, String databaseName) {
		super(mongo, databaseName);
		// TODO Auto-generated constructor stub
	}

	public OperationsAdapter(Mongo mongo, String databaseName,
			UserCredentials userCredentials) {
		super(mongo, databaseName, userCredentials);
		// TODO Auto-generated constructor stub
	}

	public OperationsAdapter(MongoDbFactory mongoDbFactory) {
		super(mongoDbFactory);
		// TODO Auto-generated constructor stub
	}

	public OperationsAdapter(MongoDbFactory mongoDbFactory,
			MongoConverter mongoConverter) {
		super(mongoDbFactory, mongoConverter);
		// TODO Auto-generated constructor stub
	}

	public <T> Map<String, Object> findAsPage(Query query, Class<T> c,
			String collection) {
		if (StringUtils.isBlank(collection) || c == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (query == null) {
		}
		this.find(query, c, collection);
		return map;
	}
}