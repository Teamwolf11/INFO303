/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router;

import domain.Account;
import domain.Customer;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 *
 * @author villa
 */
public class AccountCreationRouterBuilder extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        //message coming from ajax client - to accounts-Ajax queue
        from("jetty:http://localhost:9000/?enableCORS=true")
                .setExchangePattern(ExchangePattern.InOnly)            // make message in-only so web browser doesn't have to wait on a non-existent response
                .log("${body}")
                .to("jms:queue:accounts-Ajax");

        // translate from json to Account Object
        from("jms:queue:accounts-Ajax")
                .unmarshal().json(JsonLibrary.Gson, Account.class)
                .to("jms:queue:accountObject");

        //convert Account object to customer object - compatible with vend - using .bean     
        from("jms:queue:accountObject")
                .bean(DomainConverter.class, "accountToCustomer(${body})")
                .to("jms:queue:customerObject");


        //Convert the object back in to JSON.  Log the message at this point and verify that matches 
        // the format that Vend expect (look at the Vend API docs).
      
        //Send the message to Vend to create the customer and capture the response in another queue
        from("jms:queue:customerObject")
                .removeHeaders("*") // remove headers so they don't get sent to Vend
                .setHeader("Authorization", constant("Bearer KiQSsELLtocyS2WDN5w5s_jYaBpXa0h2ex1mep1a"))
                .marshal().json(JsonLibrary.Gson) // marshal to JSON only necessary if the message is an object, not JSON
                .setHeader(Exchange.CONTENT_TYPE).constant("application/json")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))         // set HTTP method
                .to("https://info303otago.vendhq.com/api/2.0/customers")        // send it
                .to("jms:queue:vend-response");         // store the response

//
        //extract customer from Vend that is wrapped in a field named 'data'
        from("jms:queue:vend-response")
                .setBody().jsonpath("$.data")
                .marshal().json(JsonLibrary.Gson)
                .to("jms:queue:extracted-response");
    
        //unmarshal response into Customer Object
        from("jms:queue:extracted-response")
                .unmarshal().json(JsonLibrary.Gson, Customer.class)
                .to("jms:queue:newCustomerObject");
          
        //use a bean to convert customerObject back to account object   
        from("jms:queue:newCustomerObject")
                .bean(DomainConverter.class, "customerToAccount(${body})")
                .to("jms:queue:newAccountObject");
        
        
       //marshal object back into JSON and send it to  Accounts service
        from("jms:queue:newAccountObject")
                .removeHeaders("*") // remove headers to stop them being sent to the service
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .marshal().json(JsonLibrary.Gson)  // only necessary if object needs to be converted to JSON
                .to("http://localhost:8086/api/accounts")
                .to("jms:queue:vend-response");  // HTTP response ends up in this queue

        }
    
    
        }
















































































