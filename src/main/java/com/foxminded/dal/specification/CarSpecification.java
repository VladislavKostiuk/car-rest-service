package com.foxminded.dal.specification;

import com.foxminded.model.Car;
import com.foxminded.model.Category;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecification {

    public static Specification<Car> containCategory(Category category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isMember(category, root.get("categories"));
    }

    public static Specification<Car> haveModel(String modelName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("model").get("name"), "%" + modelName + "%");
    }

    public static Specification<Car> haveManufacturer(String manufacturerName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("model").get("manufacturer").get("name"),
                "%" + manufacturerName + "%");
    }

    public static Specification<Car> haveYearGreaterThan(int minYear) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("year"), minYear);
    }

    public static Specification<Car> haveYearLessThan(int maxYear) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("year"), maxYear);
    }
}
