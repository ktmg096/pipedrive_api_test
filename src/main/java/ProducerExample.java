import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.connectors.pipedrive.PipedriveAPITest;
import org.connectors.pipedrive.PipedriveHistoricalLoadTest;

import java.util.ArrayList;
import java.util.Properties;

public class ProducerExample {
    public static String KAFKA_TOPIC = "pipedrive-topic-1";

    public static void main(String[] args) {

        Properties kafkaProducerProperties = setUpProperties();
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaProducerProperties);

        // String value = PipedriveAPITest.scrapeOrganizations();
        // ProducerRecord<String, String> record = new ProducerRecord<String, String>(KAFKA_TOPIC, value);

        ArrayList<String> historical_values = PipedriveHistoricalLoadTest.fetchOrganizations();
        ArrayList<ProducerRecord<String, String>> records = new ArrayList<ProducerRecord<String, String>>();
        for (String historical_value: historical_values) {
            records.add(new ProducerRecord<String, String>(KAFKA_TOPIC, historical_value));
        }

        try {
            for (ProducerRecord<String, String> record: records) {
                System.out.println(producer.send(record).get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Properties setUpProperties() {
        Properties kafkaProducerProperties = new Properties();
        kafkaProducerProperties.put("bootstrap.servers", "localhost:9092");
        kafkaProducerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProducerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return kafkaProducerProperties;
    }

}
