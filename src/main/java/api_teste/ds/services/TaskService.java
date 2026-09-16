package api_teste.ds.services;

import api_teste.ds.DsApplication;
//Importa lista da biblioteca padrão do Java para manipular coleções de objetos.
import java.util.List;
//Importa optional, usado para tratr valores que podem não estar presentes(evita NUllExpectionPointer).
import java.util.Optional;

//Importa a anotação do Spring para injeção automatica de dependencias.
import org.springframework.beans.factory.annotation.Autowired;
//Importa a anotação que define essa classe como um componente de serviço gerenciado pelo Spring.
import org.springframework.stereotype.Service;
//Importa a anotação para gerenciar transações no banco de dados(garante atomicidade na operação).
import org.springframework.transaction.annotation.Transactional;

//Importa o models.Task
import api_teste.ds.models.Task;
//Importa o models.User
import api_teste.ds.models.User;
//Importa a interface do repositorio responsavel pelas operações no banco de dados.
import api_teste.ds.repositories.TaskRepository;

//Anotação que indica para o Spring que essa classe contem as regras de negócio.
@Service
public class TaskService {

    private final DsApplication dsApplication;

    //Injeta automaticamente a  instancia do TaskRepository gerenciado pelo Spring.
    @Autowired
    private TaskRepository taskRepository;

    //Injeta automaticamente a instancia do UserService para validar o usuário.
    @Autowired
    private UserService userService;

    TaskService(DsApplication dsApplication) {
        this.dsApplication = dsApplication;
    }

    //Método para buscar Task a partir do ID.
    public Task findById(Long Id){
        //Executa a busca no banco, retorna um Optional contendo (ou não) a Task.
        Optional<Task> task = this.taskRepository.findById(Id);

        //Se a tarefa existir, retorna o objeto, se estiver vazio, lança um RuntimeException.
        return task.orElseThrow(()-> new RuntimeException(
            "Tarefa não encontrada! Id:"+ Id + ", Tipo:"+ Task.class.getName()
        ));
    }

    //Método para buscar todas as tarefas vinculadas a um determinado usuário.
    public List<Task> findByUserId(Long UserId){
        //Chama o UserService para garantir que o usuário existe no banco(lança exceção se não existir).
        this.userService.findById(UserId);

        //Executa a busca customizada no repositorio filtrando pelo Id do usuário.
        List<Task> tasks = this.taskRepository.findByUserId(UserId);

        //Retorna a lista de tarefas.
        return tasks;
    }

        //Garante que a criação ocorra dentro de uma transação de banco de dados(rolback automatico se falhar).
        @Transactional
        public Task create(Task obj){
            //Valida se o usuário informado no objeto realmente existe no banco e recupera seus dados.
            User user = this.userService.findById(obj.getUser().getId());
            
            //Define o ID como null para garantir que o JPA realize uma inserção(INSERT) e não uma atualização.
            obj.setId(id:null);

            //Associa a entidade User completa e valida a tarefa.
            obj.setUser(user);

            //Salva a nova tarefa no banco de dados e atualiza 'obj' com o ID gerado.
            obj = this.taskRepository.save(obj);

            //Retorna a tarefa salva.
            return obj;
        }

        //Garante que a atualização ocorra dentro de transação isolada no banco.
        @Transactional
        public Task update(Task obj){
            //Reaproveita findById para verificar se a tarefa a ser atualizada existe realmente.
            Task newObj = findById(obj.getId());

            //Atualiza apenas o campo descrição do objeto persistido com o novo valor.
            newObj.setDescription(obj.getDescription());

            //Salva a alteração no banco de dados e retorna o objeto atualizado.
            return this.taskRepository.save(newObj);
        }

        //Método para deletar uma tarefa pelo ID.
        public void delete(long Id){
            //Verifica se a tarefa existe antes de tentar deletar.
            findById(Id);

            try {
                //Solicita a remoção da tarefa no banco de dados pelo ID.
                this.taskRepository.deleteById(Id);
            } catch (Exception e){
                //Captura exceções como violações de chave estrangeira e lança uma mensagem amigavel.
                throw new RuntimeException("Não é possivel excluir pois não há tarefas relacionadas");
            }
        }
}