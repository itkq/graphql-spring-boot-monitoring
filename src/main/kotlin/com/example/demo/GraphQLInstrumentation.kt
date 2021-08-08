package com.example.demo

import graphql.ExecutionResult
import graphql.execution.instrumentation.InstrumentationContext
import graphql.execution.instrumentation.InstrumentationState
import graphql.execution.instrumentation.SimpleInstrumentation
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters
import graphql.execution.instrumentation.tracing.TracingInstrumentation
import graphql.schema.DataFetcher
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import java.util.concurrent.CompletableFuture

@Component
class GraphQLInstrumentation(
    val meterRegistry: MeterRegistry
) : TracingInstrumentation() {
    companion object {
        const val QUERY_COUNTER_METRIC_NAME = "graphql.query"

        const val TAG_KEY_OUTCOME = "outcome"
        const val TAG_VALUE_OUTCOME_SUCCESS = "success"
        const val TAG_VALUE_OUTCOME_ERROR = "error"

        const val TAG_KEY_OPERATION = "operation"
        const val TAG_KEY_OPERATION_NAME = "operation_name"

        const val TAG_VALUE_UNKNOWN = "unknown"
    }

    override fun instrumentExecutionResult(
        executionResult: ExecutionResult?,
        parameters: InstrumentationExecutionParameters?
    ): CompletableFuture<ExecutionResult> {
        val outcome = if (CollectionUtils.isEmpty(executionResult?.errors)) {
            TAG_VALUE_OUTCOME_SUCCESS
        } else {
            TAG_VALUE_OUTCOME_ERROR
        }

        val operation = parameters?.query?.toString()?.let {
            Regex("""^(query|mutation)""").find(it)?.value ?: TAG_VALUE_UNKNOWN
        } ?: TAG_VALUE_UNKNOWN
        val operationName = parameters?.operation ?: TAG_VALUE_UNKNOWN
        val tags = listOf(Tag.of(TAG_KEY_OUTCOME, outcome), Tag.of(TAG_KEY_OPERATION, operation), Tag.of(
            TAG_KEY_OPERATION_NAME, operationName))

        meterRegistry.counter(QUERY_COUNTER_METRIC_NAME, tags).increment()

        return super.instrumentExecutionResult(executionResult, parameters)
    }
}
