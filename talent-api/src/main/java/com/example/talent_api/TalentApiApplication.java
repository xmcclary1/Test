package com.example.talent_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.example.talent_api.domain.User;
import com.example.talent_api.repository.UserRepository;

@SpringBootApplication
@EnableMongoRepositories
public class TalentApiApplication implements CommandLineRunner {

	@Autowired UserRepository CustRepo;

	public static void main(String[] args) {
		SpringApplication.run(TalentApiApplication.class, args);
	}

	public void run(String... args) {
        CustRepo.deleteAll();

        System.out.println("-------------CREATE Customers-----------------------------------\n");
        createCustomers();
        System.out.println("-------------Creation Complete-----------------------------------\n");

	}

	void createCustomers() {
		System.out.println("Customer creation started..."); 

		try {
			User cust = new User();
			cust.setFirstname("Connor");
			cust.setUsername("connor");
			cust.setRole("Admin");

			CustRepo.save(cust);

			User cust1 = new User();
			cust1.setFirstname("Ron");
			cust1.setUsername("ron");
			cust1.setRole("applicant");

			CustRepo.save(cust1);

			User cust2 = new User();
			cust2.setFirstname("Nadia");
			cust2.setUsername("nadia");
			cust2.setRole("manager");

			CustRepo.save(cust2);

        } catch (Exception e) {
            e.printStackTrace();
        }

		System.out.println("Customer creation complete...");
	}



}
