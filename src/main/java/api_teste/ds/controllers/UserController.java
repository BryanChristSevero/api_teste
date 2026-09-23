package api_teste.ds.controllers;

import api_teste.ds.DsApplication;
//importa a classe URI para construir e manipular HTTP de novos recursos.
import java.net.URI;
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

//
import api_teste.ds.models.User;
//
import api_teste.ds.models.User.CreateUser;
//
import api_teste.ds.models.User.UpdateUser;
//
import api_teste.ds.services.UserService;
import org.springframework.web.bind.annotation.RequestParam;


//define a classe como um controlador REST que retorna respostas em JSON.
@RestController
//define que todas as rotas desta classe terão como prefixo o caminho "/user".
@RequestMapping ("/user")
//ativa a verificação de validações nos parametros recebidos no controller.
@Validated

public class UserController {

    private final DsApplication dsApplication;
    @Autowired 
    private UserService userService;

    UserController(DsApplication dsApplication) {
        this.dsApplication = dsApplication;
    }

    //mapeia requisições HTTP GET na rota "/user/{id}"
    @GetMapping ("/{id}")
    //método para buscar usuário por ID capturado da URL. 
    public ResponseEntity<User> findById(@PathVariable Long Id){
        User obj=this.userService.findById(Id); //invoca a busca do usuário através do ID recebido.
        return ResponseEntity.ok().body(obj); //retorna código HTTP 200(ok) com o objeto User no corpo da resposta.
    } //fim do método findById.

    //mapeia requisições HTTP POST na rota base "/user" (criação de novo usuário).
    @PostMapping
    //valida regra de CreateUser e desserializa o corpo JSON.
    public ResponseEntity<Void> create(@Validated (CreateUser.class) @RequestBody User obj){
        this.userService.create(obj); //chama a camada de serviço para persistir o novo usuário no banco de dados.
        URI url = ServletUriComponentsBuilder.fromCurrentRequest() //obtém a rota da requisição atual.
            .path("/{id}").buildAndExpand(obj.getId()).toUri(); //adiciona o ID do usuário gerado no final do caminho da URI.
            return ResponseEntity.created(url).build(); //retorna código HTTP 201(created) contendo a URL no cabeçalho location.
    }

    //mapeia requisições HTTP PUT na rota base "/user/{id}" (atualização do usuário).
    @PutMapping("/{id}")
    //aplica a regra de UpdateUser e recebe ID e JSON.
    public ResponseEntity<Void> update(@Validated(UpdateUser.class)@RequestBody  User obj, @PathVariable Long id){
        obj.setId(id); //garante que o ID do objeto a ser atualizado corresponde ao ID informado no parametro da URL.
        this.userService.update(obj); //executa a atualização da senha do usuário no banco de dados.
        return ResponseEntity.noContent().build(); //retorna código HTTP 204(No Content) indicando sucesso sem corpo de resposta.
    }

    //mapeia requisições HTTP DELETE na rota base "/user/{id}" (exclusão de usuário).
    @DeleteMapping ("/{id}")
    //captura o ID da URL a ser deletada.
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.userService.delete(id); //invoca o método de deleção do serviço.
        return ResponseEntity.noContent().build(); //retorna código HTTP 204 (No Content) confirmando a exclusão.
    } 
}