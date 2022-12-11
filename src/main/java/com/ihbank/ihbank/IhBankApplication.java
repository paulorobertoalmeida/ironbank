package com.ihbank.ihbank;

import com.ihbank.ihbank.models.accounts.Checking;
import com.ihbank.ihbank.models.accounts.CreditCard;
import com.ihbank.ihbank.models.accounts.Savings;
import com.ihbank.ihbank.models.accounts.StudentChecking;
import com.ihbank.ihbank.models.users.*;
import com.ihbank.ihbank.repositories.accounts.*;
import com.ihbank.ihbank.repositories.users.AccountHolderRepository;
import com.ihbank.ihbank.repositories.users.AdminRepository;
import com.ihbank.ihbank.repositories.users.RoleRepository;
import com.ihbank.ihbank.repositories.users.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class IhBankApplication implements CommandLineRunner {

	//autowired a las accounts
	@Autowired
	AccountRepository accountRepository;

	@Autowired
	CheckingRepository checkingRepository;

	@Autowired
	CreditCardRepository creditCardRepository;

	@Autowired
	SavingsRepository savingsRepository;

	@Autowired
	StudentCheckingRepository studentCheckingRepository;

	@Autowired
	RoleRepository roleRepository;

	//autowired a los users

	@Autowired
	AccountHolderRepository accountHolderRepository;

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	ThirdPartyRepository thirdPartyRepository;

	PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}


	public static void main(String[] args) {
		SpringApplication.run(IhBankApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		//I INSTANTIATED MANY ADDRESSES, ACCOUNTS, AND USERS TO MAKE THE TEST EASIER

		Address address1 = new Address("Avenue 1", 1, 1111, "Rio de Janeiro", "Brasil");
		Address address2 = new Address("Avenue 2", 2, 2222, "Salvador", "Brasil");
		Address address3 = new Address("Avenue 3", 3, 3333, "Sao Paulo", "Brasil");
		Address address4 = new Address("Avenue 4", 4, 4444, "Curitiba", "Brasil");
		Address address5 = new Address("Avenue 5", 5, 5555, "Natal", "Brasil");
		Address address6 = new Address("Avenue 6", 6, 6666, "Joao Pessoa", "Brasil");

		AccountHolder user1 = new AccountHolder("Carlos", passwordEncoder().encode("123banana"), "Felipe", LocalDate.of(1982, 12, 12), address1, address2);
		accountHolderRepository.save(user1);

		AccountHolder user2 = new AccountHolder("Aitana", passwordEncoder().encode("123banana"), "Pablo", LocalDate.of(1970, 3, 26), address2);
		accountHolderRepository.save(user2);

		AccountHolder user3 = new AccountHolder("Joana", passwordEncoder().encode("123banana"), "Maria", LocalDate.of(1991, 5, 07), address3);
		accountHolderRepository.save(user3);

		AccountHolder user4 = new AccountHolder("Carla", passwordEncoder().encode("123banana"), "Leo", LocalDate.of(1987, 7, 24), address4);
		accountHolderRepository.save(user4);

		AccountHolder user5 = new AccountHolder("Mariana", passwordEncoder().encode("123banana"), "Theo", LocalDate.of(1935, 2, 14), address5, address6);
		accountHolderRepository.save(user5);


		Checking checking1 = new Checking(new BigDecimal("580.0"), user1, null, "bananabank2");
		checkingRepository.save(checking1);

		Checking checking2 = new Checking(new BigDecimal("1200.0"), user2, null, "bananabank2");
		checkingRepository.save(checking2);

		Checking checking3 = new Checking(new BigDecimal("3000.0"), user3, null, "bananabank2");
		checkingRepository.save(checking3);


		CreditCard creditCard1 = new CreditCard(new BigDecimal("20000.0"), user1, null, new BigDecimal("700.0"), new BigDecimal("0.1"));
		creditCardRepository.save(creditCard1);

		CreditCard creditCard2 = new CreditCard(new BigDecimal("17.0"), user2, null, new BigDecimal("980.0"), new BigDecimal("0.2"));
		creditCardRepository.save(creditCard2);

		CreditCard creditCard3 = new CreditCard(new BigDecimal("40.0"), user3, null, new BigDecimal("2700.0"), new BigDecimal("0.01"));
		creditCardRepository.save(creditCard3);

		CreditCard creditCard4 = new CreditCard(new BigDecimal("900.0"), user4, null, new BigDecimal("170000.50"), new BigDecimal("0.3"));
		creditCardRepository.save(creditCard4);


		Savings saving1 = new Savings(new BigDecimal("71.0"), user1, null, new BigDecimal("0.0025"), new BigDecimal("2000"), "7bananas1manzana");
		savingsRepository.save(saving1);

		Savings saving2 = new Savings(new BigDecimal("133.0"), user2, null, new BigDecimal("0.0025"), new BigDecimal("100"), "7bananas1manzana");
		savingsRepository.save(saving2);

		Savings saving3 = new Savings(new BigDecimal("1870.0"), user3, null, new BigDecimal("0.8"), new BigDecimal("350"), "7bananas1manzana");
		savingsRepository.save(saving3);

		Savings saving4 = new Savings(new BigDecimal("8800.0"), user5, null, "7bananas1manzana");
		savingsRepository.save(saving4);


		StudentChecking student1 = new StudentChecking(new BigDecimal("97.0"), user4, user1, "7bananas1manzana");
		studentCheckingRepository.save(student1);

		StudentChecking student2 = new StudentChecking(new BigDecimal("9826.0"), user1, null, "7bananas1manzana");
		studentCheckingRepository.save(student2);

		StudentChecking student3 = new StudentChecking(new BigDecimal("762.0"), user2, null, "7bananas1manzana");
		studentCheckingRepository.save(student3);


		Admin admin1 = new Admin("admin1111", passwordEncoder().encode("contrasena88"), "Admin1");
		adminRepository.save(admin1);


		Role adminRole = new Role("ADMIN", admin1);
		roleRepository.save(adminRole);
		adminRepository.save(admin1);


		Role holderRole = new Role("ACCOUNT HOLDER", user1);
		roleRepository.save(holderRole);
		accountHolderRepository.save(user1);

		Role holderRole2 = new Role("ACCOUNT HOLDER", user2);
		roleRepository.save(holderRole2);
		accountHolderRepository.save(user2);

		Role holderRole3 = new Role("ACCOUNT HOLDER", user3);
		roleRepository.save(holderRole3);
		accountHolderRepository.save(user3);

		Role holderRole4 = new Role("ACCOUNT HOLDER", user4);
		roleRepository.save(holderRole4);
		accountHolderRepository.save(user4);

		ThirdParty thirdParty1 = new ThirdParty("thirdpartymember", passwordEncoder().encode("password"), "name", "hashed");
		thirdPartyRepository.save(thirdParty1);

		Role thirdPartyRole = new Role("THIRD PARTY", thirdParty1);
		roleRepository.save(thirdPartyRole);
		thirdPartyRepository.save(thirdParty1);

	}
}
