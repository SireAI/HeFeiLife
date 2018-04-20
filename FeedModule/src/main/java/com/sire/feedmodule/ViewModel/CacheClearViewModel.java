package com.sire.feedmodule.ViewModel;

import android.arch.lifecycle.ViewModel;

import com.sire.corelibrary.Utils.CommonUtils;
import com.sire.feedmodule.Repository.CacheClearRepository;
import com.sire.feedmodule.Utils.DateKeyUtils;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/07
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class CacheClearViewModel extends ViewModel {
    private final CacheClearRepository cacheClearRepository;

    @Inject
    public CacheClearViewModel(CacheClearRepository cacheClearRepository) {
        this.cacheClearRepository = cacheClearRepository;
    }

    /**
     * 删除七天之前的feed缓存
     */
    public void clearCacheBy(){
        long timeLine = DateKeyUtils.generateOffsetdayKey(new Date(), 1);
        cacheClearRepository.clearFeedsCacheBy(new Date(timeLine));
    }
}
