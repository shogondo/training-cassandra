package shou.training.cassandra;

import java.util.HashSet;
import java.util.Set;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "hotel", name = "hotels")
public class Hotel {
	@PartitionKey
	private String id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="phone")
	private String phone;
	
	public Hotel() {
	}

	public String getId() {
		return id;
	}

	public void setId(String value) {
		id = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String value) {
		phone = value;
	}
}
