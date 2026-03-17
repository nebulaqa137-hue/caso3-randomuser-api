Feature: Randomuser API - Validaciones de endpoints y parametros

  Background:
    Given el cliente tiene configurado RestAssured con la URL base de Randomuser

  @api @smoke
  Scenario: GET usuario aleatorio retorna status 200
    When hace GET al endpoint raiz
    Then el status code debe ser 200
    And la respuesta debe contener al menos 1 usuario

  @api @smoke
  Scenario: GET multiples usuarios con parametro results
    When hace GET con parametro results igual a 5
    Then el status code debe ser 200
    And la respuesta debe contener exactamente 5 usuarios

  @api @regression
  Scenario: Filtrar usuarios por genero male
    When hace GET con parametro gender igual a "male"
    Then el status code debe ser 200
    And todos los usuarios deben tener genero "male"

  @api @regression
  Scenario: Filtrar usuarios por genero female
    When hace GET con parametro gender igual a "female"
    Then el status code debe ser 200
    And todos los usuarios deben tener genero "female"

  @api @regression
  Scenario: Filtrar usuarios por nacionalidad MX
    When hace GET con parametro nat igual a "MX"
    Then el status code debe ser 200
    And todos los usuarios deben tener nacionalidad "MX"

  @api @regression
  Scenario: Seed produce resultados reproducibles
    When hace GET dos veces con el mismo seed
    Then ambas respuestas deben tener el mismo email

  @api @regression
  Scenario: Campos especificos con parametro inc
    When hace GET con parametro inc igual a "name,email,phone"
    Then el status code debe ser 200
    And la respuesta debe contener los campos name email y phone
    And la respuesta NO debe contener el campo gender

  @api @regression
  Scenario: Validar formato de email en respuesta
    When hace GET con parametro results igual a 5
    Then el status code debe ser 200
    And todos los emails deben tener formato valido

  @api @regression
  Scenario: Validar campos de ubicacion en respuesta
    When hace GET al endpoint raiz
    Then el status code debe ser 200
    And la respuesta debe contener campos de ubicacion
