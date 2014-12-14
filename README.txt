Para trabajar en desarrollo sin reiniciar cada vez que se hacen cambios en las clases (incluido añadir nuevos métodos) :
Arrancar el proyecto en eclipse añadiendo esta línea:

-javaagent:c:/Users/ahg/Documents/idea_workspace/custodes/lib/springloaded-1.2.1.RELEASE.jar -noverify

Para arrancar spring en modo producción:

-Dspring.profiles.active=prod


Ejemplo de desarrollo

-javaagent:c:/Users/ahg/Documents/idea_workspace/custodes/lib/springloaded-1.2.1.RELEASE.jar -noverify -noverify -Dspring.profiles.active=casa