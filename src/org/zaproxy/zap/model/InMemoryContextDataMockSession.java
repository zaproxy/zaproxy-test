package org.zaproxy.zap.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.parosproxy.paros.model.Session;

/**
 * A Mock for Session that stores Context data in memory instead of using a database. 
 */
public class InMemoryContextDataMockSession extends Session {
	public Map<Integer, Map<Integer, List<String>>> data;

	public InMemoryContextDataMockSession() {
		super(null);
		this.data = new HashMap<>();
	}

	@Override
	public List<String> getContextDataStrings(int contextId, int type) throws SQLException {
		List<String> d = getMapForContext(contextId).get(type);
		if (d != null)
			return d;
		else
			return new LinkedList<>();
	}

	@Override
	public void setContextData(int contextId, int type, List<String> dataList) throws SQLException {
		this.getMapForContext(contextId).put(type, new LinkedList<>(dataList));
	}

	private Map<Integer, List<String>> getMapForContext(int contextId) {
		if (data.get(contextId) == null)
			data.put(contextId, new HashMap<Integer, List<String>>());
		return data.get(contextId);
	}
}