package restaurant.petproject.service;

import javax.naming.directory.SearchResult;
import java.util.List;

public interface SearchService {
    public List<SearchResult> search(String query);
}
