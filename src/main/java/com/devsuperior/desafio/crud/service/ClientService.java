package com.devsuperior.desafio.crud.service;

import com.devsuperior.desafio.crud.dto.ClientDTO;
import com.devsuperior.desafio.crud.model.Client;
import com.devsuperior.desafio.crud.repository.ClientRepository;
import com.devsuperior.desafio.crud.service.exceptions.DatabaseException;
import com.devsuperior.desafio.crud.service.exceptions.ResourceEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
        Page<Client> list = repository.findAll(pageRequest);
        return list.map(x -> new ClientDTO(x));
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        Optional<Client> obj = repository.findById(id);
        Client entity = obj.orElseThrow(() -> new ResourceEntityNotFoundException("Entity not found"));
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO insert(ClientDTO dto){
        Client entity = new Client();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO dto) {
        try{
            Client entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ClientDTO(entity);
        }
        catch(EntityNotFoundException e){
            throw new ResourceEntityNotFoundException("Id " + id + " not found");
        }
    }

    public void delete(Long id) {
        try{
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResourceEntityNotFoundException("Id " + id + " not found");
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }

    }

    public void copyDtoToEntity(ClientDTO dto, Client entity){
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }
}
