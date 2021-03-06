package com.example.customerclient;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import wiremock.org.apache.http.HttpHeaders;
import wiremock.org.eclipse.jetty.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest(classes = CustomerClientApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 8080)
public class CustomerClientWiremockTest {

    private Resource customers = new ClassPathResource("customers.json");
    private Resource customerById = new ClassPathResource("customer-by-id.json");

    @Autowired
    private CustomerClient client;


    @Test
    public void customerShouldReturnAllCustomers(){
        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/customers"))
                .willReturn(
                        WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withStatus(HttpStatus.OK_200)
                        .withBody(asJson(customers))
                )
        );

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
        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/customers/1"))
                .willReturn(
                        WireMock.aResponse()
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .withStatus(HttpStatus.OK_200)
                                .withBody(asJson(customerById))
                )
        );

        Customer customer = client.getCustomerById(1L);

        BDDAssertions.then(customer.getFirstName()).isEqualToIgnoringCase("first");
        BDDAssertions.then(customer.getLastName()).isEqualToIgnoringCase("last");
        BDDAssertions.then(customer.getEmail()).isEqualToIgnoringCase("email");

    }
}
