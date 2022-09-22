package com.devsuperior.desafio.crud.repository;

import com.devsuperior.desafio.crud.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
