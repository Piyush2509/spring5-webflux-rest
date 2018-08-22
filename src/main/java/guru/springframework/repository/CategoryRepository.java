package guru.springframework.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import guru.springframework.domain.Category;

/**
 * Created by piyush.b.kumar on Aug 22, 2018
 */
public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {

}
