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

        ProdutoDAO produtoDAO = new ProdutoDAO(em);

        List<Produto> todosOsProdutos = produtoDAO.buscarTodos();

        System.out.println("--- LISTA DE TODOS OS PRODUTOS CADASTRADOS ---");

        for (Produto produto : todosOsProdutos) {
            System.out.println("----------------------------------------");
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Preço: R$ " + produto.getPrecoVenda());
            System.out.println("Categoria: " + produto.getCategoria().getNome());
        }

        em.close();
        factory.close();
    }
}