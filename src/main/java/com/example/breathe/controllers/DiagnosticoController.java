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
import com.example.breathe.models.Diagnostico;
import com.example.breathe.repository.DiagnosticoRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Diagnosticos")
@RequestMapping("/api/diagnosticos")
public class DiagnosticoController {

    
    @Autowired
    DiagnosticoRepository diagnosticoRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    @GetMapping
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) String busca, @PageableDefault(size = 10) Pageable pageable){
        Page<Diagnostico> diagnostico = diagnosticoRepository.findAll(pageable);

        return assembler.toModel(diagnostico.map(Diagnostico::toEntityModel));
    }

    @PostMapping
    public ResponseEntity<Diagnostico> create(@RequestBody @Valid Diagnostico diagnostico){
        diagnosticoRepository.save(diagnostico);
        return ResponseEntity.status(HttpStatus.CREATED).body(diagnostico);
    }

    @GetMapping("{id}")
    public EntityModel<Diagnostico> show(@PathVariable long id) {
        var diagnostico = diagnosticoRepository.findById(id).orElseThrow(() -> new RestNotFoundException("diagnostico nao encontrada"));
        return diagnostico.toEntityModel();
    }

    @PutMapping("{id}")
    public EntityModel<Diagnostico> update(@PathVariable Long id, @RequestBody @Valid Diagnostico diagnostico){

        diagnosticoRepository.findById(id).orElseThrow(() -> new RestNotFoundException("Erro ao apagar, diagnostico não encontrada"));

        diagnostico.setId(id);
        diagnosticoRepository.save(diagnostico);

        return diagnostico.toEntityModel();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Diagnostico> destroy(@PathVariable Long id){

        var diagnostico = diagnosticoRepository.findById(id).orElseThrow(()-> new RestNotFoundException("Erro ao apagar, doenca não encontrada"));
        
        diagnosticoRepository.delete(diagnostico);
        return ResponseEntity.noContent().build();

    }

}
