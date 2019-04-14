package com.jakewharton.rs.kotlinx.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import java.lang.UnsupportedOperationException
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.NotAcceptableException
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Application
import javax.ws.rs.core.HttpHeaders.CONTENT_TYPE
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

const val APPLICATION_CBOR = "application/cbor"
val APPLICATION_CBOR_TYPE: MediaType = MediaType.valueOf(APPLICATION_CBOR)

val serializers = setOf(
    Json.asMessageBodyReader(APPLICATION_JSON_TYPE),
    Json.asMessageBodyWriter(APPLICATION_JSON_TYPE),
    Cbor.asMessageBodyReader(APPLICATION_CBOR_TYPE),
    Cbor.asMessageBodyWriter(APPLICATION_CBOR_TYPE))

val testUsersList = listOf(
    User(1, "John"),
    User(2, "Mary"))

object TestApp : Application() {
  override fun getSingletons() = serializers
  override fun getClasses() = setOf(TestResource::class.java, ErrorMapper::class.java)
}

@Path("users")
@Consumes(APPLICATION_JSON, APPLICATION_CBOR)
@Produces(APPLICATION_JSON, APPLICATION_CBOR)
class TestResource {
  @GET
  fun list(): List<User> = testUsersList

  @GET
  @Path("{id}")
  fun getById(@PathParam("id") id: Int): User? = testUsersList.find { it.id == id }

  @GET
  @Path("fail")
  fun fail(): Nothing = throw UnsupportedOperationException()
}

@Provider
@Produces(APPLICATION_JSON, APPLICATION_CBOR)
class ErrorMapper : ExceptionMapper<Exception> {
  override fun toResponse(exception: Exception): Response = when (exception) {
    is NotAcceptableException -> exception.response
    is WebApplicationException -> Response.status(exception.response.statusInfo)
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .entity(ErrorMessage(exception.response.status, exception.localizedMessage))
        .build()
    else -> Response.serverError()
        .entity(ErrorMessage(INTERNAL_SERVER_ERROR.statusCode, exception.localizedMessage))
        .build()
  }.also { exception.printStackTrace() }
}

@Serializable
data class User(val id: Int, val name: String)

@Serializable
data class ErrorMessage(val errorCode: Int, val message: String?)
