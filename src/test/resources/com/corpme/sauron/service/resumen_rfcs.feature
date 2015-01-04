@resumenRfcs

Feature: Resumen de rfcs en curso.
  Como usuario de la aplicación Sauron.
  Quiero poder consultar el resumen de rfcs en curso.
  Para poder ver las rfcs en curso clasificadas de la siguiente manera:

  1) Pendientes:
  Rfcs en estado OPEN.

  2) Paradas:
  Rfcs en estado DETENIDA.

  3) En curso:
  Rfcs en estado DESARROLLANDO, DISPONIBLE_PARA_PRUEBAS, DETECTADO_ERROR_PRUEBAS, PROBANDO y FINALIZADA.

  Estos estados se agrupan lógicamente de la siguiente manera:

  En desarrollo (DESARROLLANDO) -> Las Rfcs que estén en este estado tienen que cumplir las siguientes reglas:

  a) Tener al menos una issue asociada.
  b) Tener una fecha de inicio y de fin de desarrollo válida, ninguna de las dos puede ser null.
  c) La fecha de inicio no puede ser mayor que la de fin.
  d) La fecha de fin no puede inferior al día actual, si es así esa rfc aparecerá clasificada como Vencida.

  En pruebas (DISPONIBLE_PARA_PRUEBAS,PROBANDO,DETECTADO_ERROR_PRUEBAS) -> Las Rfcs que estén en este estado tienen
  que cumplir las siguientes reglas:

  a) Cumplir los apartados a,b y c anteriores.
  b) Tener una fecha de inicio y de fin de calidad válida, ninguna de las dos puede ser null.
  c) La fecha de inicio no puede ser mayor que la de fin.
  d) La fecha de fin no puede ser inferior al día actual, si es así esa rfc aparecerá clasificada como Vencida.

  Finalizada (FINALIZADA) -> Las Rfcs que estén en este estado tienen.
  que cumplir las siguientes reglas:

  a) Cumplir los apartados a,b y c anteriores.
  b) Tener una fecha de paso a producción válida, no puede ser null.
  c) La fecha de paso a producción no puede inferior al día actual, si es así esa rfc aparecerá clasificada
  como Vencida.

  4) Anomalías:
  Aparecerán las rfcs que no cumplan cualquiera de las reglas descritas en el punto 3 (excepto las rfcs vencidas),
  en este caso además aparecerá junto a rfc una descripción de la anomalía detectada.

  5) Vencidas:
  Aparecerán las rfcs que, según lo descrito en el apartado 3 la fecha de planificación correspondiente al estado en
  el que se encuentre la rfc esté vencida.

  En este resumen no deberán aparecer rfcs en estado CLOSED o EN_PRODUCCION.

  Scenario: En el resumen no deberán aparecer rfcs en estado CERRADA o EN_PRODUCCION.
    Given 1 rfc creada en estado "CERRADA"
    And 1 rfc creada en estado "EN_PRODUCCION"
    When Consulto el resumen de rfcs
    Then No debe devolver ninguna RFC

  Scenario: En el resumen deberá aparecer clasificadas como En curso las tareas RFCS en estado En pruebas que
  cumplan las reglas descritas en el enunciado.
    Given 1 rfc valida creada en estado "DESARROLLANDO"
    And 1 rfc valida creada en estado "DISPONIBLE_PARA_PRUEBAS"
    And 1 rfc valida creada en estado "PROBANDO"
    And 1 rfc valida creada en estado "DETECTADO_ERROR_PRUEBAS"
    And 1 rfc valida creada en estado "FINALIZADA"
    And 1 rfc creada en estado "DETENIDA"
    And 1 rfc creada en estado "OPEN"
    When Consulto el resumen de rfcs
    Then Debe devolver 5 rfc en la clasificación "encurso"
    And  Debe devolver 1 rfc en la clasificación "paradas"
    And  Debe devolver 1 rfc en la clasificación "pendientes"
