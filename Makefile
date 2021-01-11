run:
	sh mvnw spring-boot:run

all-test:
	sh mvnw test

unit-test:
	sh mvnw test -Dgroups='!integration-test & !component-test'

integration-test:
	sh mvnw test -Dgroups=integration-test

component-test:
	sh mvnw test -Dgroups=component-test

load-files:
	curl -X POST http://localhost:8080/articles/inventory/bulk -d @src/test/resources/inventory.json --header "Content-Type: application/json" -v
	curl -X POST http://localhost:8080/products/bulk -d @src/test/resources/products.json --header "Content-Type: application/json" -v
	curl -X GET http://localhost:8080/catalog --header "Content-Type: application/json" -v

show-catalog:
	curl -X GET http://localhost:8080/catalog --header "Content-Type: application/json" | jq .

buy-dinning-chair:
	curl -X DELETE 'http://localhost:8080/products/Dining%20Chair/stock' -v

buy-dinning-table:
	curl -X DELETE 'http://localhost:8080/products/Dinning%20Table/stock' -v
