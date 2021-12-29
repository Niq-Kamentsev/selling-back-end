package com.main.sellplatform;

import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.analyzer.DbConnector2;
import com.main.sellplatform.entitymanager.analyzer.TableSetter;
import com.main.sellplatform.entitymanager.testdao.LotDao2;

import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.persistence.entity.enums.Role;
import com.main.sellplatform.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SellplatformApplication  implements CommandLineRunner {

	@Autowired
	ApplicationContextProvider applicationContextProvider;


	public static void main(String[] args) {
		SpringApplication.run(SellplatformApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		EntityManager entityManager = applicationContextProvider.getApplicationContext().getBean(EntityManager.class);
//		User user = new User();
//		user.setLastName("test");
//		user.setFirstName("test");
//		user.setEmail("fdsghn@gmail.com");
//		user.setRole(Role.ADMIN);
//		Lot lot = new Lot();
//		lot.setOwner(user);
//		lot.setDescr("desc test");
//		lot.setName("name lot");
//		lot.setStartPrice(90.1);
//		lot.setEndPrice(80.1);
//		lot.setCategory("Toys");
//		lot.setLocation("location");
//		lot.setStartDate(new Date());
//		Date date = new Date();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		calendar.add(Calendar.DAY_OF_MONTH, 30);
//		System.out.println(calendar.getTime());
//		lot.setEndDate(calendar.getTime());
//		lot.setImgPath("https://project-sell-platfrom-nc.s3.eu-west-3.amazonaws.com/1640292553308-kreslo-flori-manchester-42-24382663.jpeg");
//		LotService bean1 = applicationContextProvider.getApplicationContext().getBean(LotService.class);
//		Lot merge = entityManager.merge(lot);
//		List<Lot> publishedLot = bean1.getPublishedLot();
//		publishedLot.forEach(System.out::println);
//


	}

}
