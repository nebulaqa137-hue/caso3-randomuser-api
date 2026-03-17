[README-caso3-randomuser-api.md](https://github.com/user-attachments/files/26058439/README-caso3-randomuser-api.md)
# Caso 3 - Randomuser API Testing

## Descripción

Proyecto de automatización de pruebas para la API pública [randomuser.me](https://randomuser.me), desarrollado como parte del caso práctico de QA para Envíoclick.

Valida de forma automatizada los diferentes endpoints, parámetros y opciones que expone la API: filtros por género y nacionalidad, reproducibilidad por semilla, selección y exclusión de campos específicos, y validaciones de integridad sobre los datos retornados. No requiere navegador ni Docker, es prueba de API pura implementada con RestAssured y Cucumber.

---

## Instalación

**Prerequisitos**

- Java 21
- Maven 3.8 o superior
- Conexión a internet
- Git

**Pasos**

```bash
git clone https://github.com/tu-usuario/caso3-randomuser-api.git
cd caso3-randomuser-api
mvn clean install -DskipTests
```

La API es pública y no requiere autenticación ni configuración adicional.

---

## Uso

Ejecutar la suite completa:

```bash
mvn clean test
```

Ejecutar por etiquetas:

```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
mvn clean test -Dcucumber.filter.tags="@regression"
mvn clean test -Dcucumber.filter.tags="@api"
```

Desde Eclipse: click derecho en `testng.xml` → Run As → TestNG Suite.

Los reportes se generan en `reports/` al finalizar.

---

## Configuración

Archivo: `src/test/resources/config.properties`

```properties
base.url=https://randomuser.me/api
connection.timeout=10000
read.timeout=15000
default.results=5
seed=envioclick2024
nat.mx=MX
gender.male=male
gender.female=female
```

No se requieren credenciales. La API randomuser.me es completamente gratuita y pública.

---

## Estructura del Proyecto

```
src/test/java/
  hooks/           - configuracion de RestAssured
  stepdefinitions/ - steps de cucumber
  runners/         - configuracion del runner
  utils/           - lector de configuracion

src/test/resources/
  features/        - escenarios gherkin
  config.properties
  testng.xml
```

---

## Estructura del Framework

**Hooks**
Antes de cada escenario configura `RestAssured.baseURI` con la URL leída de `config.properties` y activa los filtros de logging para trazar request y response en consola. Al finalizar hace `RestAssured.reset()` para limpiar el estado.

**ApiConfigManager**
Singleton que carga `config.properties`. Expone getters tipados para la URL base, la semilla de prueba, el código de nacionalidad y los valores de género.

**RandomuserSteps**
Contiene todos los steps del feature. Ejecuta los requests con el DSL de RestAssured (`given/when/then`), almacena las respuestas en variables de instancia para compartirlas entre steps del mismo escenario, y hace las validaciones con Hamcrest y TestNG Assert.

**TestRunner**
Configura `@CucumberOptions` y extiende `AbstractTestNGCucumberTests` para integrar Cucumber con TestNG como runner.

---

## Casos de Prueba

| Escenario | Tag | Validación |
|---|---|---|
| GET usuario aleatorio retorna status 200 | @smoke | Status 200, lista no vacía, campos name, email, gender presentes |
| GET múltiples usuarios con results=5 | @smoke | La lista contiene exactamente 5 elementos |
| Filtro por género male | @regression | Todos los usuarios retornados tienen gender: male |
| Filtro por género female | @regression | Todos los usuarios retornados tienen gender: female |
| Filtro por nacionalidad MX | @regression | Todos los usuarios retornados tienen nat: MX |
| Seed produce resultados reproducibles | @regression | Dos requests con el mismo seed retornan el mismo email |
| Campos específicos con parámetro inc | @regression | Presencia de name, email, phone. Ausencia de gender |
| Exclusión de campos con parámetro exc | @regression | Ausencia de login, id, registered en la respuesta |
| Validación de formato de email | @regression | Todos los emails cumplen expresión regular estándar |
| Validación de campos de ubicación | @regression | Presencia de location.city, location.country, location.state |

---

## API Bajo Prueba

**Base URL**

```
https://randomuser.me/api
```

**Método y endpoint**

```
GET /
```

**Parámetros soportados**

| Parámetro | Ejemplo | Descripción |
|---|---|---|
| results | ?results=5 | Número de usuarios a retornar |
| gender | ?gender=male | Filtrar por male o female |
| nat | ?nat=MX | Filtrar por nacionalidad ISO |
| seed | ?seed=abc | Semilla para resultados reproducibles |
| inc | ?inc=name,email | Incluir solo los campos indicados |
| exc | ?exc=login,id | Excluir los campos indicados |

**Ejemplo de respuesta**

```json
{
  "results": [{
    "gender": "male",
    "name": { "first": "Carlos", "last": "Ramirez" },
    "email": "carlos.ramirez@example.com",
    "nat": "MX",
    "location": {
      "city": "Guadalajara",
      "state": "Jalisco",
      "country": "Mexico"
    }
  }],
  "info": {
    "seed": "envioclick2024",
    "results": 1,
    "version": "1.4"
  }
}
```

---

## Tecnologías Usadas

| Tecnología | Versión |
|---|---|
| Java | 21 |
| RestAssured | 5.4.0 |
| Cucumber | 7.15.0 |
| TestNG | 7.10.2 |
| Jackson Databind | 2.17.1 |
| ExtentReports | 5.1.1 |
| Log4j | 2.23.1 |
| Maven | 3.8+ |

---

## Contribución

```bash
git checkout -b feature/nombre-del-cambio
git commit -m "descripcion del cambio"
git push origin feature/nombre-del-cambio
```

Abrir Pull Request describiendo qué endpoint o parámetro se cubrió y la validación implementada.

---



Proyecto desarrollado como evaluación técnica de QA para Envíoclick. Uso educativo.
