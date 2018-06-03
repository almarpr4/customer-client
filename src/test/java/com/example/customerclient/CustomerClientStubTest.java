package com.example.customerclient;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest(classes = CustomerClientApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureStubRunner(
        ids = "com.example:customer-service:+:stubs:8080",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class CustomerClientStubTest {

    @Autowired
    private CustomerClient client;


    @Test
    public void customerShouldReturnAllCustomers(){

        Collection<Customer> customers = client.getCustomers();
        BDDAssertions.then(customers.size()).isEqualTo(2);


    }

    private String asJson(Resource resource){
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()))){
            Stream<String> lines = bufferedReader.lines();
            return lines.collect(Collectors.joining());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Test
    public void customerByIdShouldReturnACustomer(){

        Customer customer = client.getCustomerById(1L);

        BDDAssertions.then(customer.getFirstName()).isEqualToIgnoringCase("first");
        BDDAssertions.then(customer.getLastName()).isEqualToIgnoringCase("last");
        BDDAssertions.then(customer.getEmail()).isEqualToIgnoringCase("email");

    }
}
