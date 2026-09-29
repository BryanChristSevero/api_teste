//Url base da API Spring Boot para buscar as tarefas do usuário de ID 1.
const url = "http://localhost:8080/task/user/1";

//Função responsável por ocultar o icone de carregamento.
function hideLoader(){

    //Busca o elemento HTML com o ID 'loading' e altera seu estilo de exibição para oculta-lo
    document.getElementById("loading").style.display = "none";
}

//Função responsável por construir o html da tabela e preenche-lo com as tarefas.
function show(task){
    //Cria uma String contendo cabeçalho da tabela utilizando Template Literals.
    let tab = `
    <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Descrição</th>
            <th scope="col">Usuário</th>
            <th scope="col">User ID</th>
        </tr>
    </thead>
    `;
    //Iterador 'for...of'.
    for(let task of tasks) {
        //Concatena uma nova linha html (<tr>) com as colunas (<td>) preenchida com dados da tarefa.
        tab += `
        <tr>
        <td scope="row">${task.id}</td>
        <td>${task.description}</td>
        <td>${task.user.username}</td>
        <td>${task.user.id}</td>
        </tr>`
    }
    //Injeta a String acumulada diretamente na tabela através do ID 'tasks'.
    document.getElementById("tasks").innerHTML = tab
    //Função assincrona encarregada de realizar a requisição HTTP GET para a API.
    async function getAPI(url) {
        
        //Executa a requisição HTTP usando a API nativa fetch() e aguarda(wait) a resposta da rede.
        const response = await fetch(url,{method:"GET"});

        //Converte o corpo da resposta HTTP e formato JSON e guarda na variável
        var data = await response.json();

        //Se a resposta for obtida com sucesso, executa a função de ocultar o carregamento.
        if(response) {
            hideLoader();
        }
    }
}