package org.sid.billingservice.repositories;

import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;

@CrossOrigin(origins = "http://localhost:4200")
@RepositoryRestResource
public interface BillRepository extends JpaRepository<Bill , Long> {

}
