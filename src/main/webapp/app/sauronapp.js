/*********************************************************************
 * Creación del módulo de aplicación sauronApp
 *********************************************************************/
var App = angular.module('sauronApp',['ngResource', 'ngRoute','ngSanitize']);



/*********************************************************************
 * Configura la App
 *********************************************************************/
App.config(function($routeProvider, $locationProvider, $provide,
		$httpProvider) {

	
	/*****************************************************************
	 * Router
	 *****************************************************************/
	$routeProvider
		.when('/', {
			templateUrl : 'app/home/home.html',
			controller : 'HomeCtrl'
		})
		.when('/solicitudes', {
			templateUrl : 'app/solicitud/solicitudes.html',
			controller : 'SolicitudesCtrl'
		})
		.when('/solicitudes/new', {
			templateUrl : 'app/solicitud/datosGenerales-form.html',
			controller : 'DatosGeneralesCtrl'
		})
		.when('/solicitudes/:id/datosgenerales', {
			templateUrl : 'app/solicitud/datosGenerales-form.html',
			controller : 'DatosGeneralesCtrl'
		})
		.otherwise({
			redirectTo : function(){
				console.log("Error de url: ",arguments);
				return '/error';
		}
	});

	// $locationProvider.html5Mode(true);

});


/*********************************************************************
 * Listener Run de la App
 *********************************************************************/
App.run(function($rootScope,$location){
	
	console.log("Sauron Ok!");


	moment.locale('es');

	/*****************************************************************
	 * Ventana modal para los mensajes de la App
	 *****************************************************************/
	var modalMsg = $("#msg-modal").modal({backdrop:false,show:false});

	$rootScope.alert = function(title,messages,type,icon) {
		$rootScope.modalMsg = {};
		$rootScope.modalMsg.title = title;
		$rootScope.modalMsg.messages = messages;
		$rootScope.modalMsg.type = type;
		$rootScope.modalMsg.icon = icon;
		modalMsg.modal('show');
	};

	$rootScope.alertOk = function(title,messages) {
		$rootScope.alert(title,messages,'success','ok');
	};

	$rootScope.alertError = function(title,messages) {
		$rootScope.alert(title,messages,'danger','remove');
	};

	/*****************************************************************
	 * Crea el enlace a la ventana modal de 'Espera' para las llamadas 
	 * Rest.
	 *****************************************************************/
	$rootScope.waitModal=$("#waitmodal").modal({ backdrop: false, show:false });
	
	/*****************************************************************
	 * Crea el enlace a la ventana modal de 'Error' para todas la 
	 * appliación.
	 *****************************************************************/
	$rootScope.errorModal=$("#errormodal").modal({backdrop: false,show:false});
	
	//$location.path("/solicitudes/8/datosgenerales");
	$location.path("/solicitudes");
	
});
