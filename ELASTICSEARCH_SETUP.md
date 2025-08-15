# Elasticsearch Setup and Error Fix

## The Error
You're getting this error because the application is trying to connect to Elasticsearch but it's not running. The error occurs because Spring Boot is trying to initialize Elasticsearch repositories but can't establish a connection.

## Solutions

### Solution 1: Quick Fix - Disable Elasticsearch (Recommended for Development)
The application is already configured to make Elasticsearch optional. By default, it's disabled.

#### To run WITHOUT Elasticsearch:
```bash
# Just run the application normally - Elasticsearch is disabled by default
mvn spring-boot:run
```

The application will start normally and all other features will work. Search endpoints will not be available.

#### To run WITH Elasticsearch:
```bash
# Set the environment variable to enable Elasticsearch
set ELASTICSEARCH_ENABLED=true
mvn spring-boot:run
```

### Solution 2: Install and Run Elasticsearch

#### Option A: Using Docker (Easiest)
```bash
# Pull and run Elasticsearch
docker run -d --name elasticsearch \
  -p 9200:9200 \
  -e "discovery.type=single-node" \
  -e "ELASTIC_PASSWORD=your_password" \
  docker.elastic.co/elasticsearch/elasticsearch:8.11.0
```

#### Option B: Download and Install Elasticsearch
1. Download Elasticsearch from https://www.elastic.co/downloads/elasticsearch
2. Extract and run:
   ```bash
   cd elasticsearch-8.11.0
   bin/elasticsearch.bat  # Windows
   bin/elasticsearch      # Linux/Mac
   ```

### Solution 3: Use External Elasticsearch Service
Update `application.yaml` to point to your Elasticsearch service:
```yaml
spring:
  elasticsearch:
    enabled: true
    uris: https://your-elasticsearch-cloud-url:9200
    username: elastic
    password: your_password
```

## Environment Variables Needed

When enabling Elasticsearch, you need these environment variables:
```bash
ELASTICSEARCH_ENABLED=true
ELASTIC_PASSWORD=your_elasticsearch_password  # Only if Elasticsearch has authentication
```

## How the Conditional Setup Works

The application uses Spring's `@ConditionalOnProperty` to make Elasticsearch optional:

- **DocumentIndexingService**: Only loads when `spring.elasticsearch.enabled=true`
- **SearchService**: Only loads when `spring.elasticsearch.enabled=true`
- **SearchController**: Only loads when `spring.elasticsearch.enabled=true`
- **ElasticsearchConfig**: Only loads when `spring.elasticsearch.enabled=true`

Services like JobService, PostService, and PostVolunteerService use optional injection, so they work with or without Elasticsearch.

## Testing Search Functionality

Once Elasticsearch is running and enabled:

```bash
# Search jobs
GET http://localhost:8080/api/search/jobs?keyword=developer

# Search posts  
GET http://localhost:8080/api/search/posts?keyword=community

# Search volunteer posts
GET http://localhost:8080/api/search/post-volunteers?keyword=help

# Search all
GET http://localhost:8080/api/search/all?keyword=java
```

## Features Available Without Elasticsearch

- All job creation and management
- All post creation and management  
- All volunteer post creation and management
- User authentication and authorization
- All other application features

Only the `/api/search/*` endpoints require Elasticsearch to be enabled.
