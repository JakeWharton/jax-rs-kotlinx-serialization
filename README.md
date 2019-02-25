JAX-RS Kotlinx Serialization
============================

A message body reader/writer and parameter converter which uses kotlinx-serialization.



Usage
-----

```kotlin
val resourceConfig = new ResourceConfig();

resourceConfig.register(Json.asMessageBodyReader(APPLICATION_JSON_TYPE));
resourceConfig.register(Json.asMessageBodyWriter(APPLICATION_JSON_TYPE));
```

_(This example uses Jersey, but any JAX-RS-compatible implementation will work.)_

By specifying `application/json` as the `MediaType`, its usage will match the reader and/or writer
for your `@Serializable` models.

```kotlin
@POST @Path("register")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
fun register(registerRequest: RegisterRequest): RegisterResponse {
  // ...
}
```


Download
--------

Gradle:
```groovy
compile 'com.jakewharton:jax-rs-kotlinx-serialization:0.1.0'
```
or Maven:
```xml
<dependency>
  <groupId>com.jakewharton</groupId>
  <artifactId>jax-rs-kotlinx-serialization</artifactId>
  <version>0.1.0</version>
</dependency>
```

Snapshot versions are available in the Sonatype 'snapshots' repository: https://oss.sonatype.org/content/repositories/snapshots/



License
-------

    Copyright 2019 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
