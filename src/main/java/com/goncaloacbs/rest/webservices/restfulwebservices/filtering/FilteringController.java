package com.goncaloacbs.rest.webservices.restfulwebservices.filtering;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class FilteringController {

    private MappingJacksonValue applyFilters(MappingJacksonValue mapping, String jsonFilterName, PropertyFilter filter) {
        // Add our filter to the provider
        FilterProvider filters = new SimpleFilterProvider().addFilter(jsonFilterName, filter);

        // Apply our filter provider
        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping("/filtering")
    public MappingJacksonValue retrieveSomeBean() {
        SomeBean someBean = new SomeBean("val1", "val2", "val3");

        // Define our filter
        PropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field2");

        return this.applyFilters(new MappingJacksonValue(someBean), "SomeBeanFilter", filter);
    }


    //field2 and 3
    @GetMapping("/filtering-list")
    public MappingJacksonValue retrieveListOfSomeBeans() {
        List<SomeBean> beanList = Arrays.asList(new SomeBean("val12", "val23", "val32"), new SomeBean("val1", "val2", "val3"));

        // Define our filter
        PropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field2", "field3");

        return this.applyFilters(new MappingJacksonValue(beanList), "SomeBeanFilter", filter);
    }


}
