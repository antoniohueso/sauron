@rfcsEvents

Feature: RfcService.rfcsEvents

  Consultar las Rfcs que están en curso en un periodo de fechas desde y hasta. Devuelve un array de eventos, si una rfc
  está en ese periodo aparecerá en el array con un evento para cada día que hay en ese periodo excepto el sábado y
  el domingo. Los eventos tendrán una propiedad className que será de tipo calendar-warning si es un periodo correspondiente
  a la planificación de calidad, calendar-normal si corresponde a la planificación de desarrollo y calendar-produccion si
  correspode al evento de la fecha planificada para producción.
  Si la rfc está en estado Cerrada o en Producción deberá contener en el campo title el literal (P) y si está
  vencido deberá contener el literal (V).

  Scenario: Consulta de rfcs Ok
    Given no hay rfcs creadas
    Given Creo rfcs con el siguiente juego de datos:
    |1000|15/07/2014|13/08/2014|17/08/2014|20/08/2014|21/08/2014|
    |1001|01/07/2014|15/07/2014|16/07/2014|17/07/2014|19/07/2014|
    |1002|02/07/2014|05/07/2014||||
    |1003|07/07/2014|08/07/2014|09/07/2014|10/07/2014||
    When realizo la consulta de los eventos rfcs

