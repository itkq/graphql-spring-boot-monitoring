# graphql-spring-boot-monitoring

A sample graphql-spring-boot application with a simple GraphQL metrics instrumentation.

## Usage

```
$ ./gradlew bootRun &

$ curl -sSf -XPOST -H 'content-type: application/json' -d '{"query":"query foo {\n  foo(id: 1)\n}","variables":{},"operationName":"foo"}' http://localhost:8080/graphql
{"data":{"foo":"foo"}}

$ curl http://localhost:8080/prometheus | grep graphql | grep foo
graphql_timer_query_seconds_max{operation="execution",operationName="foo",} 0.05711325
graphql_timer_query_seconds_max{operation="parsing",operationName="foo",} 0.018640292
graphql_timer_query_seconds_max{operation="validation",operationName="foo",} 0.010388375
graphql_timer_query_seconds_count{operation="execution",operationName="foo",} 1.0
graphql_timer_query_seconds_sum{operation="execution",operationName="foo",} 0.05711325
graphql_timer_query_seconds_count{operation="parsing",operationName="foo",} 1.0
graphql_timer_query_seconds_sum{operation="parsing",operationName="foo",} 0.018640292
graphql_timer_query_seconds_count{operation="validation",operationName="foo",} 1.0
graphql_timer_query_seconds_sum{operation="validation",operationName="foo",} 0.010388375
graphql_query_total{operation="query",operation_name="foo",outcome="success",} 1.0
```
