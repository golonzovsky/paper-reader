package golonzovsky.paperreader;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphPaperRepository extends PagingAndSortingRepository<Paper, Long> {

}
