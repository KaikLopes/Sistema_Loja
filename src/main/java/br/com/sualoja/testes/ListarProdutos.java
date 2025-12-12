package br.com.sualoja.testes;

import java.util.List;
import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ListarProdutos {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("loja-virtual");
        EntityManager em = factory.createEntityManager();

        // 1. Pegue a ferramenta DAO
        ProdutoDAO produtoDAO = new ProdutoDAO(em);

        // 2. Use a nova função da ferramenta para buscar a lista de todos os produtos
        List<Produto> todosOsProdutos = produtoDAO.buscarTodos();

        System.out.println("--- LISTA DE TODOS OS PRODUTOS CADASTRADOS ---");

        // 3. Percorra a lista e imprima as informações de cada produto
        for (Produto produto : todosOsProdutos) {
            System.out.println("----------------------------------------");
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Preço: R$ " + produto.getPrecoVenda());
            System.out.println("Categoria: " + produto.getCategoria().getNome());
        }

        // 4. Feche a conexão
        em.close();
        factory.close();
    }
}