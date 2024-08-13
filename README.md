# Api Assíncrona
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

Essa é uma API REST :fairy_woman:	 assíncrona :fairy_woman:	 feita com Spring boot. Ela é mágica, MASTIGADINHA. Um presente de um coelho mágico. 

# O que foi usado?
 - Flyway: migração. 
 - Spring Security: responsável pela autorização e autenticação.
 - Postgresql
 - JWT auth0 (especificamente esse):
```xml
<dependency>
  <groupId>com.auth0</groupId>
  <artifactId>java-jwt</artifactId>
  <version>4.4.0</version>
</dependency>
```
- Spring Reactive (webflux): permite a construção de APIs assíncronas. 

## Instalação

```
mvn install
```

## Diagrama de fluxo do Spring Security

<img src="https://raw.githubusercontent.com/verymew/api-webflux-security-jwt/main/diagrama-security.png">
