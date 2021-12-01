package org.sid.billingservice.web;

import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.feign.CustomerRestClient;
import org.sid.billingservice.feign.ProductItemRestClient;
import org.sid.billingservice.model.Customer;
import org.sid.billingservice.model.Product;
import org.sid.billingservice.repositories.BillRepository;
import org.sid.billingservice.repositories.ProductItemRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class BillingRestController {

    private BillRepository billRepository;
    private CustomerRestClient customerRestClient;
    private ProductItemRepository productItemRepository;
    private ProductItemRestClient productItemRestClient;

    public BillingRestController(BillRepository billRepository, CustomerRestClient customerRestClient, ProductItemRepository productItemRepository, ProductItemRestClient productItemRestClient) {
        this.billRepository = billRepository;
        this.customerRestClient = customerRestClient;
        this.productItemRepository = productItemRepository;
        this.productItemRestClient = productItemRestClient;
    }

    @GetMapping(path = "/fullBill/{id}")
    public Bill getBill(@PathVariable(name = "id") Long id){
        Bill bill = billRepository.findById(id).get();
        Customer customer = customerRestClient.getCustomerById(bill.getCustomerID());
        bill.setCustomer(customer);
        bill.getProductItems().forEach(
                productItem -> {
                    Product p = productItemRestClient.getProductById(productItem.getProductID());
                    //productItem.setProduct(p);
                    productItem.setProductName(p.getName());
                }
        );
        return bill;
    }
}
