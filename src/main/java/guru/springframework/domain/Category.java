package guru.springframework.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * Created by piyush.b.kumar on Aug 22, 2018
 */
@Data
@Document
public class Category {
	
	@Id
	private String id;
	private String description;

}
