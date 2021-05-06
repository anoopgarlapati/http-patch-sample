# HTTP PATCH variants in Spring MVC

This sample project contains variants of implementing HTTP PATCH in Spring MVC.

- Build the sample -> `./gradlew clean build`
- Run the server -> `./gradlew bootRun`
- OpenAPI documentation is available at `http://localhost:8080/swagger-ui.html`

The service contains one resource `Entity` and APIs to create, read and update `Entity` resources.

Different PATCH variants demonstrated:
- Copy only non null attributes from request body:
    - In this mode, only non null attributes from the request body are patched. 
    - Downside of this approach is there is no way to reset already set attributes.
    - Use API `PATCH /api/entities/copy-nonull/{id}`
- JSON Merge Patch:
    - In this mode, all attributes from the request body are patched.
    - This is as per [RFC 7396: JSON Merge Patch](https://tools.ietf.org/html/rfc7396)
    - If the caller wants to reset an attribute, then specifying the attribute as `null` would result in reset of that attribute.
    - Use API `PATCH /api/entities/{id}`  
