package org.sid.billingservice;

import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.entities.ProductItem;
import org.sid.billingservice.feign.CustomerRestClient;
import org.sid.billingservice.feign.ProductItemRestClient;
import org.sid.billingservice.model.Customer;
import org.sid.billingservice.model.Product;
import org.sid.billingservice.repositories.BillRepository;
import org.sid.billingservice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BillRepository billRepository  ,
                            ProductItemRepository productItemRepository ,
                            CustomerRestClient customerRestClient,
                            ProductItemRestClient productItemRestClient){
        return args -> {

            Customer customer = customerRestClient.getCustomerById(1L);     //le id est null car en 1er lieu le id n'est pas récupéré dans /CUSTOMER-SERVICE (spring data rest) l faut l'activer dans customer service
            Bill bill1 = billRepository.save(new Bill(null , new Date() , null , customer.getId() , null));
            PagedModel<Product>  productPagedModel = productItemRestClient.pageProducts(1 , 2); //seulement le 3eme 
            productPagedModel.forEach(
                    product -> {
                        ProductItem productItem = new ProductItem();
                        productItem.setPrice(product.getPrice());
                        productItem.setQuantity(1+ new Random().nextInt(100)); //entre 1 et 101
                        productItem.setBill(bill1);
                        productItem.setProductID(product.getId());
                        productItemRepository.save(productItem);
                    }
            );
            System.out.println("Test-------------------");
            System.out.println(customer);


        };
    }

}
