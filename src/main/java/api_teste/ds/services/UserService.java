
package api_teste.ds.services;

//Importa optional, usado para tratr valores que podem não estar presentes(evita NUllExpectionPointer).
import java.util.Optional;

import org.apache.catalina.User;
//Importa a anotação do Spring para injeção automatica de dependencias.
import org.springframework.beans.factory.annotation.Autowired;
//Importa a anotação que define essa classe como um componente de serviço gerenciado pelo Spring.
import org.springframework.stereotype.Service;
//Importa a anotação para gerenciar transações no banco de dados(garante atomicidade na operação).
import org.springframework.transaction.annotation.Transactional;

//Importa o models.Task
import api_teste.ds.models.Task;
//Importa a interface do repositório responsável pelas operações no banco de dados.
import api_teste.ds.repositories.TaskRepository;
//Importa a interface do repositório responsável pelas operações no banco de dados.
import api_teste.ds.repositories.UserRepository;

//Anotação que indica no Spring que essa classe contem as regras de negócios da entidade User.
@Service
public class UserService {
    //Injeta automaticamente a  instancia do UserRepository gerenciado pelo Spring.
    @Autowired
    private UserRepository userRepository;

    //Injeta automaticamente a  instancia do TaskRepository gerenciado pelo Spring.
    @Autowired
    private TaskRepository taskRepository;

    //
    public User findById(Long Id){
        //
        Optional<User> user = this.userRepository.findById(Id);

        //
        return user.orElseThrow(()-> new RuntimeException(
            "Usuario não encontrado!" + Id + ", Tipo:" + User.class.getName()
        ));
    }

    @Transactional
    public User create(User obj){
        //Define o ID como null para garantir que o JPA realize uma inserção(INSERT) e não uma atualização.
        obj.setId(null);

        //Salva a nova tarefa no banco de dados e atualiza 'obj' com o ID gerado.
        obj = this.userRepository.save(obj);

        //
        this.taskRepository.saveAll(obj.getClass());

        //Retorna a tarefa salva.
        return obj;
    }

    @Transactional
    public User update(User obj){
        //
        User newObj = findById(obj.getId());

        //
        newObj.setPassword(obj.getPassword());

        //
        return this.userRepository.save(newObj);
    }

    //
    public void delete(Long Id){
        //
        findById(Id);

        try {
            //
            this.userRepository.deleteById(Id);
        } catch (Exception e){
            //
            throw new RuntimeException("Não é possível exibir pois há entidade relacionadas");
    }
    }
    
}