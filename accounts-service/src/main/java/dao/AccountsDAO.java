package dao;

import domain.Account;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AccountsDAO {

	private static final Map<String, Account> customers = new HashMap<>();
        
        
        /*
        * Some dummy data for testing
        */
        
        static{
            if(customers.isEmpty()){
                
                
                

                
                customers.put("01", new Account("01", "username", "email@email.com", "firstname" , "lastName" , "Regular Customers"));
//                customers.put("02", new Account("02", "user", "first" , "last" , "email@gmail.com", "Regular"));
                Account account = new Account();
                account.setId("0afa8de1-147c-11e8-edec-25c09e4a6f05");
                account.setUsername("Doris-9CR9");
                account.setLastName("Dolores");
                account.setFirstName("Doris");
                account.setGroup("Regular Customers");
                account.setUri("/api/accounts/account/0afa8de1-147c-11e8-edec-25c09e4a6f05");
                customers.put("0afa8de1-147c-11e8-edec-25c09e4a6f05", account); 
                
               
            }
        }
	public Collection<Account> getAll() {
		return new ArrayList<>(customers.values());
	}

	public void save(Account customer) {
		customers.put(customer.getId(), customer);
	}

	public Account get(String id) {
		return customers.get(id);
	}

	public void delete(String id) {
		customers.remove(id);
	}

	public void update(String id, Account updatedCustomer) {
		customers.put(id, updatedCustomer);
	}

	public boolean exists(String id) {
		return customers.containsKey(id);
	}

}
