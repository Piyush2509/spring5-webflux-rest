package guru.springframework.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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
		BDDMockito.given(categoryRepository.findAll()).willReturn(Flux
				.just(Category.builder().description("Cat1").build(), Category.builder().description("Cat2").build()));

		webTestClient.get().uri("/api/v1/categories").exchange().expectBodyList(Category.class).hasSize(2);
	}

	@Test
	public void testGetById() throws Exception {
		BDDMockito.given(categoryRepository.findById("someid"))
				.willReturn(Mono.just(Category.builder().description("Cat1").build()));

		webTestClient.get().uri("/api/v1/categories/someid").exchange().expectBodyList(Category.class);
	}

}
