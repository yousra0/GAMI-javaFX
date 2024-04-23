package Service;

import Entity.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class product_s implements Services<product> {

    Connection cnx;

    public product_s(Connection cnx) {
        this.cnx = cnx;
    }

    @Override
    public void add(product p) throws SQLException {
        String qry = "INSERT INTO `product`(`reference`, `name`, `description`, `price`, `image`, `rating`) VALUES (?,?,?,?,?,?)";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, p.getReference());
            pstm.setString(2, p.getName());
            pstm.setString(3, p.getDescription());
            pstm.setInt(4, p.getPrice());
            pstm.setString(5, p.getImage());
            pstm.setInt(6, p.getRating());
            pstm.executeUpdate();
        }
    }

    @Override
    public List<product> show() throws SQLException {
        List<product> productList = new ArrayList<>();
        String qry = "SELECT * FROM `product`";
        try (PreparedStatement pstm = cnx.prepareStatement(qry); ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                product p = new product();
                p.setId(rs.getInt("id"));
                p.setReference(rs.getString("reference"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getInt("price"));
                p.setImage(rs.getString("image"));
                p.setRating(rs.getInt("rating"));
                productList.add(p);
            }
        }
        return productList;
    }

    @Override
    public void delete(int id) throws SQLException {
        String qry = "DELETE FROM `product` WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        }
    }

    @Override
    public void edit(product p) throws SQLException {
        String qry = "UPDATE `product` SET `reference`=?, `name`=?, `description`=?, `price`=?, `image`=?, `rating`=? WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, p.getReference());
            pstm.setString(2, p.getName());
            pstm.setString(3, p.getDescription());
            pstm.setInt(4, p.getPrice());
            pstm.setString(5, p.getImage());
            pstm.setInt(6, p.getRating());
            pstm.setInt(7, p.getId());
            pstm.executeUpdate();
        }
    }

    public List<product> getProductsByOrderId(int orderId) throws SQLException {
        List<product> productList = new ArrayList<>();
        String qry = "SELECT p.* FROM `product` p " +
                "JOIN order_product op ON p.id = op.product_id " +
                "WHERE op.order_id = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, orderId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    product p = new product();
                    p.setId(rs.getInt("id"));
                    p.setReference(rs.getString("reference"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getInt("price"));
                    p.setImage(rs.getString("image"));
                    p.setRating(rs.getInt("rating"));
                    productList.add(p);
                }
            }
        }
        return productList;
    }

}
