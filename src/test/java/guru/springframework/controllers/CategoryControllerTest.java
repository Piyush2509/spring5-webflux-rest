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

import guru.springframework.domain.Category;
import guru.springframework.repository.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by piyush.b.kumar on Aug 22, 2018
 */
public class CategoryControllerTest {

	WebTestClient webTestClient;
	CategoryRepository categoryRepository;
	CategoryController categoryController;

	@Before
	public void setUp() throws Exception {
		categoryRepository = Mockito.mock(CategoryRepository.class);
		categoryController = new CategoryController(categoryRepository);
		webTestClient = WebTestClient.bindToController(categoryController).build();
	}

	@Test
	public void testList() throws Exception {
		given(categoryRepository.findAll()).willReturn(Flux.just(Category.builder().description("Cat1").build(),
				Category.builder().description("Cat2").build()));

		webTestClient.get().uri("/api/v1/categories").exchange().expectBodyList(Category.class).hasSize(2);
	}

	@Test
	public void testGetById() throws Exception {
		given(categoryRepository.findById("someid"))
				.willReturn(Mono.just(Category.builder().description("Cat1").build()));

		webTestClient.get().uri("/api/v1/categories/someid").exchange().expectBodyList(Category.class);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreate() throws Exception {
		given(categoryRepository.saveAll(any(Publisher.class)))
				.willReturn(Flux.just(Category.builder().description("descrp").build()));

		Mono<Category> catToSaveMono = Mono.just(Category.builder().description("Some Cat").build());

		webTestClient.post().uri("/api/v1/categories").body(catToSaveMono, Category.class).exchange().expectStatus()
				.isCreated();
	}

	@Test
	public void testUpdate() throws Exception {
		given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

		Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("Some Cat").build());

		webTestClient.put().uri("/api/v1/categories/someid").body(catToUpdateMono, Category.class).exchange()
				.expectStatus().isOk();
	}

	@Test
	public void testPatch() throws Exception {
		given(categoryRepository.findById(anyString())).willReturn(Mono.just(Category.builder().build()));

		given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

		Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("Some Cat").build());

		webTestClient.patch().uri("/api/v1/categories/someid").body(catToUpdateMono, Category.class).exchange()
				.expectStatus().isOk();

		verify(categoryRepository).save(any());
	}

	@Test
	public void testPatchNoChanges() throws Exception {
		given(categoryRepository.findById(anyString())).willReturn(Mono.just(Category.builder().build()));

		given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

		Mono<Category> catToUpdateMono = Mono.just(Category.builder().build());

		webTestClient.patch().uri("/api/v1/categories/someid").body(catToUpdateMono, Category.class).exchange()
				.expectStatus().isOk();

		verify(categoryRepository, never()).save(any());
	}

}
