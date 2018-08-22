package guru.springframework.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;

import guru.springframework.domain.Vendor;
import guru.springframework.repository.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by piyush.b.kumar on Aug 22, 2018
 */
public class VendorControllerTest {

	WebTestClient webTestClient;
	VendorRepository vendorRepository;
	VendorController vendorController;

	@Before
	public void setUp() throws Exception {
		vendorRepository = Mockito.mock(VendorRepository.class);
		vendorController = new VendorController(vendorRepository);
		webTestClient = WebTestClient.bindToController(vendorController).build();

	}

	@Test
	public void testList() throws Exception {
		BDDMockito.given(vendorRepository.findAll())
				.willReturn(Flux.just(Vendor.builder().firstName("Fred").lastName("Flintstone").build(),
						Vendor.builder().firstName("Barney").lastName("Rubble").build()));

		webTestClient.get().uri("/api/v1/vendors").exchange().expectBodyList(Vendor.class).hasSize(2);
	}

	@Test
	public void testGetById() throws Exception {
		BDDMockito.given(vendorRepository.findById("someid"))
				.willReturn(Mono.just(Vendor.builder().firstName("Jimmy").lastName("Johns").build()));

		webTestClient.get().uri("/api/v1/vendors/someid").exchange().expectBodyList(Vendor.class);
	}

}
