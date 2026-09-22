package api_teste.ds.controllers;

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
import api_teste.ds.models.User.updateUser;
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

    @Autowired 
    private UserService userService;

    //mapeia requisições HTTP GET na rota "/user/{id}"
    @GetMapping ("/{id}")
    //método para buscar usuário por ID capturado da URL. 
    public ResponseEntity<User> findById(@PathVariable Long Id){
        User obj=this.userService.findById(Id); //invoca a busca do usuário através do ID recebido.
        return ResponseEntity.ok().body(obj) //retorna código HTTP 200(ok) com o objeto User no corpo da resposta.
    } //fim do método findById.

    @PostMapping
    public ResponseEntity<Void> create(@Validated (CreateUser.class) @RequestBody User obj){
        this.userService.create(obj);
        URI url = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(obj.getId()).toUri();
            return ResponseEntity.created(url).build();
    }
    
}