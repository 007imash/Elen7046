package layer.config

import com.typesafe.config.ConfigFactory

object Settings {

    private val config = ConfigFactory.load()

    object ParticleReaderGen {

        private val particleReaderGen = config.getConfig("particle")

        lazy val filepath: String = particleReaderGen.getString("file_path")
        lazy val deviceUrl: String = particleReaderGen.getString("device_url")
        lazy val kafkaTopic: String = particleReaderGen.getString("kafka_topic")
        lazy val kafkaBootstrapServers: String = particleReaderGen.getString("kafka_bootstrap_servers")
        lazy val kafkaKeySerializerClass: String = particleReaderGen.getString("kafka_key_serializer_class")
        lazy val kafkaValueSerializerClass: String = particleReaderGen.getString("kafka_value_serializer_class")
        lazy val kafkaKeyDeserializerClass: String = particleReaderGen.getString("kafka_key_deserializer_class")
        lazy val kafkaValueDeserializerClass: String = particleReaderGen.getString("kafka_value_deserializer_class")
        lazy val kafkaAcks: String = particleReaderGen.getString("kafka_acks")
        lazy val kafkaClientId: String = particleReaderGen.getString("kafka_client_id")
        lazy val kafkaGroupId: String = particleReaderGen.getString("kafka_group_id")
        lazy val hdfsPath: String = particleReaderGen.getString("hdfs_path")
        lazy val cassandraHost = particleReaderGen.getString("cassandraHost")
        lazy val cassandraPort = particleReaderGen.getString("cassandraPort")
        lazy val deviceId = particleReaderGen.getString("deviceId")
        lazy val cassandra_key_space = particleReaderGen.getString("cassandra_key_space")
        lazy val cassandra_speed_table = particleReaderGen.getString("cassandra_speed_table")
        lazy val cassandra_batch_table = particleReaderGen.getString("cassandra_batch_table")
        lazy val cassandra_daily_table = particleReaderGen.getString("cassandra_daily_table")
        lazy val cassandra_dayOfWeek_table = particleReaderGen.getString("cassandra_dayOfWeek_table")
        lazy val cassandra_monthly_table = particleReaderGen.getString("cassandra_monthly_table")
        lazy val cassandra_hourly_table = particleReaderGen.getString("cassandra_hourly_table")

    }
}
