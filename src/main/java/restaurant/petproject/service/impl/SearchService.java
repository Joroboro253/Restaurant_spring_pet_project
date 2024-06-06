package restaurant.petproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.petproject.entity.SearchResult;
import restaurant.petproject.repository.SearchRepository;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private SearchRepository searchRepository;

    public List<SearchResult> search(String query) {
        return searchRepository.findByQuery(query);
    }
}
