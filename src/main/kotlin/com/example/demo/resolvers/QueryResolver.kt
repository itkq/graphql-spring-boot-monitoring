package com.example.demo.resolvers

import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class QueryResolver : GraphQLQueryResolver {
   fun foo(id: Int): String {
       return "foo"
   }

   fun piyo(id: Int): String {
       return "piyo"
   }
}
