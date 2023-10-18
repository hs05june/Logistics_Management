package com.logistics.web.dao;

import com.logistics.web.models.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addOrder(Order order){
        String sql = "INSERT INTO Order(orderDate,quantity,productId,customerId) VALUES(?,?,?,?)";
        KeyHolder keyholder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, order.getOrderDate());
            ps.setInt(2, order.getQuantity());
            ps.setInt(3, order.getProductId());
            ps.setInt(4, order.getCustomerId());
        
            return ps;
        }, keyholder);

        return Objects.requireNonNull(keyholder.getKey()).intValue();
    }


    public Order getOrderById(int id){
         String sql = "SELECT * FROM Order WHERE orderId ="+id;
        List<Order> orders= jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
        if(orders.isEmpty()){
            return null;
        }
        return orders.get(0);
    }


    public List<Order> getAllOrders(){
        String sql = "SELECT * FROM Order";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }

    public int deleteOrderById(int id){
        String sql = "DELETE FROM Order WHERE orderId = ?";
        return jdbcTemplate.update(sql,id);
    }

    public Order updateOrderById(Order order, int id){
        String sql = "UPDATE Order SET orderDate=?, quantity=?, productId=?, customerId=? WHERE orderId = ?";
        jdbcTemplate.update(sql,order.getOrderDate(),order.getQuantity(),order.getProductId(),order.getCustomerId(),id);
        return order;
    }

}
