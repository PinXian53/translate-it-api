package com.pino.translateitapi.util;

import com.pino.translateitapi.model.dto.PageInfo;
import com.pino.translateitapi.model.dto.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PageUtils {

    private PageUtils() {}

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public static Pageable toPageable(Integer pageNum, Integer pageSize) {
        return PageRequest.of(ObjectUtils.get(pageNum, DEFAULT_PAGE_NUM) - 1,
            ObjectUtils.get(pageSize, DEFAULT_PAGE_SIZE));
    }

    public static <T, R> Page<R> convertContent(Page<T> page, Function<? super T, R> func) {
        if (page == null) {
            return null;
        }
        List<R> resultList = new ArrayList<>();
        page.getContent().forEach(o ->
            resultList.add(func.apply(o))
        );
        return new PageImpl<>(resultList, page.getPageable(), page.getTotalElements());
    }

    public static <T> Pagination<T> toPagination(Page<T> page, Pageable pageable) {
        return toPagination(page, pageable.getPageSize());
    }

    public static <T> Pagination<T> toPagination(Page<T> page, int pageSize) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(page.getNumber() + 1);
        pageInfo.setPageSize(pageSize);
        pageInfo.setSize(page.getContent().size());
        pageInfo.setTotal(page.getTotalElements());
        pageInfo.setPages(page.getTotalPages());

        Pagination<T> pagination = new Pagination<>();
        pagination.setData(page.getContent());
        pagination.setPageInfo(pageInfo);
        return pagination;
    }
}
