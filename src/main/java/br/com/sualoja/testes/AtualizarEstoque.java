package br.com.sualoja.testes;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class AtualizarEstoque {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("loja-virtual");
        EntityManager em = factory.createEntityManager();

        ProdutoDAO produtoDAO = new ProdutoDAO(em);

        em.getTransaction().begin();

        Produto furadeira = produtoDAO.buscarPorId(1);
        
        if (furadeira != null) {
            System.out.println("Estoque atual: " + furadeira.getQuantidadeEstoque());

            furadeira.setQuantidadeEstoque(15);
            System.out.println("Novo estoque: " + furadeira.getQuantidadeEstoque());

            produtoDAO.atualizar(furadeira);
            System.out.println(">>> Estoque atualizado com sucesso!");
        } else {
            System.out.println("Produto com ID 1 não encontrado.");
        }

        em.getTransaction().commit();
        
        em.close();
        factory.close();
    }
}