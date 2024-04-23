package Entity;

import java.util.Collections;
import java.util.List;

public class order {

    private int id;
    private List<product> products; // List of products associated with the order

    public order(int id, List<product> products) {
        this.id = id;
        this.products = products;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<product> getProducts() {
        return products;
    }

    public void setProducts(List<product> products) {
        this.products = products;
    }

    public order() {

    }
}
