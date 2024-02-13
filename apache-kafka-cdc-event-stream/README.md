# Apache Kafka CDC Event Stream

This example implements a Change Data Capture (CDC) pipeline with Apache Kafka.

## Architecture

```mermaid
graph LR
    subgraph Mastodon
        A[REST API]
    end
    A:::mastodon -- REST --> B1:::spring
    subgraph Spring Boot App
        B1[REST Client] --> B2[JDBC Client]
    end
    B2:::spring -- JDBC --> C:::database
    subgraph Database
        C[(Model)]
    end
    C -- CDC --> D:::kafka
    subgraph Kafka Connect 
        D[Debezium]
    end
    D --> E:::kafka
    subgraph Kafka 
        E[Source Topic]
    end
    E --> F:::spring
    subgraph Spring Boot App 
        F[Kafka Streams]
    end
    
    classDef mastodon fill:#8c8dff,stroke:#000000,color:#000000
    classDef spring fill:#70db70,stroke:#000000,color:#000000
    classDef database fill:#800000,stroke:#000000,color:#ffffff
    classDef kafka fill:#003399,stroke:#000000,color:#ffffff
```

The arrows in the diagram above signify data flow.

Change Data Capture (CDC) is typically used to publish records of data changes in a database.

## Use Cases

