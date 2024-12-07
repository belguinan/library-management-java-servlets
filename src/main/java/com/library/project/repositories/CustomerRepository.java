package com.library.project.repositories;

import com.library.project.beans.Customer;

import jakarta.persistence.EntityManagerFactory;

public class CustomerRepository extends DatabaseRepository<Customer> {

    /**
     * @param entityManagerFactory
     */
    public CustomerRepository(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    /**
     * @param customer
     * @return
     * @throws Exception
     */
    public Customer find(long id) {
        return super.find(Customer.class, id);
    }

    /**
     * @param customer
     * @return
     * @throws Exception
     */
    public Customer findOrCreate(Customer customer) throws Exception {
        try {
            Customer dbCustomer = this.findByCin(customer.getCin());

            if (dbCustomer != null) {
                return dbCustomer;
            }

        } catch (Exception e) {
            super.create(customer);
        }

        return customer;
    }

    /**
     * @param cin
     * @return
     */
    public Customer findByCin(String cin)
    {
        return this.entityManager
                .createQuery("SELECT c FROM Customer c WHERE c.cin = :cin", Customer.class)
                .setParameter("cin", cin)
                .setMaxResults(1)
                .getSingleResult();
    }
}
