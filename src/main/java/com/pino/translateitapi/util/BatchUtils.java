package com.pino.translateitapi.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class BatchUtils {

    private BatchUtils() {}

    public static <T> void subBatchIterator(
        Collection<T> source,
        int subBatchSize,
        Consumer<? super List<T>> action
    ) {
        if (source == null || source.isEmpty()) {
            return;
        }

        int idx = 1;
        List<T> subCollection = new ArrayList<>();

        for (T o : source) {
            subCollection.add(o);

            if (idx % subBatchSize == 0) {
                action.accept(subCollection);
                subCollection = new ArrayList<>();
            }

            idx++;
        }

        if (!subCollection.isEmpty()) {
            action.accept(subCollection);
        }
    }
}
