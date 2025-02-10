# dynamic_file_processor

## Overview
`dynamic_file_processor` is a Java-based application that fetches files via Apache Camel and processes them using Spring Batch. The application is designed to handle dynamic configurations for different banks, allowing for flexible file processing and mapping.

## Features
- Fetch files from FTP servers using Apache Camel.
- Process files using Spring Batch.
- Dynamic configuration for different banks.
- Custom field mapping and data transformation.
- MD5 hash generation for transaction validation.

## Technologies Used
- Java 17
- Spring Boot
- Spring Batch
- Apache Camel
- PostgreSQL
- Docker

## Getting Started

### Prerequisites
- Java 17
- Docker
- Docker Compose

### Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/your-repo/dynamic_file_processor.git
   cd dynamic_file_processor


### Bank Configuration
Bank configurations are stored in the bank_configs table. You can initialize the configurations using the DataInitializer class.  
### CSV Field Mappings
Field mappings for CSV files are stored in the csv_field_mappings table. These mappings define how CSV columns are mapped to the Transaction entity fields.