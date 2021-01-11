# Warehouse

Warehouse project for interview assessment

## Assumptions

- Load files operations is an update or insert the item.
- Product primary key is its `name` field.
- Product `price` field is not implemented.
- Load files is transactional operation (all or nothing is loaded).
- Remove from stock (sell) operation is for only one product

## Implemented solution

### Tech stack

- Java 11
- Spring boot
- h2 or MySql for SQL database

### Architecture decisions

- Implementation of back-end rest service
- Following a Domain-driven design
- Usage of Java as strong typed language to implemented the domain.
- Implemented a onion architecture, domain as a core, application services as use cases.
- Usage of SQL database for the ACID properties for transactions
- Usage of plain jdbc for data access, better control of sql operations

## Testing

Three levels of testing:

1. Unit - Testing the domain, only memory
2. Component - Using spring boot testing test from the endpoint using H2 database.
3. Integration - Using testcontainers to test against MySql


## Run locally

### Requirements

- JDK 11
- Docker (for integration test with testcontainers)
- make
- curl
- jq

### Run tests:

Run all tests:

```
make all-test
```

Or run by category:

```
make unit-test
make component-test
make integration-test
```

### Run locally

```
make run
```

### Load files

```
make load-files
```

### Show the products and their stock

```
make show-catalog
```

### Buy a dinning table

```
make buy-dinning-table
```

## Example Request and response:

```
curl -X POST http://localhost:8080/articles/inventory/bulk -d @src/test/resources/inventory.json --header "Content-Type: application/json" -v

curl -X POST http://localhost:8080/products/bulk -d @src/test/resources/products.json --header "Content-Type: application/json" -v
```

```
curl -X GET http://localhost:8080/catalog --header "Content-Type: application/json" -v
```
```json
{
  "products": [
    {
      "name": "Dinning Table",
      "stock": 1
    },
    {
      "name": "Dining Chair",
      "stock": 2
    }
  ]
}

```

```
curl -X DELETE 'http://localhost:8080/products/Dinning%20Table/stock' -v
```
