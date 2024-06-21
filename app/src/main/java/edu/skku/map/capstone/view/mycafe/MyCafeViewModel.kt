package edu.skku.map.capstone.view.mycafe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.skku.map.capstone.manager.CafeManager
import edu.skku.map.capstone.models.cafe.Cafe
//import edu.skku.map.capstone.models.favorite.Favorite

class MyCafeViewModel() {
    private val _favoriteCafeList: MutableLiveData<List<Cafe>> = MutableLiveData<List<Cafe>>() // 즐겨찾기한 카페
    val favoriteCafeList:LiveData<List<Cafe>> get() = _favoriteCafeList

    fun updateFavoriteCafeList() {
        _favoriteCafeList.value = CafeManager.getInstance().getFavoriteCafeList()
    }
}