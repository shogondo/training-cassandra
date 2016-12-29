package shou.training.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Host.StateListener;

public class ClusterConnectionListener implements StateListener {

	public void onAdd(Host host) {
		// TODO Auto-generated method stub

	}

	public void onUp(Host host) {
		System.out.println("The node is up.");
		printHostInfo(host);
	}

	public void onDown(Host host) {
		System.out.println("The node is down.");
		printHostInfo(host);
	}

	public void onRemove(Host host) {
		System.out.println("The node is removed.");
		printHostInfo(host);
	}

	public void onRegister(Cluster cluster) {
		System.out.println("The cluster is registered.");
		printClusterInfo(cluster);
	}

	public void onUnregister(Cluster cluster) {
		System.out.println("The cluster is unregistered.");
		printClusterInfo(cluster);
	}
	
	private void printClusterInfo(Cluster cluster) {
		System.out.println(String.format("cluster: %s", cluster.getClusterName()));
	}

	private void printHostInfo(Host host) {
		System.out.println(String.format("dc: %s, rack: %s, host: %s, version: %s, state: %s", host.getDatacenter(),
				host.getRack(), host.getAddress(), host.getCassandraVersion(), host.getState()));
	}
}
