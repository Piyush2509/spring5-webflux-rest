package guru.springframework.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import guru.springframework.domain.Category;
import guru.springframework.domain.Vendor;
import guru.springframework.repository.CategoryRepository;
import guru.springframework.repository.VendorRepository;

/**
 * Created by piyush.b.kumar on Aug 22, 2018
 */
@Component
public class Bootstrap implements CommandLineRunner {

	private final CategoryRepository categoryRepository;
	private final VendorRepository vendorRepository;

	public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
		this.categoryRepository = categoryRepository;
		this.vendorRepository = vendorRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		if (categoryRepository.count().block() == 0) {
			// load data
			System.out.println("### LOADING DATA ON BOOTSTRAP ###");

			categoryRepository.save(Category.builder().description("Fruits").build()).block();
			categoryRepository.save(Category.builder().description("Nuts").build()).block();
			categoryRepository.save(Category.builder().description("Breads").build()).block();
			categoryRepository.save(Category.builder().description("Meats").build()).block();
			categoryRepository.save(Category.builder().description("Eggs").build()).block();

			System.out.println("Loaded Catrgories: " + categoryRepository.count().block());

			vendorRepository.save(Vendor.builder().firstName("Joe").lastName("Buck").build()).block();
			vendorRepository.save(Vendor.builder().firstName("Micheal").lastName("Weston").build()).block();
			vendorRepository.save(Vendor.builder().firstName("Jessie").lastName("Waters").build()).block();
			vendorRepository.save(Vendor.builder().firstName("Bill").lastName("Nershi").build()).block();
			vendorRepository.save(Vendor.builder().firstName("Jimmy").lastName("Buffet").build()).block();

			System.out.println("Loaded Vendors: " + vendorRepository.count().block());
		}
	}

}
