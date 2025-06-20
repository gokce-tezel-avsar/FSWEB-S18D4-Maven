package com.workintech.s18d1.dao;

import com.workintech.s18d1.entity.BreadType;
import com.workintech.s18d1.entity.Burger;
import com.workintech.s18d1.exceptions.BurgerException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
@Slf4j
@Repository
public class BurgerDaoImpl implements BurgerDao {
    private final EntityManager entityManager;

    @Autowired
    public BurgerDaoImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Transactional//hata oluşursa veritabanını işlemden önceki haline getirir.Role back
    @Override
    public Burger save(Burger burger) {
        entityManager.persist(burger);
        log.info("save ended");
        return burger;
    }

    @Override
    public Burger findById(Long id) {
       Burger burger = entityManager.find(Burger.class,id);
       if(burger == null){
           throw new BurgerException("Burger not found: "+ id, HttpStatus.NOT_FOUND);
       }
        return burger;
    }

    @Override
    public List<Burger> findAll() {
       TypedQuery<Burger> foundAll = entityManager.createQuery("SELECT b FROM Burger b", Burger.class);
        return foundAll.getResultList();
    }

    @Transactional
    @Override
    public Burger update(Burger burger) {
        return entityManager.merge(burger);
    }

    @Override
    public Burger remove(Long id) {
     Burger foundedBurger = findById(id);
     entityManager.remove(foundedBurger);
     return foundedBurger;
    }

    @Override
    public List<Burger> findByPrice(Integer price) {
        TypedQuery<Burger> query = entityManager.createQuery("SELECT b FROM Burger b WHERE b.price > :price ORDER BY b.price DESC", Burger.class);
        query.setParameter("price", price);
        return query.getResultList();

    }
    @Override
    public List<Burger> findByBreadType(BreadType breadType) {
        TypedQuery<Burger> query = entityManager.createQuery("SELECT b FROM Burger b WHERE b.breadType = :breadType ORDER BY b.breadType DESC", Burger.class);
        query.setParameter("breadType", breadType);
        return query.getResultList();
    }

    @Override
    public List<Burger> findByContent(String content) {
        TypedQuery<Burger> query = entityManager.createQuery("SELECT b FROM Burger b WHERE b.contents LIKE CONCAT('%',:content,'%') ORDER BY b.name", Burger.class);
        query.setParameter("content", content);
        return query.getResultList();
    }

}




















