# Apache Pulsar Twitter Source Connector

```bash
./bin/pulsar-admin source create \
  --name acntech-twitter-source \
  --source-type acntech-twitter-source \
  --destinationTopicName tweets \
  --source-config '{"bearerToken":<BEARER_TOKEN>}'
```
