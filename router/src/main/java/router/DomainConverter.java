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
//import domain.Summary;

/**
 *
 * @author villa
 */
public class DomainConverter {
    
    public Customer accountToCustomer(Account account) {
//        Account account = new Account();
        
            
    return new Customer(account.getEmail(), account.getUsername(), account.getFirstName(), account.getLastName(), "0afa8de1-147c-11e8-edec-2b197906d816");
      
  }
    
    public Account customerToAccount(Customer customer){
        
        if ("0afa8de1-147c-11e8-edec-2b197906d816".equals(customer.getGroup())){
        return new Account(customer.getId(), customer.getEmail(), customer.getCustomerCode(), customer.getFirstName(),customer.getLastName(), "Regular Customers");
    } else {
            
                    return new Account(customer.getId(), customer.getEmail(), customer.getCustomerCode(), customer.getFirstName(),customer.getLastName(), "VIP Customers");

        }
    }
     
    public String compareGroupId(Summary summary){
        
        if("Regular Customers".equals(summary.getGroup())){
            return "Regular Customers";
        } else {
            return "VIP Customers";
        }
        
        
    }
    
    
      
    
    public Customer getCustomer(Sale sale){

        return sale.getCustomer();
    }
    
    public Summary convertGroup(Summary summary){
        
        if ("Regular Customers".equals(summary.getGroup())){
            return new Summary(summary.getNumberOfSales(), summary.getTotalPayment(), "0afa8de1-147c-11e8-edec-2b197906d816");
        } else {
            return new Summary(summary.getNumberOfSales(), summary.getTotalPayment(), "0afa8de1-147c-11e8-edec-201e0f00872c");
        }
    }
    
    //customerId}, ${exchangeProperty.customerEmail}, ${exchangeProperty.customerCode}, ${exchangeProperty.customerFirst} ,${exchangeProperty.customerGroup},  ${exchangeProperty.summaryID})")

    
//    public Customer updateVend(String customerId, String summaryID, String customerEmail){
//        
//     Customer cust = new Customer();
//                cust.setId(customerId);        
//                cust.setGroup(summaryID); 
//                cust.setEmail(customerEmail);
//
//    
//    return cust;
//    }
    
    
    
    
        public Customer updateVend(Account account){
            
     Customer cust = new Customer();
     
            cust.setId(account.getId());
            cust.setEmail(account.getEmail());
            cust.setCustomerCode(account.getUsername());
            cust.setFirstName(account.getFirstName());
            cust.setLastName(account.getLastName());
            
            if ("Regular Customers".equals(account.getGroup())){
                
            
         
                cust.setGroup("0afa8de1-147c-11e8-edec-2b197906d816");
            } else {
                cust.setGroup("0afa8de1-147c-11e8-edec-201e0f00872c");
            }
            
    
    return cust;
    }
    
    public Account updateAccount(String customerId, String customerEmail, String customerCode, String customerFirst, String customerLast, String customerGroup, String summaryID){
     
    Account account = new Account();
    
            account.setId(customerId);
            account.setEmail(customerEmail);
            account.setUsername(customerCode);     
            account.setFirstName(customerFirst);
            account.setLastName(customerLast);
            
            if ("0afa8de1-147c-11e8-edec-2b197906d816".equals(summaryID)){
            account.setGroup("Regular Customers");
            } else {
                account.setGroup("VIP Customers");
            }
    
return account;
    }
}












































































