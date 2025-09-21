package com.godan.chunk.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;
import java.util.List;

public class MyItemReader implements ItemReader<String> {

    public Iterator<String> productListIterator;

    public MyItemReader(List<String> productList){
        this.productListIterator = productList.iterator();
    }
    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return this.productListIterator.hasNext() ? productListIterator.next() : null;
    }
}
