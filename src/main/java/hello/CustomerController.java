package hello;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

	private CustomerRepository customerRepository;
	
	public CustomerController( CustomerRepository customerRepository ) {
		this.customerRepository = customerRepository;
	}

	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Customer> create(@RequestBody Customer customer) {	
		return new ResponseEntity<>(customerRepository.save(customer), HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Customer> getAll() {
		return customerRepository.findAll();
	}
}