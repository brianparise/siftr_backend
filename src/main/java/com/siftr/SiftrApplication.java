package com.siftr;

import com.siftr.Repository.*;
import com.siftr.Repository.*;
import com.siftr.SftpClient.SftpClient;
import com.siftr.entity.*;
import com.siftr.entity.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {" com.siftr.entity"})  // scan JPA entities
public class SiftrApplication {

	static SftpClient sftpClient;
	public static void main(String[] args) {
		//SpringApplication.run(SchedulingApplication.class, args);

		ApplicationContext context = SpringApplication.run(SiftrApplication.class, args);

		CaseRepository caseRepository = context.getBean(CaseRepository.class);
		OneCaseRepository oneCaseRepository = context.getBean(OneCaseRepository.class);
		EpicCaseRepository epicCaseRepository = context.getBean(EpicCaseRepository.class);
		AppCaseStateRepository appCaseStateRepository = context.getBean(AppCaseStateRepository.class);
		AppUserRepository appUserRepository = context.getBean(AppUserRepository.class);
		RoleRepository roleRepository = context.getBean(RoleRepository.class);

		BaseCase baseCase1 = new BaseCase("2024-03-02 09:57:49");
		BaseCase baseCase2 = new BaseCase("2024-03-02 09:57:49");
		caseRepository.saveAll(Arrays.asList(baseCase1, baseCase2));

		Role role1 = new Role("Unit Clerk");
		Role role2 = new Role("SCC");
		Role role3 = new Role("OR Supervisor");
		Role role4 = new Role("ARN");
		roleRepository.saveAll(Arrays.asList(role1, role2, role3, role4));


		OneCase oneCase1 = new OneCase("2024-03-02 09:56:49", "123456", "Abhinay", "2024-03-06 14:06:49", "456787", "Dr.One","4 hours","POH","205/26/2024", "Depuy", baseCase1);
		OneCase oneCase2 = new OneCase("2024-03-01 08:56:49", "587452", "Test", "2024-02-06 14:06:49", "467853", "Dr.Two", "2 hours","","","Screws", baseCase2);
		oneCaseRepository.saveAll(Arrays.asList(oneCase1, oneCase2));

		EpicCase epicCase1 = new EpicCase("2024-03-02 10:56:49", "431567", "Medicare", "Medicare", baseCase1);
		EpicCase epicCase2 = new EpicCase("2024-03-01 14:36:19", "674574", "Signature Care", "Signature", baseCase2);
		epicCaseRepository.saveAll(Arrays.asList(epicCase1, epicCase2));


		AppCaseState appCaseState1 = new AppCaseState();
		AppCaseState appCaseState2 = new AppCaseState();
		appCaseState1.setBaseCase(baseCase1);
		appCaseState2.setBaseCase(baseCase2);

		AppUser appUser1 = new AppUser();
		appUser1.setEmail("test@abc.com");
		appUser1.setName("test");

		appUser1.getAppCaseStates().add(appCaseState1);
		appUser1.getAppCaseStates().add(appCaseState2);
		appUser1.setRole(role1);
		appCaseState1.getAppUsers().add(appUser1);
		appUserRepository.save(appUser1);

		AppUser appUser2 = new AppUser();
		appUser2.setEmail("brian.parise@parkview.com");
		appUser2.setName("Brian Parise");
//		appUser2.getAppCaseStates().add(appCaseState1);
//		appUser2.getAppCaseStates().add(appCaseState2);
		appUser2.setRole(role1);
		//appCaseState1.getAppUsers().add(appUser2);
		appUserRepository.save(appUser2);

		// sftp client
		sftpClient = new SftpClient();
	}


}
