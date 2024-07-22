package gift.repository;

import gift.entity.Option;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findByProductId(Long productId);

    Optional<Option> findByIdAndProductId(Long optionId, Long productId);

}