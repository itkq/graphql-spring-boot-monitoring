package com.example.demo.resolvers

import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class MutationResolver: GraphQLMutationResolver {
    fun bar(id: Int): String {
        return "bar"
    }
}
