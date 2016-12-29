#! /bin/sh

CLUSTER="my_cluster"

for NODE in node1 node2 node3; do
    CASSANDRA_HOME="$HOME/.ccm/$CLUSTER/$NODE"

    cp "$CASSANDRA_HOME/conf/cassandra.yaml" "$CASSANDRA_HOME/conf/cassandra.yaml.default"
    sed -i -e "s/127.0.0.1/192.168.33.10/g" "$CASSANDRA_HOME/conf/cassandra.yaml"
    sed -i -e "s/127.0.0.2/192.168.33.20/g" "$CASSANDRA_HOME/conf/cassandra.yaml"
    sed -i -e "s/127.0.0.3/192.168.33.30/g" "$CASSANDRA_HOME/conf/cassandra.yaml"

    cp "$CASSANDRA_HOME/node.conf" "$CASSANDRA_HOME/node.conf.default"
    sed -i -e "s/127.0.0.1/192.168.33.10/g" "$CASSANDRA_HOME/node.conf"
    sed -i -e "s/127.0.0.2/192.168.33.20/g" "$CASSANDRA_HOME/node.conf"
    sed -i -e "s/127.0.0.3/192.168.33.30/g" "$CASSANDRA_HOME/node.conf"
done
