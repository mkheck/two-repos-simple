package com.thehecklers.tworepos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.Id;

@SpringBootApplication
public class TwoReposApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwoReposApplication.class, args);
    }

}

@Component
class CheckItOut {
    private final CoffeeRepositoryJPA jpaRepo;
    private final CoffeeRepositoryMongo monRepo;

    @Value("${database:JPA}")
    private String database;

    public CheckItOut(CoffeeRepositoryJPA jpaRepo, CoffeeRepositoryMongo monRepo) {
        this.jpaRepo = jpaRepo;
        this.monRepo = monRepo;
    }

    @PostConstruct
    private void saveSomething() {
        if (database.equals("JPA")) {
            jpaRepo.save(new CoffeeL(1L, "H2? What kind of coffee is that?!?"));
            System.out.println("    >>> JPA <<<    ");
            jpaRepo.findAll().forEach(System.out::println);
        } else {
            monRepo.save(new CoffeeS("1", "Mongo Coffee good!"));
            System.out.println("    >>> MONGO <<<    ");
            monRepo.findAll().forEach(System.out::println);
        }
    }

}

interface CoffeeRepositoryJPA extends CrudRepository<CoffeeL, Long> {
}

interface CoffeeRepositoryMongo extends CrudRepository<CoffeeS, String> {
}

@Entity
class CoffeeL {
    @Id
    private Long id;
    private String name;

    public CoffeeL() {
    }

    public CoffeeL(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CoffeeL{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

@Document
class CoffeeS {
    @org.springframework.data.annotation.Id
    private String id;
    private String name;

    public CoffeeS(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CoffeeS{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}