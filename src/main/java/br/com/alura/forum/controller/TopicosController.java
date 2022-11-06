package br.com.alura.forum.controller;

import br.com.alura.forum.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.form.AtualizacaoTopicoForm;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoReposity;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoReposity topicoReposity;
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
                                 @PageableDefault(sort = "id",direction = Sort.Direction.ASC,page = 0,size = 10)
                                 Pageable paginacao){



        if (nomeCurso == null){
            Page<Topico> topicos = topicoReposity.findAll(paginacao);
            return TopicoDto.converter(topicos);
        }else {
            Page<Topico> topicos = topicoReposity.findByCursoNome(nomeCurso,paginacao);
            return  TopicoDto.converter(topicos);
            
        }


    }
    @PostMapping
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder){
        Topico topico = form.converter(cursoRepository);
        topicoReposity.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));

    }
    @GetMapping("/{id}")
    public  ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id){
       Optional <Topico> topico = topicoReposity.findById(id);
       if (topico.isPresent()){
           return  ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
       }else{
           return ResponseEntity.notFound().build();
       }

    }
    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id,@RequestBody @Valid AtualizacaoTopicoForm form){
        Optional <Topico> optional = topicoReposity.findById(id);
        if (optional.isPresent()){
            Topico topico = form.atualizar(id, topicoReposity);
            return ResponseEntity.ok(new TopicoDto(topico));
        }else{
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<?> remover(@PathVariable Long id){
        Optional <Topico> optional = topicoReposity.findById(id);
        if (optional.isPresent()){
            topicoReposity.deleteById(id);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }



    }


}
