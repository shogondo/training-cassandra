package shou.training.cassandra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.policies.ConstantSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.LoggingRetryPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

public class App {
	private Cluster cluster;

	private Session session;

	private MappingManager mappingManager;

	private Mapper<Hotel> hotelMapper;

	public static void main(String[] args) {
		App app = new App();
		app.start();
	}

	private void start() {
		connect();
		
		dumpMetadata();
		
		PreparedStatement statemetToInsert = prepareToInsert();
		insert(statemetToInsert, "JP001", "Hotel new otani", "+81-3-1234-5678");

		PreparedStatement statementToFind = prepareToFind();
		find(statementToFind, "JP001");

		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "CZ111");
		params.put("name", "Beijing fang dian");
		params.put("phone", "9-999-999-9999");
		insert(params);

		Hotel hotel = new Hotel();
		hotel.setId("KR222");
		hotel.setName("Korean king's hotel");
		hotel.setPhone("123-456-7890");
		insert(hotel);
		find("KR222");

		insert("AZ123", "Super Hotel at WestWorld", "1-888-999-9999");

		hotelMapper.delete("CZ-111");

		search();
	}

	private void connect() {
		List<Host.StateListener> listeners = new ArrayList<Host.StateListener>();
		listeners.add(new ClusterConnectionListener());
		cluster = Cluster.builder().addContactPoint("192.168.33.10").withPort(9042)
				.withInitialListeners(listeners)
				.withLoadBalancingPolicy(new TokenAwarePolicy(DCAwareRoundRobinPolicy.builder().build()))
				.withRetryPolicy(new LoggingRetryPolicy(DefaultRetryPolicy.INSTANCE))
				.withSpeculativeExecutionPolicy(new ConstantSpeculativeExecutionPolicy(200, 3)).build();
		cluster.init();
		session = cluster.connect("hotel");
		mappingManager = new MappingManager(session);
		hotelMapper = mappingManager.mapper(Hotel.class);
	}

	private PreparedStatement prepareToFind() {
		return session.prepare("SELECT * FROM hotels WHERE id = ?");
	}

	private PreparedStatement prepareToInsert() {
		return session.prepare("INSERT INTO hotels (id, name, phone) VALUES (?, ?, ?)");
	}

	private void find(String id) {
		Hotel hotel = hotelMapper.get(id);
		print(hotel);
	}

	private void find(PreparedStatement statement, String id) {
		ResultSet result = session.execute(statement.bind(id));
		print(result);
	}

	private void search() {
		ResultSet result = session.execute("SELECT * FROM hotel.hotels");
		print(result);
	}

	private void insert(Hotel hotel) {
		hotelMapper.save(hotel);
	}

	private void insert(String id, String name, String phone) {
		SimpleStatement statement = new SimpleStatement("INSERT INTO hotels (id, name, phone) VALUES (?, ?, ?)", id,
				name, phone);
		session.execute(statement);
	}

	private void insert(PreparedStatement statement, String id, String name, String phone) {
		session.execute(statement.bind(id, name, phone));
	}

	private void insert(Map<String, String> params) {
		Insert statement = QueryBuilder.insertInto("hotels");
		for (String name : params.keySet()) {
			statement = statement.value(name, params.get(name));
		}
		session.execute(statement);
	}

	private void print(Hotel hotel) {
		System.out.println(
				String.format("id: %s, name: %s, phone: %s", hotel.getId(), hotel.getName(), hotel.getPhone()));
	}

	private void print(ResultSet result) {
		for (Row row : result) {
			System.out.format("id: %s, name: %s, phone: %s", row.getString("id"), row.getString("name"),
					row.getString("phone"));
			System.out.println();
		}
	}

	private void dumpMetadata() {
		Metadata metadata = cluster.getMetadata();
		System.out.println(String.format("cluster: %s", metadata.getClusterName()));
		for (Host host : metadata.getAllHosts()) {
			System.out.println(String.format("dc: %s, rack: %s, host: %s", host.getDatacenter(), host.getRack(),
					host.getAddress().getHostName()));
		}
	}
}
