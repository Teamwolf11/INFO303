/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package router;


import domain.Account;
import domain.Customer;
import domain.Sale;
import domain.Summary;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 *
 * @author villa
 */
public class SaleRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   /*
Vend payload fetched from Vend 
*/
    from("imap://localhost?username=test@localhost"
          + "&port=3143"
          + "&password=password"
          + "&consumer.delay=5000"
          + "&searchTerm.subject=Vend:SaleUpdate")
          .log("Found new E-Mail: ${body}")
          .to("jms:queue:vend-sale");
    
    //converted payload into Sale Object    
    from("jms:queue:vend-sale")
        .unmarshal().json(JsonLibrary.Gson, Sale.class)
        .log("Result : ${body}")
        .removeHeaders("*") // remove headers to stop them being sent to the service
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        .marshal().json(JsonLibrary.Gson)  // only necessary if object needs to be converted to JSON                    
        .to("http://localhost:8081/api/sales") 
        .to("jms:queue:http-sale");
//    
    from("jms:queue:http-sale") 
            
            //             22:15:39 INFO  route12 - CUSTOMER_CLASS: {id=0afa8de1-147c-11e8-edec-25c09e4a6f05, email=doris@example.net, customer_code=Doris-9CR9, first_name=Doris, last_name=Dolores, customer_group_id=0afa8de1-147c-11e8-edec-2b197906d816}
            //extract all customerfields
              
            .setProperty("customerId").jsonpath("$.customer.id")
            .setProperty("customerEmail").jsonpath("$.customer.email")
            .setProperty("customerCode").jsonpath("$.customer.customer_code")
            .setProperty("customerFirst").jsonpath("$.customer.first_name")
            .setProperty("customerLast").jsonpath("$.customer.last_name")
            .setProperty("customerGroup").jsonpath("$.customer.customer_group_id")
            .removeHeaders("*") // remove headers to stop them being sent to the service
            .setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .toD("http://localhost:8081/api/sales/customer/${exchangeProperty.customerId}/summary")
            .unmarshal().json(JsonLibrary.Gson, Summary.class)  
            .log("Summary : ${body}")
            .to("jms:queue:http-summary");
//    
    
            from("jms:queue:http-summary")        
                                .log("CUSTOMER ID IN HTTP-SUMMARY: ${exchangeProperty.customerId}")
                                 .log("CUSTOMER EMAIL IN HTTP-SUMMARY: ${exchangeProperty.customerEmail}")
                                 .log("CUSTOMER CODE IN HTTP-SUMMARY: ${exchangeProperty.customerCode}")
                                 .log("CUSTOMER FIRST IN HTTP-SUMMARY: ${exchangeProperty.customerFirst}")
                                 .log("CUSTOMER LAST IN HTTP-SUMMARY: ${exchangeProperty.customerLast}")
                                 .log("CUSTOMER GROUP IN HTTP-SUMMARY: ${exchangeProperty.customerGroup}")

           .bean(DomainConverter.class, "convertGroup(${body})") //conversion of group id to vend  
           .log("result: ${body}") 
//           .setProperty("summary").simple("${body}")
           .setProperty("summaryID").simple("${body.group}")
          .log("SummaryGroupID: ${exchangeProperty.summaryID}") //can retrain that   
            .choice()
                    .when().simple("${exchangeProperty.summaryID} == ${exchangeProperty.customerGroup}")
                    .to("jms:queue:noChange")
    
            .otherwise()                    
                    
             .bean(DomainConverter.class, "updateAccount(${exchangeProperty.customerId}, ${exchangeProperty.customerEmail}, ${exchangeProperty.customerCode}, ${exchangeProperty.customerFirst} , ${exchangeProperty.customerLast}, ${exchangeProperty.summaryID})")

                .removeHeaders("*")
                                    .setHeader(Exchange.HTTP_METHOD, constant("PUT")) // set HTTP method

                .setHeader(Exchange.CONTENT_TYPE).constant("application/json")  
                                    .marshal().json(JsonLibrary.Gson)  // only necessary if the message is an object, not JSON

                .toD("http://localhost:8086/api/accounts/account/${exchangeProperty.customerId}") 
                .to("jms:queue:AccountResponse");
            
                from("jms:queue:AccountResponse")
                        .unmarshal().json(JsonLibrary.Gson, Account.class) 
                        .log("What's the result here after unmarshalling Account: ${body}")
                        .bean(DomainConverter.class, "updateVend(${body})") //conversion of account to customer to be sent  to vend  
                     .log("What's the result here Customer: ${body}")    
                   
                 .removeHeaders("*")
                .setHeader("Authorization", constant("Bearer KiQSsELLtocyS2WDN5w5s_jYaBpXa0h2ex1mep1a"))
                .setHeader(Exchange.HTTP_METHOD, constant("PUT")) // set HTTP method
                .setHeader(Exchange.CONTENT_TYPE).constant("application/json")  
                .marshal().json(JsonLibrary.Gson)  // only necessary if the message is an object, not JSON
//                .to("http://localhost:8089/whatever")
               .toD("https://info303otago.vendhq.com/api/2.0/customers/${exchangeProperty.customerId}") //send it
                .log("What's the result here Customer: ${body}")    
                .to("jms:queue:VendResponse");        
//                   
//           
  




    }

}
























































































































































































































































































































































































































































