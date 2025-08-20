# WebScrapingAndSearch

A Spring Boot application that scrapes web pages asynchronously, indexes keywords with a Trie, persists results, and exposes REST APIs to schedule scraping jobs and perform keyword searches.

# Features

- Schedule scrape jobs via Quartz scheduler

- Asynchronous scraping using CompletableFuture

- Keyword indexing and fuzzy search via a custom Trie (SearchTrie)

- Persistence of scraped data with merge/update semantics

- RESTful endpoints for job management and search

Extensible design for adding new scrapers or persistence stores

# Prerequisites

- Java 17 (or later)

- Maven 3.8+

- A relational database (H2, PostgreSQL, MySQL, etc.)

- (Optional) Docker for running a local database

# Getting Started

- Clone the repository

```
git clone https://github.com/pratk1812/WebScrapingAndSearch.git
cd WebScrapingAndSearch
```

- Build the project

```
mvn clean install
```

- Configure your database (see Configuration)

# Configuration

- Edit src/main/resources/application.yml (or .properties) to set:

```
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

```
org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.StdJDBCDelegate
org.quartz.jobStore.dataSource=quartzDataSource
spring.quartz.job-store-type=jdbc
spring.quartz.jdbc.initialize-schema=always
```

- You can also configure:
 -   Maximum concurrent scraping jobs
 -   Timeout thresholds for HTTP requests
 -   Custom user‐agent strings

# Running the Application

- Run via Maven:

```
mvn spring-boot:run
```

- Or run the packaged JAR:

```
java -jar target/WebScrapingAndSearch-0.0.1-SNAPSHOT.jar
```

- The application starts on port 8080 by default.

