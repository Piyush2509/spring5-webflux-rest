package guru.springframework.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
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
		given(vendorRepository.findAll())
				.willReturn(Flux.just(Vendor.builder().firstName("Fred").lastName("Flintstone").build(),
						Vendor.builder().firstName("Barney").lastName("Rubble").build()));

		webTestClient.get().uri("/api/v1/vendors").exchange().expectBodyList(Vendor.class).hasSize(2);
	}

	@Test
	public void testGetById() throws Exception {
		given(vendorRepository.findById("someid"))
				.willReturn(Mono.just(Vendor.builder().firstName("Jimmy").lastName("Johns").build()));

		webTestClient.get().uri("/api/v1/vendors/someid").exchange().expectBodyList(Vendor.class);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreate() throws Exception {
		given(vendorRepository.saveAll(any(Publisher.class))).willReturn(Flux.just(Vendor.builder().build()));

		Mono<Vendor> vendorToSaveMono = Mono
				.just(Vendor.builder().firstName("First Name").lastName("Last Name").build());

		webTestClient.post().uri("/api/v1/vendors").body(vendorToSaveMono, Vendor.class).exchange().expectStatus()
				.isCreated();
	}

	@Test
	public void testUpdate() throws Exception {
		given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

		Mono<Vendor> vendorToUpdateMono = Mono
				.just(Vendor.builder().firstName("First Name").lastName("Last Name").build());

		webTestClient.put().uri("/api/v1/vendors/someid").body(vendorToUpdateMono, Vendor.class).exchange()
				.expectStatus().isOk();
	}

	@Test
	public void testPatch() throws Exception {
		given(vendorRepository.findById(anyString()))
				.willReturn(Mono.just(Vendor.builder().firstName("Jimmy").build()));

		given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

		Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("Jim").build());

		webTestClient.patch().uri("/api/v1/vendors/someid").body(vendorToUpdateMono, Vendor.class).exchange()
				.expectStatus().isOk();

		verify(vendorRepository).save(any());
	}

	@Test
	public void testPatchNoChanges() throws Exception {
		given(vendorRepository.findById(anyString()))
				.willReturn(Mono.just(Vendor.builder().firstName("Jimmy").build()));

		given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

		Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("Jimmy").build());

		webTestClient.patch().uri("/api/v1/vendors/someid").body(vendorToUpdateMono, Vendor.class).exchange()
				.expectStatus().isOk();

		verify(vendorRepository, never()).save(any());
	}

}
