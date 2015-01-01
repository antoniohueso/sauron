Feature: RfcService.resumenRfcs

  Scenario: Si no hay rfcs creadas todas las colecciones que devuelve el resumen deben estar vacias
    Given no hay rfcs creadas
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items

  Scenario: Al crear un rfc debe aparecer esta en la colección de pendientes
    Given no hay rfcs creadas
    And creo una rfc en estado "OPEN"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "pendientes" que devolverá 1 item

  Scenario: Creo un rfc con estado DESARROLLANDO, con fechas de incicio y fin de desarrollo y le asocio una tarea
    Given no hay rfcs creadas
    And una rfc con fecha de desarrollo y tareas asociadads en estado "DESARROLLANDO"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "encurso" que devolverá 1 item

