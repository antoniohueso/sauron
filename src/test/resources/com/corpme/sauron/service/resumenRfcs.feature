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

  Scenario: Creo un rfc con estado DESARROLLANDO, con fechas de inicio y fin de desarrollo y le asocio una tarea
    Given no hay rfcs creadas
    And una rfc con fecha de desarrollo y tareas asociadas en estado "DESARROLLANDO"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "encurso" que devolverá 1 item

  Scenario: Creo un rfc con estado DISPONIBLE PARA PRUEBAS, con fechas de incicio y fin de desarrollo y las fechas de inicio y fin de calidad
            y le asocio una tarea
    Given no hay rfcs creadas
    And una rfc con fechas de desarrollo, calidad y tareas asociadas en estado "DISPONIBLE_PARA_PRUEBAS"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "encurso" que devolverá 1 item

  Scenario: Creo un rfc con estado PROBANDO, con fechas de incicio y fin de desarrollo y las fechas de inicio y fin de calidad
  y le asocio una tarea
    Given no hay rfcs creadas
    And una rfc con fechas de desarrollo, calidad y tareas asociadas en estado "PROBANDO"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "encurso" que devolverá 1 item

  Scenario: Creo un rfc con estado DETECTADO ERROR EN PRUEBAS, con fechas de incicio y fin de desarrollo y las fechas de inicio y fin de calidad
  y le asocio una tarea
    Given no hay rfcs creadas
    And una rfc con fechas de desarrollo, calidad y tareas asociadas en estado "DETECTADO_ERROR_PRUEBAS"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "encurso" que devolverá 1 item

  Scenario: Creo un rfc con estado FINALIZADO, con fechas de incicio y fin de desarrollo y las fechas de inicio y fin de calidad
  y fecha de paso a producción y le asocio una tarea
    Given no hay rfcs creadas
    And una rfc con fechas de desarrollo, calidad y tareas asociadas y fecha de paso a producción en estado "FINALIZADA"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "encurso" que devolverá 1 item

  Scenario: Las rfc con estado CERRADO o EN PRODUCCION no deben salir en el resumen
    Given no hay rfcs creadas
    And creo una rfc en estado "CERRADA"
    And creo una rfc en estado "EN_PRODUCCION"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items

  Scenario: Las rfc con estado DETENIDA deben salir en la colección "paradas"
    Given no hay rfcs creadas
    And creo una rfc en estado "DETENIDA"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "paradas" que devolverá 1 item

  Scenario: Anomalías: Las rfcs creadas con cualquier estado menos CERRADAS o EN PRODUCCION sin
  fechas de planificación de desarrollo, de calidad o de producción, ni tareas asociadas deben salir en la colección anomalias
    Given no hay rfcs creadas
    And creo una rfc en estado "DESARROLLANDO"
    And creo una rfc en estado "DISPONIBLE_PARA_PRUEBAS"
    And creo una rfc en estado "PROBANDO"
    And creo una rfc en estado "DETECTADO_ERROR_PRUEBAS"
    And creo una rfc en estado "FINALIZADA"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "anomalias" que devolverá 5 items

  Scenario: Anomalías: Las rfcs creadas con cualquier estado menos CERRADAS o EN PRODUCCION sin
  fechas de planificación de desarrollo, de calidad o de producción, ni tareas asociadas deben salir en la colección anomalias
    Given no hay rfcs creadas
    And creo una rfc en estado "DESARROLLANDO"
    And creo una rfc en estado "DISPONIBLE_PARA_PRUEBAS"
    And creo una rfc en estado "PROBANDO"
    And creo una rfc en estado "DETECTADO_ERROR_PRUEBAS"
    And creo una rfc en estado "FINALIZADA"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "anomalias" que devolverá 5 items

  Scenario: Anomalías: Las rfcs creadas sin tareas asociadas con cualquier estado menos CERRADAS o EN PRODUCCION
  deben salir en la colección anomalias
    Given no hay rfcs creadas
    And creo una rfc sin links en estado "DESARROLLANDO"
    And creo una rfc sin links en estado "DISPONIBLE_PARA_PRUEBAS"
    And creo una rfc sin links en estado "PROBANDO"
    And creo una rfc sin links en estado "DETECTADO_ERROR_PRUEBAS"
    And creo una rfc sin links en estado "FINALIZADA"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "anomalias" que devolverá 5 items

  Scenario: Anomalías: Las rfcs en estados de DESARROLLANDO creadas sin fecha de planificación de desarrollo
  deben salir en la colección anomalias
    Given no hay rfcs creadas
    And creo una rfc sin fecha de planificación de desarrollo en estado "DESARROLLANDO"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "anomalias" que devolverá 1 items


  Scenario: Anomalías: Las rfcs en estados de DISPONIBLE PARA PRUEBAS, DETECTADO ERROR PRUEBAS o PROBANDO
  creadas sin fecha de planificación de calidad deben salir en la colección anomalias
    Given no hay rfcs creadas
    And creo una rfc sin fecha de planificación de calidad en estado "DISPONIBLE_PARA_PRUEBAS"
    And creo una rfc sin fecha de planificación de calidad en estado "PROBANDO"
    And creo una rfc sin fecha de planificación de calidad en estado "DETECTADO_ERROR_PRUEBAS"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "anomalias" que devolverá 3 items

  Scenario: Anomalías: Las rfcs en estados de FINALIZADA creadas sin fecha de planificación
  de paso a producción deben salir en la colección anomalias
    Given no hay rfcs creadas
    And creo una rfc sin fecha de planificación de paso a producción en estado "FINALIZADA"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "anomalias" que devolverá 1 items

  Scenario: Anomalías: No debe haber rfcs en cualquier estado excepto EN PRODUCCIÓN o CERRADA que estén planificadas
  cuya fecha de inicio sea mayor que la de fin y deben salir en la colección anomalias
    Given no hay rfcs creadas
    And creo una rfc con fecha de planificación inicio mayor que la de fin en estado "DESARROLLANDO"
    And creo una rfc con fecha de planificación inicio mayor que la de fin en estado "DISPONIBLE_PARA_PRUEBAS"
    And creo una rfc con fecha de planificación inicio mayor que la de fin en estado "PROBANDO"
    And creo una rfc con fecha de planificación inicio mayor que la de fin en estado "DETECTADO_ERROR_PRUEBAS"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "anomalias" que devolverá 4 items

  Scenario: Vencidas: Las Rfcs cuya fecha de fin de planificación sea menor que la de hoy aparecerá en la colección de "vencidas"
  excepto para los estados EN PRODUCCIÓN o CERRADA. Siempre se fijará en la fecha de fin del estado en el que se encuentre, es decir
  si se encuentra en un estado DESARROLLANDO se fijará en la fecha de fin de desarrollo, si está en DISPONIBLE PARA PRUEBAS, PROBANDO
  o DETECTADO ERROR EN PRUEBAS se fijará en la fecha de fin de calidad y si está FINALIZADA en la fecha de paso a producción
    Given no hay rfcs creadas
    And creo una rfc con fecha de planificación vencida en estado "DESARROLLANDO"
    And creo una rfc con fecha de planificación vencida en estado "DISPONIBLE_PARA_PRUEBAS"
    And creo una rfc con fecha de planificación vencida en estado "PROBANDO"
    And creo una rfc con fecha de planificación vencida en estado "DETECTADO_ERROR_PRUEBAS"
    And creo una rfc con fecha de planificación vencida en estado "FINALIZADA"
    When consulto el resumen de rfcs
    Then debe devolver 5 colecciones con un total de 0 items en todas excepto en la colección "vencidas" que devolverá 5 items

