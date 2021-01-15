package domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Customer implements Serializable {

	private String id;
        
        private String email;
        
        @SerializedName("customer_code")
        private String customerCode;
        
         @SerializedName("first_name")
        private String firstName;

        @SerializedName("last_name")
        private String lastName;

	@SerializedName("customer_group_id")
	private String group;

    public Customer( String email, String customerCode, String firstName, String lastName, String group) {
        
        this.email = email;
        this.customerCode = customerCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        
        
        
    }

    
    public Customer(String group) {
        this.group = group;
    }

    public Customer(String id, String group, String email) {
        this.id = id;
        this.email = email;
        this.group = group;
    }

	
        
       




	public Customer() {
	}


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

        
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", email=" + email + ", customerCode=" + customerCode + ", firstName=" + firstName + ", lastName=" + lastName + ", group=" + group + '}';
    }

	



}
