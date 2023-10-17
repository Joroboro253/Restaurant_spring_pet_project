package restaurant.petproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.petproject.entity.WishList;
import restaurant.petproject.entity.WishListItem;
import restaurant.petproject.repository.WishListItemRepository;
import restaurant.petproject.repository.WishListRepository;
import restaurant.petproject.service.WishListService;

@Service
public class WishListServiceImpl implements WishListService {

    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private WishListItemRepository wishListItemRepository;
    @Autowired
    private DishServiceImpl dishService;

    @Override
    public WishList addToWishListFirstTime(Long id, String SessionToken) {
        return null;
    }

    @Override
    public WishList addToExistingWishList(Long id, String SessionToken) {
        return null;
    }

    @Override
    public WishList getWishListBySessionToken(String sessionToken) {
        return null;
    }

    @Override
    public WishList removeItemWishList(Long id, String SessionToken) {
        return null;
    }

    @Override
    public void clearWishList(String sessionToken) {

    }
}
