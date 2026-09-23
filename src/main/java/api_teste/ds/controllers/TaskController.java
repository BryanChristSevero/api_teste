package api_teste.ds.controllers;

//importa a classe URI para construir e manipular HTTP de novos recursos.
import java.net.URI;
//importa a interface List para manipular onde a classe Controller está localizada.
import java.util.List;

import org.apache.catalina.connector.Response;
//injeção automatica do Spring.
import org.springframework.beans.factory.annotation.Autowired;
//importa a classe para montar a resposta HTTP completa( status, headers, corpo).
import org.springframework.http.ResponseEntity;
//importa anotação para habilitar suporte a validação no controller.
import org.springframework.validation.annotation.Validated;
//mapeia requisições do tipo DELETE.
import org.springframework.web.bind.annotation.DeleteMapping;
//mapeia requisições do tipo GET.
import org.springframework.web.bind.annotation.GetMapping;
//mapeia variaveis passadas diretamente via caminho  da URL.
import org.springframework.web.bind.annotation.PathVariable;
//mapeia requisições do tipo POST.
import org.springframework.web.bind.annotation.PostMapping;
//mapeia requisições do tipo PUT.
import org.springframework.web.bind.annotation.PutMapping;
//converte objetos JSON em objetos JAVA.
import org.springframework.web.bind.annotation.RequestBody;
//importa anotação para definir o caminho/rota bas do controlador.
import org.springframework.web.bind.annotation.RequestMapping;
//importa anotação que define esta classe como um controller REST.
import org.springframework.web.bind.annotation.RestController;
//importa utilitario para gerar a URI da requisição atual dinamicamente.
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//importa a anotação para acionar a validação do corpo de requisição.
import jakarta.validation.Valid;
//importa a entidade Task do pacote de modelos do projeto.
import api_teste.ds.models.Task;
//importa a caçasse de serviço TaskService do projeto.
import api_teste.ds.services.TaskService;

//define a classe como um controlador REST que retorna respostas em JSON.
@RestController
//define que todas as rotas desta classe terão como prefixo o caminho "/task".
@RequestMapping ("/task")
//ativa a verificação de validações nos parametros recebidos no controller.
@Validated

public class TaskController {

    @Autowired
    private TaskService taskService;

    //mapeia requisições HTTP GET na rota "/task/{id}".
    @GetMapping ("/{id}")
    //busca tarefa especifica pelo seu ID.
    public ResponseEntity<Task> findById(@PathVariable Long id){
        Task obj = this.taskService.findById(id); //chama a camada de serviço para buscar a tarefa pelo seu ID.
        return ResponseEntity.ok().body(obj); //retorna HTTP 200(ok).
    }

    @GetMapping ("/user/{userid}")
    public ResponseEntity<List<Task>> findAllByUserId(@PathVariable Long userId){
        List<Task> objs = this.taskService.findAllByUserId(userId);
        return ResponseEntity.ok().body(objs);
    }

    @GetMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj){
        this.taskService.create(obj);
        URI url = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(url).build();
    }

    @PostMapping ("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody Task obj, @PathVariable Long id){
        obj.setId(id);
        this.taskService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
 
}