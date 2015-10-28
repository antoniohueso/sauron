@ECHO OFF

set VERSION=2.0.0-alpha.44


curl https://code.angularjs.org/tools/system.js --proxy 10.255.254.65:8080 --proxy-user ahg:ahg191907 > system.js

curl https://code.angularjs.org/tools/typescript.js --proxy 10.255.254.65:8080 --proxy-user ahg:ahg191907 > typescript.js

curl https://code.angularjs.org/%VERSION%/angular2.dev.js --proxy 10.255.254.65:8080 --proxy-user ahg:ahg191907 > angular.js

curl https://code.angularjs.org/%VERSION%/router.dev.js --proxy 10.255.254.65:8080 --proxy-user ahg:ahg191907 > router.js

curl https://code.angularjs.org/%VERSION%/http.dev.js --proxy 10.255.254.65:8080 --proxy-user ahg:ahg191907 > http.js

 