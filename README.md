# MiniBank

A Java 17+ console banking application organized as model, service, and utility packages. It covers the staged practical topics: encapsulation, inheritance and polymorphism, interfaces, annotations and reflection, checked exceptions, validation, generic repositories, thread pools, producer/consumer coordination, persistence, logging, reporting, and JUnit 5.

## Build and run

Install JDK 17+ and Maven, then run from this directory:

```sh
mvn test package
java -jar target/minibank.jar
```

Run the practical demonstrations (validators, command parsing, runtime validation, polymorphism, wait/notify, and try-with-resources) with:

```sh
java -jar target/minibank.jar --lab-examples
```

JUnit covers deposits, denied withdrawals, concurrent deposits, and persistence round trips.

## Data files

The app loads and saves serialized account objects in `data/accounts.dat`, appends transactions to `data/logs/transactions.csv`, and writes the report to `data/report.txt`. Generated data and build output are excluded from version control.

## Menu

Open savings, current, or fixed deposit accounts; deposit, withdraw, transfer, view a statement, list accounts by descending balance, and generate a transaction report. Savings accounts retain a ₹500 minimum, current accounts support a ₹1,000 overdraft, and fixed deposits do not permit withdrawals.

## Packages

- `model`: customers, account types, records, and interfaces
- `model.annotation`: runtime `@Id`, `@Positive`, and `@MaxLength` annotations
- `exception`: checked banking exception hierarchy
- `service`: bank operations, account store, transaction processing, persistence, logs, and reports
- `util`: validators, parser, statement formatter, annotation validator, and generic repository

This is a learning project, not production banking software. Keep backups of the generated data if it matters.
