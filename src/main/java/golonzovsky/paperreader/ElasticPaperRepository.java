package golonzovsky.paperreader;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticPaperRepository extends ElasticsearchRepository<Paper, String> {

}
