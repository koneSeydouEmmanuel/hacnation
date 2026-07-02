#!/bin/bash
# Kafka topic initialization for HACNATION

KAFKA_BIN=/opt/kafka/bin
BOOTSTRAP=localhost:9092

echo "Creating Kafka topics..."

$KAFKA_BIN/kafka-topics.sh --create --if-not-exists --bootstrap-server $BOOTSTRAP --topic sic.patient --partitions 3 --replication-factor 1
$KAFKA_BIN/kafka-topics.sh --create --if-not-exists --bootstrap-server $BOOTSTRAP --topic sic.rdv --partitions 3 --replication-factor 1
$KAFKA_BIN/kafka-topics.sh --create --if-not-exists --bootstrap-server $BOOTSTRAP --topic sic.file-attente --partitions 3 --replication-factor 1
$KAFKA_BIN/kafka-topics.sh --create --if-not-exists --bootstrap-server $BOOTSTRAP --topic sic.consultation --partitions 3 --replication-factor 1
$KAFKA_BIN/kafka-topics.sh --create --if-not-exists --bootstrap-server $BOOTSTRAP --topic sic.prescription --partitions 3 --replication-factor 1
$KAFKA_BIN/kafka-topics.sh --create --if-not-exists --bootstrap-server $BOOTSTRAP --topic sic.laboratoire --partitions 3 --replication-factor 1
$KAFKA_BIN/kafka-topics.sh --create --if-not-exists --bootstrap-server $BOOTSTRAP --topic sic.pharmacie --partitions 3 --replication-factor 1
$KAFKA_BIN/kafka-topics.sh --create --if-not-exists --bootstrap-server $BOOTSTRAP --topic sic.facturation --partitions 3 --replication-factor 1
$KAFKA_BIN/kafka-topics.sh --create --if-not-exists --bootstrap-server $BOOTSTRAP --topic sic.admission --partitions 3 --replication-factor 1
$KAFKA_BIN/kafka-topics.sh --create --if-not-exists --bootstrap-server $BOOTSTRAP --topic sic.audit --partitions 3 --replication-factor 1
$KAFKA_BIN/kafka-topics.sh --create --if-not-exists --bootstrap-server $BOOTSTRAP --topic sic.notification --partitions 3 --replication-factor 1

echo "Topics created:"
$KAFKA_BIN/kafka-topics.sh --list --bootstrap-server $BOOTSTRAP
