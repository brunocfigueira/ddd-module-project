package hexagonal.modular.domain.sales.persitence.postgre.repositories;

import hexagonal.modular.domain.sales.persitence.postgre.entities.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
}
