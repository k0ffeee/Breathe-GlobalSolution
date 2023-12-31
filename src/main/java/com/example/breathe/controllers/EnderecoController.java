package com.example.breathe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.breathe.exceptions.RestNotFoundException;
import com.example.breathe.models.Endereco;
import com.example.breathe.models.Estado;
import com.example.breathe.repository.EnderecoRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Endereco")
@RequestMapping("/api/endereco")
public class EnderecoController {

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;
    
    @GetMapping
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) String busca, @PageableDefault(size = 10) Pageable pageable){
        Page<Endereco> endereco = enderecoRepository.findAll(pageable);

        return assembler.toModel(endereco.map(Endereco::toEntityModel));
    }

    @PostMapping
    public ResponseEntity<Endereco> create(@RequestBody @Valid Endereco endereco){
        enderecoRepository.save(endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
    }

    @GetMapping("{id}")
    public EntityModel<Endereco> show(@PathVariable long id) {
        var endereco = enderecoRepository.findById(id).orElseThrow(() -> new RestNotFoundException("doenca nao encontrada"));
        return endereco.toEntityModel();
    }

    @PutMapping("{id}")
    public EntityModel<Endereco> update(@PathVariable Long id, @RequestBody @Valid Endereco endereco){

        enderecoRepository.findById(id).orElseThrow(() -> new RestNotFoundException("Erro ao apagar, doenca não encontrada"));

        endereco.setId(id);
        enderecoRepository.save(endereco);

        return endereco.toEntityModel();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Endereco> destroy(@PathVariable Long id){

        var endereco = enderecoRepository.findById(id).orElseThrow(()-> new RestNotFoundException("Erro ao apagar, doenca não encontrada"));
        
        enderecoRepository.delete(endereco);
        return ResponseEntity.noContent().build();

    }

}
