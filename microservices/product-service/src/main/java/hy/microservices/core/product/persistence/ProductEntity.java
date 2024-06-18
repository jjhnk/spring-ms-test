package hy.microservices.core.product.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "products")
@ToString
@Getter
@Setter
@NoArgsConstructor
public class ProductEntity {
  @Id
  private String id;

  @Version
  private Integer version;

  @Indexed(unique = true)
  private int productId;

  private String name;
  private int weight;

  public ProductEntity(int productId, String name, int weight) {
    this.productId = productId;
    this.name = name;
    this.weight = weight;
  }
}
