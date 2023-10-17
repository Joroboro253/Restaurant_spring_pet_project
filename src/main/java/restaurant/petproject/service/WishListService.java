package restaurant.petproject.service;

import restaurant.petproject.entity.WishList;

public interface WishListService {
    WishList addToWishListFirstTime(Long id, String SessionToken);
    WishList addToExistingWishList(Long id, String SessionToken);
    WishList getWishListBySessionToken(String sessionToken);
    WishList removeItemWishList(Long id, String SessionToken);
    void clearWishList(String sessionToken);
}
