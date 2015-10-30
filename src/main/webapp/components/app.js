'use strict';

import {JiraRestClient} from "../components/jirarest.js"

export class HomeComponent {

    beforeRegister() {
        this.is = 'sauron-home';

        this.data = {
            data: []
        };
    }

    ready() {

    }

}


Polymer(HomeComponent);

