#! /bin/sh

CLUSTER="my_cluster"

for NODE in node1 node2 node3; do
    CASSANDRA_HOME="$HOME/.ccm/$CLUSTER/$NODE"

    # Clear logs.
    : > "$CASSANDRA_HOME/logs/debug.log"
    : > "$CASSANDRA_HOME/logs/system.log"
done

# Set LOCAL_JMX to no if you want to allow to access from remote.
# LOCAL_JMX=no ccm start
ccm start
