/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

"use strict";

let module = angular.module('AccountModule', ['ngResource']);

let serviceURI = 'http://localhost:8086/api';
module.factory('accountCollectionApi', function ($resource) {
return $resource(serviceURI + '/accounts');
});


module.factory('accountApiJetty', function ($resource) {
return $resource('http://localhost:9000/',
null, {update: {method: 'POST'}});
});


module.controller('AccountController', function (accountCollectionApi, accountApiJetty){
this.test = "It works!";


// save 'this' so we can access it from other scopes
let ctrl = this;
// get all customers and load them into the 'customers' model

ctrl.accounts = accountCollectionApi.query();

this.addAccount = function(accountToAdd) {    
    accountApiJetty.save({}, accountToAdd, function(){
        ctrl.accounts = accountCollectionApi.query();
    });

};

});







