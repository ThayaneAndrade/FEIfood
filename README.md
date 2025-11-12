<img width="500" height="70" alt="image" src="https://github.com/user-attachments/assets/b741186d-56c9-4de5-a8e0-d6d703dd2948" />

**Desenvolvedora:** Thayane de Sousa Andrade 
**RA:** 22.224.003-8

Este projeto consite em uma aplicação desktop para a gestão de pedidos de alimentos, inspirado na plataforma IFood. Neste sistema é possível: fazer login, cadastro, pesquisar alimentos, montar pedido, finalizar pedido e avaliar pedido.

A liguagem de programação utilizada foi Java, utilizando o padrão **MVC (Model-View-Controller)** que garamte melhor organização e todos os dados presentes no projeto estão em um banco de dados   que foi inserido no código. 

## Funcionalidades 
### Contas 
  **Cadastro:** Um novo cliente pode criar uma conta fornecendo nome, usuario e senha. O sistema valida e insere os dados no 'tbusuarios'.
  <img width="500" height="488" alt="image" src="https://github.com/user-attachments/assets/bb36ce9e-be2c-4eef-a13a-6c339f79c78c" />

  **Login:** O usuario pode autenticar-se nesra funcionalidade. O sistema consulta o banco para validar as credenciais. 
  <img width="500" height="488" alt="image" src="https://github.com/user-attachments/assets/95474ddf-d8e7-4b5f-b1c8-48ae0e85e0fa" />

  **Minha Conta (Logado):** Após o Login é possivel acessar a área Minha Conta através de um botão no Menu de pesquisar item, nesta funcionalidade é possivel optar por alterar senha ou excluir cadastro do sistema. 
  <img width="500" height="488" alt="image" src="https://github.com/user-attachments/assets/44bfc23a-430a-4372-9ade-0d35d6269fe9" />

  
### Busca e Realização de Pedido
* **Buscar Alimento:** Na tela principal é possivel pesquisar por alimentos. O sistema faz uma consulta 'ILIKE' no 'tbalimentos' para retornar resultados por uma Jtable.
  <img width="860" height="560" alt="image" src="https://github.com/user-attachments/assets/3aecdc5b-58e9-410f-872e-1e85503983dc" />

* **Seleção Multipla:** O usuario pode selecionar múltiplos itens (usando 'Ctrl' ou 'Shift') na tabela de resultado de pesquisa
* **Lógica 'Carrinho de Compras':** Foi implementada a lógica de um carrinho, que é uma lista local no 'ControleBuscaAlimento'
      ***Adicionar Pedido:** Adiciona os itens selecionados ao carrinho;
      ***Ver Pedido:** Abre a tela 'GererenciarPedido' com um resumo de todos os itens do carrinho.
  
### Gestão de Pedidos
* **Criar e Gerir um Novo Pedido (GerenciarPedido):**
        * O usuário pode **remover itens** do seu pedido antes de salvá-lo através do botão 'remover item';
        * O ID do pedido e a Data são gerados automaticamente (esses campos não são editáveis);
        * Ao **Salvar**, o sistema insere os dados em 'tbpedidos' e 'tbpedido_alimento', também limpa o carrinho
  <img width="860" height="560" alt="image" src="https://github.com/user-attachments/assets/7ec3577a-a44c-4c1f-873f-943ccefe2bd4" />

* **Ver Histórico e Avaliar (MeusPedidos):**
       * Acessada através da tela "Meus Pedidos", esta tela lista todos os pedidos já feitos pelo usuário (buscando em 'tbpedidos' por 'usuario_id')
       * **Avaliar Pedido:** O usuario pode selecionar um pedido na tabela, escolher uma nota de 0 a 5 e clicar em "Avaliar".Essa ação chama o método 'atualizarAvaliacao' no DAO, que executa um 'UPDATE' no banco.
       * **Excluir Pedido:** É possível excluir um pedido do histórico.
  <img width="862" height="533" alt="image" src="https://github.com/user-attachments/assets/4c4562e1-c732-4421-b3f9-8760c0082f53" />

       * **Ver Itens:** Abre a tela 'GerenciarPedido' em modo "somente leitura" (botões ficam escondidos)
  <img width="862" height="533" alt="image" src="https://github.com/user-attachments/assets/0769245a-4f60-47ae-807c-ebd32bb52cb9" />

## MVC
* **Model:** Pacote 'Model'. Contém as classes que representam os dados, como 'Usuario', 'Pedido', 'Alimento', `Comida`, e `Bebida`.
  
 <img width="260" height="200" alt="image" src="https://github.com/user-attachments/assets/a48a098e-ef9b-4678-8075-040c6d989915" />

* **View:** Pacote 'View'. Contém todas as classes 'Jframe' (ex:'Login').
  
  <img width="260" height="200" alt="image" src="https://github.com/user-attachments/assets/816edc4b-2e14-4d8c-b81e-5be04311b713" />

* **Controller:** Pacote 'Controller'. Contém as classes de lógica (ex: 'ContoleLogin').
  
  <img width="260" height="200" alt="image" src="https://github.com/user-attachments/assets/c708d089-146e-4ed7-805f-f99cdeb555c2" />

* **DAO:** Pacote 'dao'. Contém as classes que executam os comandos SQL (ex: 'UsuarioDAO').
  
<img width="260" height="200" alt="image" src="https://github.com/user-attachments/assets/476800b6-ac6f-4542-a07e-3423a48a153c" />

## Conceitos Aplicados 
* **Herança:** A classe 'Alimento' foi definida como 'abstract' e serve como superclasse para 'Comida' e 'Bebida'.
* **Polimorfismo:**
  Foi criado a interface 'ImpostoAlcool'.
  A classe 'Bebida' implementa essa interface, fornecendo uma lógica para o método 'calcularImpostoAlcool'.
  Em 'ControlePedido ao calcular o preço total, o sistema verifica se há necessidade de chamar o método de cálculo de imposto.
