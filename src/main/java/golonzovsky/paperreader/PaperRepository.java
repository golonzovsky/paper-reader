package golonzovsky.paperreader;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperRepository extends PagingAndSortingRepository<Paper, Long> {

}
